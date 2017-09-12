package com.nong.nongo2o.entities.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017-9-11.
 */

public class GoodSpec implements Parcelable {

    private String id;
    private int recVer;
    private int status;
    private String tenantCode;
    private String companyCodes;
    private String siteCodes;
    private String whCodes;
    private long createTime;
    private String createUserId;
    private long updateTime;
    private String updateUserId;
    private String remark;
    private String specCode;
    private String goodsCode;
    private String title;
    private BigDecimal price;
    private int quantity;
    private String detail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRecVer() {
        return recVer;
    }

    public void setRecVer(int recVer) {
        this.recVer = recVer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getCompanyCodes() {
        return companyCodes;
    }

    public void setCompanyCodes(String companyCodes) {
        this.companyCodes = companyCodes;
    }

    public String getSiteCodes() {
        return siteCodes;
    }

    public void setSiteCodes(String siteCodes) {
        this.siteCodes = siteCodes;
    }

    public String getWhCodes() {
        return whCodes;
    }

    public void setWhCodes(String whCodes) {
        this.whCodes = whCodes;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Parcelable
     */


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.recVer);
        dest.writeInt(this.status);
        dest.writeString(this.tenantCode);
        dest.writeString(this.companyCodes);
        dest.writeString(this.siteCodes);
        dest.writeString(this.whCodes);
        dest.writeLong(this.createTime);
        dest.writeString(this.createUserId);
        dest.writeLong(this.updateTime);
        dest.writeString(this.updateUserId);
        dest.writeString(this.remark);
        dest.writeString(this.specCode);
        dest.writeString(this.goodsCode);
        dest.writeString(this.title);
        dest.writeSerializable(this.price);
        dest.writeInt(this.quantity);
        dest.writeString(this.detail);
    }

    public GoodSpec() {
    }

    protected GoodSpec(Parcel in) {
        this.id = in.readString();
        this.recVer = in.readInt();
        this.status = in.readInt();
        this.tenantCode = in.readString();
        this.companyCodes = in.readString();
        this.siteCodes = in.readString();
        this.whCodes = in.readString();
        this.createTime = in.readLong();
        this.createUserId = in.readString();
        this.updateTime = in.readLong();
        this.updateUserId = in.readString();
        this.remark = in.readString();
        this.specCode = in.readString();
        this.goodsCode = in.readString();
        this.title = in.readString();
        this.price = (BigDecimal) in.readSerializable();
        this.quantity = in.readInt();
        this.detail = in.readString();
    }

    public static final Parcelable.Creator<GoodSpec> CREATOR = new Parcelable.Creator<GoodSpec>() {
        @Override
        public GoodSpec createFromParcel(Parcel source) {
            return new GoodSpec(source);
        }

        @Override
        public GoodSpec[] newArray(int size) {
            return new GoodSpec[size];
        }
    };
}
