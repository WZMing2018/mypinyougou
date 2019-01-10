package com.mypinyougou.sellergoods.service;

import com.mypinyougou.pojo.TbSpecification;
import com.mypinyougou.entity.PageResult;
import com.mypinyougou.vo.Specification;

import java.util.List;
import java.util.Map;

public interface TbSpecificationService {
    public List<TbSpecification> findAll();

    PageResult<TbSpecification> findPage(int page, int size);

    void add(Specification specification);

    Specification findOne(Long id);

    void delete(Long[] ids);

    List<Map> findSpecAndOption(Long typeId);
}
