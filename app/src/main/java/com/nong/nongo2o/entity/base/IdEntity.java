package com.nong.nongo2o.entity.base;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-9-12.
 */

public class IdEntity implements Serializable{
    private static final long serialVersionUID = -4674398859076520064L;
    protected String id;
    protected Integer recVer;

    public IdEntity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRecVer() {
        return this.recVer;
    }

    public void setRecVer(Integer recVer) {
        this.recVer = recVer;
    }
}
