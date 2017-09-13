package com.nong.nongo2o.entity.base;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by HEJF7 on 2017-9-13.
 */

public class IdRequest extends Request {
    private static final long serialVersionUID = -404071043021358806L;
    @NotNull
    private String id;
    private Integer recVer;

    public IdRequest() {
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
