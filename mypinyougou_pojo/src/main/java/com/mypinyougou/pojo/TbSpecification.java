package com.mypinyougou.pojo;

import java.io.Serializable;

public class TbSpecification implements Serializable {
    private Long id;

    private String specName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName == null ? null : specName.trim();
    }

    //为了匹配select2的key: text
    public String getText() {
        return this.specName;
    }
}