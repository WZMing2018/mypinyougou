package com.mypinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mypinyougou.mapper.TbBrandMapper;
import com.mypinyougou.pojo.TbBrand;
import com.mypinyougou.sellergoods.service.TbBrandService;
import com.mypinyougou.entity.PageResult;
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
        return tbBrandMapper.selectByExample(null);
    }

    @Override
    public PageResult<TbBrand> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<TbBrand> pageReturn = (Page<TbBrand>) tbBrandMapper.selectByExample(null);
        return new PageResult<>(pageReturn.getTotal(), pageReturn.getResult());
    }

    @Override
    public void add(TbBrand tbBrand) {
        if (tbBrand.getId() == null) {
            tbBrandMapper.insert(tbBrand);
        } else {
            tbBrandMapper.updateByPrimaryKey(tbBrand);
        }
    }

    @Override
    public TbBrand findOne(Long id) {
        return tbBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }
}
