package com.mypinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypinyougou.entity.Result;
import com.mypinyougou.pojo.TbSpecification;
import com.mypinyougou.sellergoods.service.TbSpecificationService;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.vo.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference
    private TbSpecificationService tbSpecificationService;

    @RequestMapping("/findPage/{page}/{size}")
    public PageResult<TbSpecification> findPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        return tbSpecificationService.findPage(page, size);
    }

    @RequestMapping("/findOne/{id}")
    public Specification findOne(@PathVariable("id") Long id) {
        return tbSpecificationService.findOne(id);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Specification specification) {
        try {
            tbSpecificationService.add(specification);
            return new Result(true, "操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败!");
        }
    }

    @RequestMapping("/delete/{ids}")
    public Result delete(@PathVariable("ids") Long[] ids) {
        try {
            tbSpecificationService.delete(ids);
            return new Result(true, "操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败!");
        }
    }

}
