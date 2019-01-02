package com.mypinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mypinyougou.pojo.TbBrand;
import com.mypinyougou.sellergoods.service.TbBrandService;
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

}
