package com.mypinyougou.vo;

import com.mypinyougou.pojo.TbSpecification;
import com.mypinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

public class Specification implements Serializable {
    private TbSpecification spec;
    private List<TbSpecificationOption> optionList;

    public Specification() {
    }

    public Specification(TbSpecification spec, List<TbSpecificationOption> optionList) {
        this.spec = spec;
        this.optionList = optionList;
    }

    public TbSpecification getSpec() {
        return spec;
    }

    public void setSpec(TbSpecification spec) {
        this.spec = spec;
    }

    public List<TbSpecificationOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<TbSpecificationOption> optionList) {
        this.optionList = optionList;
    }
}
