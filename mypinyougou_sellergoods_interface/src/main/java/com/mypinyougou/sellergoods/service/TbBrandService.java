package com.mypinyougou.sellergoods.service;

import com.mypinyougou.pojo.TbBrand;
import com.mypinyougou.utils.PageResult;

import java.util.List;

public interface TbBrandService {
    public List<TbBrand> findAll();

    PageResult<TbBrand> findPage(int page, int size);

    void add(TbBrand tbBrand);

    TbBrand findOne(Long id);

    void delete(Long[] ids);
}
