package com.nong.nongo2o.entities.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-9-11.
 */

public class Good implements Parcelable {

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
    private String goodsCode;
    private String category;
    private String categoryCode;
    private String userCode;
    private String title;
    private String subTitle;
    private BigDecimal price;
    private int goodsStatus;
    private String summary;
    private String detail;
    private String covers;
    private BigDecimal freight;
    private int totalSale;
    private User sale;
    private List<GoodSpec> goodsSpecs;

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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCovers() {
        return covers;
    }

    public void setCovers(String covers) {
        this.covers = covers;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(int totalSale) {
        this.totalSale = totalSale;
    }

    public User getSale() {
        return sale;
    }

    public void setSale(User sale) {
        this.sale = sale;
    }

    public List<GoodSpec> getGoodsSpecs() {
        return goodsSpecs;
    }

    public void setGoodsSpecs(List<GoodSpec> goodsSpecs) {
        this.goodsSpecs = goodsSpecs;
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
        dest.writeString(this.goodsCode);
        dest.writeString(this.category);
        dest.writeString(this.categoryCode);
        dest.writeString(this.userCode);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeSerializable(this.price);
        dest.writeInt(this.goodsStatus);
        dest.writeString(this.summary);
        dest.writeString(this.detail);
        dest.writeString(this.covers);
        dest.writeSerializable(this.freight);
        dest.writeInt(this.totalSale);
        dest.writeParcelable(this.sale, flags);
        dest.writeList(this.goodsSpecs);
    }

    public Good() {
    }

    protected Good(Parcel in) {
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
        this.goodsCode = in.readString();
        this.category = in.readString();
        this.categoryCode = in.readString();
        this.userCode = in.readString();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.price = (BigDecimal) in.readSerializable();
        this.goodsStatus = in.readInt();
        this.summary = in.readString();
        this.detail = in.readString();
        this.covers = in.readString();
        this.freight = (BigDecimal) in.readSerializable();
        this.totalSale = in.readInt();
        this.sale = in.readParcelable(User.class.getClassLoader());
        this.goodsSpecs = new ArrayList<GoodSpec>();
        in.readList(this.goodsSpecs, GoodSpec.class.getClassLoader());
    }

    public static final Parcelable.Creator<Good> CREATOR = new Parcelable.Creator<Good>() {
        @Override
        public Good createFromParcel(Parcel source) {
            return new Good(source);
        }

        @Override
        public Good[] newArray(int size) {
            return new Good[size];
        }
    };
}
