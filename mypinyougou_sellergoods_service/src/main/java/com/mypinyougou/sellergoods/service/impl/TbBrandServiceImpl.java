package com.mypinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mypinyougou.mapper.TbBrandMapper;
import com.mypinyougou.pojo.TbBrand;
import com.mypinyougou.sellergoods.service.TbBrandService;
import com.mypinyougou.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TbBrandServiceImpl implements TbBrandService {

    @Autowired
    private TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.findAll();
    }

    @Override
    public PageResult<TbBrand> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<TbBrand> pageReturn = (Page<TbBrand>) tbBrandMapper.findAll();
        return new PageResult<>(pageReturn.getTotal(), pageReturn.getResult());
    }

    @Override
    public void add(TbBrand tbBrand) {
        if (tbBrand.getId() == null) {
            tbBrandMapper.add(tbBrand);
        } else {
            tbBrandMapper.update(tbBrand);
        }
    }

    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.findOne(id);
    }
}
