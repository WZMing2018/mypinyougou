package com.mypinyougou.mapper;

import com.mypinyougou.pojo.TbBrand;

import java.util.List;

public interface TbBrandMapper {

    public List<TbBrand> findAll();

    void add(TbBrand tbBrand);

    TbBrand findOne(Long id);

    void update(TbBrand tbBrand);

    void delete(Long id);
}
