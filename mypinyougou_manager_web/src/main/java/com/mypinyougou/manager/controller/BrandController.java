package com.mypinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypinyougou.entity.Result;
import com.mypinyougou.pojo.TbBrand;
import com.mypinyougou.sellergoods.service.TbBrandService;
import com.mypinyougou.utils.PageResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private TbBrandService tbBrandService;

    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return tbBrandService.findAll();
    }

    @RequestMapping("/findPage/{page}/{size}")
    public PageResult<TbBrand> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        return tbBrandService.findPage(page, size);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand tbBrand) {
        try {
            tbBrandService.add(tbBrand);
            return new Result(true, "操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败!");
        }
    }

    @RequestMapping("/edit/{id}")
    public TbBrand findOne(@PathVariable("id") Long id) {
        return tbBrandService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids) {
        try {
            tbBrandService.delete(ids);
            return new Result(true, "操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败!");
        }
    }

}
