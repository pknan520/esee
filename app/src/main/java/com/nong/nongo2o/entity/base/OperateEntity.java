package com.nong.nongo2o.entity.base;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017-9-12.
 */

public class OperateEntity extends IdEntity{

    private static final long serialVersionUID = 6339468883299171311L;
    public static final Integer STATUS_NORMAL = Integer.valueOf(0);
    public static final Integer STATUS_DELETE = Integer.valueOf(1);
    protected Integer status = Integer.valueOf(0);
    protected String tenantCode;
    protected List<String> companyCodes;
    protected List<String> siteCodes;
    protected List<String> whCodes;
    protected Date createTime;
    protected String createUserId;
    protected Date updateTime;
    protected String updateUserId;
    protected String remark;

    public OperateEntity() {
    }

    public List<String> getCompanyCodes() {
        return this.companyCodes;
    }

    public void setCompanyCodes(List<String> companyCodes) {
        this.companyCodes = companyCodes;
    }

    public List<String> getSiteCodes() {
        return this.siteCodes;
    }

    public void setSiteCodes(List<String> siteCodes) {
        this.siteCodes = siteCodes;
    }

    public List<String> getWhCodes() {
        return this.whCodes;
    }

    public void setWhCodes(List<String> whCodes) {
        this.whCodes = whCodes;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTenantCode() {
        return this.tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
