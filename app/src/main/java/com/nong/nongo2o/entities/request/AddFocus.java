package com.nong.nongo2o.entities.request;

/**
 * Created by Administrator on 2017-8-22.
 */

public class AddFocus {

    /**
     * 关注方用户编码
     */
    private String userCode;
    /**
     * 被关注方用户编码
     */
    private String targetCode;

    public AddFocus(String userCode, String targetCode) {
        this.userCode = userCode;
        this.targetCode = targetCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }
}
