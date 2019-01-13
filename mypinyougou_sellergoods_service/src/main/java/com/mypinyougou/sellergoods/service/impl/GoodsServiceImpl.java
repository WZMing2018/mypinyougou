package com.mypinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.mapper.*;
import com.mypinyougou.pojo.*;
import com.mypinyougou.sellergoods.service.GoodsService;
import com.mypinyougou.vo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
     * @param goods
     */
	@Override
	public void add(Goods goods) {

		TbGoods tbGoods = goods.getTbGoods();
		TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();

		//设置商品默认状态 未审核:0
		tbGoods.setAuditStatus("0");
		goodsMapper.insert(tbGoods);

		//关联GoodsId
		tbGoodsDesc.setGoodsId(tbGoods.getId());
		goodsDescMapper.insert(tbGoodsDesc);

		//添加SKU
		if ("1".equals(tbGoods.getIsEnableSpec())) {//启用规格
			for (TbItem tbItem : goods.getTbItems()) {
				//标题
				String tile = tbGoods.getGoodsName();
				Map<String,Object> specMap  = JSON.parseObject(tbItem.getSpec());
				for (String key : specMap.keySet()) {
					tile += " " + specMap.get(key);
				}
				tbItem.setTitle(tile);
				//设置其他字段的值
				setItemValues(tbGoods, tbGoodsDesc, tbItem);
				//保存到数据库
				itemMapper.insert(tbItem);
			}
		} else {
			TbItem tbItem = new TbItem();
			tbItem.setPrice(tbGoods.getPrice());
			tbItem.setNum(99999);
			tbItem.setStatus("1");
			tbItem.setIsDefault("1");
			tbItem.setSpec("{}");
			//标题
			tbItem.setTitle(tbGoods.getGoodsName());
			//设置其他字段的值
			setItemValues(tbGoods, tbGoodsDesc, tbItem);
			//保存到数据库
			itemMapper.insert(tbItem);
		}
	}

	/**
	 * 设置其他字段的值
	 * @param tbGoods
	 * @param tbGoodsDesc
	 * @param tbItem
	 */
	private void setItemValues(TbGoods tbGoods, TbGoodsDesc tbGoodsDesc, TbItem tbItem) {
		//商品 SPU 编号
		tbItem.setGoodsId(tbGoods.getId());
		//商家编号
		tbItem.setSellerId(tbGoods.getSellerId());
		//商品分类编号（3 级）
		tbItem.setCategoryid(tbGoods.getCategory3Id());
		//创建日期
		tbItem.setCreateTime(new Date());
		//修改日期
		tbItem.setUpdateTime(new Date());
		//品牌名称
		TbBrand tbBrand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
		tbItem.setBrand(tbBrand.getName());
		//分类名称
		TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbItem.getCategoryid());
		tbItem.setCategory(tbItemCat.getName());
		//商家名称
		TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbItem.getSellerId());
		tbItem.setSeller(tbSeller.getNickName());
		//图片地址（取 spu 的第一个图片）
		List<Map> itemImageList = JSON.parseArray(tbGoodsDesc.getItemImages(), Map.class);
		if (itemImageList.size() > 0) {
            tbItem.setImage((String) itemImageList.get(0).get("url"));
        }
	}


	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		goodsMapper.updateByPrimaryKey(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		TbGoodsExample.Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusEqualTo(goods.getAuditStatus());
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}
	
}
