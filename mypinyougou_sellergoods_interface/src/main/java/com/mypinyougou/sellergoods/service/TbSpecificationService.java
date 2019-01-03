package com.mypinyougou.sellergoods.service;

import com.mypinyougou.pojo.TbSpecification;
import com.mypinyougou.utils.PageResult;
import com.mypinyougou.vo.Specification;

import java.util.List;

public interface TbSpecificationService {
    public List<TbSpecification> findAll();

    PageResult<TbSpecification> findPage(int page, int size);

    void add(Specification specification);

    TbSpecification findOne(Long id);

    void delete(Long[] ids);
}
