package com.mypinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mypinyougou.content.service.ContentService;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.mapper.TbContentMapper;
import com.mypinyougou.pojo.TbContent;
import com.mypinyougou.pojo.TbContentExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        Long categoryId = content.getCategoryId();
        contentMapper.insert(content);
        //同步缓存
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        if (contentList != null) {
            redisTemplate.boundHashOps("content").delete(categoryId);
        }
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //页面传过来的categoryId
        Long newCategoryId = content.getCategoryId();

        //mysql数据库中的categoryId
        Long id = content.getId();
        TbContent tbContent = contentMapper.selectByPrimaryKey(id);
        Long oldCategoryId = tbContent.getCategoryId();

        contentMapper.updateByPrimaryKey(content);
        //同步缓存
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(newCategoryId);
        if (contentList != null) {
            redisTemplate.boundHashOps("content").delete(newCategoryId);
        }
        if (newCategoryId != oldCategoryId) {
            contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(oldCategoryId);
            if (contentList != null) {
                redisTemplate.boundHashOps("content").delete(oldCategoryId);
            }
        }

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            TbContent tbContent = contentMapper.selectByPrimaryKey(id);
            Long categoryId = tbContent.getCategoryId();
            contentMapper.deleteByPrimaryKey(id);
            //同步缓存
            List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
            if (contentList != null) {
                redisTemplate.boundHashOps("content").delete(categoryId);
            }
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbContent> findContentListByCategoryId(Long categoryId) {
        //首先从缓存中查询
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);

        if (contentList == null || contentList.size() <= 0) {//缓存中没有数据
            TbContentExample example = new TbContentExample();
            example.setOrderByClause("sort_order ASC");
            TbContentExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            criteria.andStatusEqualTo("1");
            contentList = contentMapper.selectByExample(example);
            //放入Redis缓存中
            redisTemplate.boundHashOps("content").put(categoryId, contentList);
            System.out.println("FORM MYSQL");
            return contentList;
        }

        System.out.println("FORM REDIS");
        return contentList;
    }

}
