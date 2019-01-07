package com.mypinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.entity.Result;
import com.mypinyougou.pojo.TbSeller;
import com.mypinyougou.sellergoods.service.SellerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage/{page}/{size}")
	public PageResult findPage(@PathVariable("page") int page, @PathVariable("size")int size){
		return sellerService.findPage(page, size);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeller seller){
		try {
			sellerService.add(seller);
			return new Result(true, "入驻成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "入驻失败!");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	public TbSeller findOne(@PathVariable("id") String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete/{ids}")
	public Result delete(@PathVariable("ids") Long [] ids){
		try {
			sellerService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param seller
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search/{page}/{rows}")
	public PageResult search(@RequestBody TbSeller seller, @PathVariable("page") int page,  @PathVariable("rows") int rows  ){
		return sellerService.findPage(seller, page, rows);		
	}

	@RequestMapping("/updateStatus/{id}/{status}")
	public Result updateStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
		try {
			sellerService.updateStatus(id, status);
			return new Result(true, "审核成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "审核失败");
		}
	}
	
}
