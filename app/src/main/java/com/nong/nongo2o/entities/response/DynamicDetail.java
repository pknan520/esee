package com.nong.nongo2o.entities.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-8-25.
 */

public class DynamicDetail implements Parcelable {

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
    private String momentCode;
    private String userCode;
    private String goodsCode;
    private String headerImg;
    private String title;
    private String content;
    private String provinceCode;
    private String cityCode;
    private String areaCode;
    private String goods;
    private User user;
    private int isFavorite; //  1-已点赞，0-未点赞
    private int favorite;

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

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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
        dest.writeString(this.momentCode);
        dest.writeString(this.userCode);
        dest.writeString(this.goodsCode);
        dest.writeString(this.headerImg);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.provinceCode);
        dest.writeString(this.cityCode);
        dest.writeString(this.areaCode);
        dest.writeString(this.goods);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.isFavorite);
        dest.writeInt(this.favorite);
    }

    public DynamicDetail() {
    }

    protected DynamicDetail(Parcel in) {
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
        this.momentCode = in.readString();
        this.userCode = in.readString();
        this.goodsCode = in.readString();
        this.headerImg = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.provinceCode = in.readString();
        this.cityCode = in.readString();
        this.areaCode = in.readString();
        this.goods = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.isFavorite = in.readInt();
        this.favorite = in.readInt();
    }

    public static final Parcelable.Creator<DynamicDetail> CREATOR = new Parcelable.Creator<DynamicDetail>() {
        @Override
        public DynamicDetail createFromParcel(Parcel source) {
            return new DynamicDetail(source);
        }

        @Override
        public DynamicDetail[] newArray(int size) {
            return new DynamicDetail[size];
        }
    };
}
