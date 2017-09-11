package com.nong.nongo2o.entities.request;

/**
 * Created by Administrator on 2017-8-26.
 */

public class LikeDynamic {

    private String momentCode;
    private String userCode;

    public LikeDynamic(String momentCode, String userCode) {
        this.momentCode = momentCode;
        this.userCode = userCode;
    }

    public String getMomentCode() {
        return momentCode;
    }

    public void setMomentCode(String momentCode) {
        this.momentCode = momentCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
