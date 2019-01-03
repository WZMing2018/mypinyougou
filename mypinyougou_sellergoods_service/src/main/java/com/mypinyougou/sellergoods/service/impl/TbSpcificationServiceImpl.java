package com.mypinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mypinyougou.mapper.TbSpecificationMapper;
import com.mypinyougou.mapper.TbSpecificationOptionMapper;
import com.mypinyougou.pojo.TbSpecification;
import com.mypinyougou.pojo.TbSpecificationOption;
import com.mypinyougou.sellergoods.service.TbSpecificationService;
import com.mypinyougou.utils.PageResult;
import com.mypinyougou.vo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TbSpcificationServiceImpl implements TbSpecificationService {

    @Autowired
    private TbSpecificationMapper tbSpecificationMapper;
    @Autowired
    private TbSpecificationOptionMapper tbSpecificationOptionMapper;

    @Override
    public List<TbSpecification> findAll() {
        return tbSpecificationMapper.selectByExample(null);
    }

    @Override
    public PageResult<TbSpecification> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<TbSpecification> pageReturn = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(null);
        return new PageResult<>(pageReturn.getTotal(), pageReturn.getResult());
    }

    @Override
    public void add(Specification specification) {
        TbSpecification spec = specification.getSpec();
        tbSpecificationMapper.insert(spec);
        List<TbSpecificationOption> optionList = specification.getOptionList();
        for (TbSpecificationOption specificationOption : optionList) {
            specificationOption.setSpecId(spec.getId());
            tbSpecificationOptionMapper.insert(specificationOption);
        }
    }

    @Override
    public TbSpecification findOne(Long id) {
        return tbSpecificationMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            tbSpecificationMapper.deleteByPrimaryKey(id);
        }
    }
}
