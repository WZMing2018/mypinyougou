package com.mypinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mypinyougou.mapper.TbSpecificationMapper;
import com.mypinyougou.mapper.TbSpecificationOptionMapper;
import com.mypinyougou.mapper.TbTypeTemplateMapper;
import com.mypinyougou.pojo.TbSpecification;
import com.mypinyougou.pojo.TbSpecificationOption;
import com.mypinyougou.pojo.TbSpecificationOptionExample;
import com.mypinyougou.pojo.TbTypeTemplate;
import com.mypinyougou.sellergoods.service.TbSpecificationService;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.vo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TbSpcificationServiceImpl implements TbSpecificationService {

    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;
    @Autowired
    private TbTypeTemplateMapper tbTypeTemplateMapper;

    @Override
    public List<TbSpecification> findAll() {
        return tbSpecificationMapper.selectByExample(null);
    }

    @Override
    public PageResult<TbSpecification> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<TbSpecification> pageReturn = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(null);
        return new PageResult<>(pageReturn.getTotal(), pageReturn.getResult());
    }

    /**
     * 修改规格数据回显
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        TbSpecification spec = tbSpecificationMapper.selectByPrimaryKey(id);

        TbSpecificationOptionExample example = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> optionList = tbSpecificationOptionMapper.selectByExample(example);

        return new Specification(spec, optionList);
    }

    /**
     * 规格保存
     * @param specification
     */
    @Override
    public void add(Specification specification) {
        TbSpecification spec = specification.getSpec();
        List<TbSpecificationOption> optionList = specification.getOptionList();

        if (spec.getId() == null) {// 插入
            tbSpecificationMapper.insert(spec);
            for (TbSpecificationOption specificationOption : optionList) {
                specificationOption.setSpecId(spec.getId());
                tbSpecificationOptionMapper.insert(specificationOption);
            }
        } else {// 更新
            tbSpecificationMapper.updateByPrimaryKey(spec);//规格主表直接更新

            //规格从表先删除
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(spec.getId());
            tbSpecificationOptionMapper.deleteByExample(example);
            //再插入
            for (TbSpecificationOption specificationOption : optionList) {
                specificationOption.setSpecId(spec.getId());
                tbSpecificationOptionMapper.insert(specificationOption);
            }
        }

    }

    /**
     * 规格删除
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //删除规格主表
            tbSpecificationMapper.deleteByPrimaryKey(id);
            //删除规格从表
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(id);
            tbSpecificationOptionMapper.deleteByExample(example);
        }
    }

    /**
     * 查询规格以及规格选项
     * @param typeId
     * @return
     */
    @Override
    public List<Map> findSpecAndOption(Long typeId) {
        TbTypeTemplate tbTypeTemplate = tbTypeTemplateMapper.selectByPrimaryKey(typeId);
        List<Map> specMap = JSON.parseArray(tbTypeTemplate.getSpecIds(), Map.class);
        for (Map spec : specMap) {
            TbSpecificationOptionExample example = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
            criteria.andSpecIdEqualTo(Long.valueOf(String.valueOf(spec.get("id"))));
            List<TbSpecificationOption> options = tbSpecificationOptionMapper.selectByExample(example);
            spec.put("options", options);
        }
        return specMap;
    }
}
