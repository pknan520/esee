package com.nong.nongo2o.entities.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-8-21.
 */

public class Address implements Parcelable {

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
    private String addressCode;
    private String consigneeName;
    private String consigneeMobile;
    private String consigneePhoneno;
    private String consigneeCountryCode;
    private String consigneeProvince;
    private String consigneeCity;
    private String consigneeArea;
    private String consigneeTown;
    private String consigneeAddress;
    private String consigneeZipCode;
    private int defaultAddr;
    private String userCode;

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

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeMobile() {
        return consigneeMobile;
    }

    public void setConsigneeMobile(String consigneeMobile) {
        this.consigneeMobile = consigneeMobile;
    }

    public String getConsigneePhoneno() {
        return consigneePhoneno;
    }

    public void setConsigneePhoneno(String consigneePhoneno) {
        this.consigneePhoneno = consigneePhoneno;
    }

    public String getConsigneeCountryCode() {
        return consigneeCountryCode;
    }

    public void setConsigneeCountryCode(String consigneeCountryCode) {
        this.consigneeCountryCode = consigneeCountryCode;
    }

    public String getConsigneeProvince() {
        return consigneeProvince;
    }

    public void setConsigneeProvince(String consigneeProvince) {
        this.consigneeProvince = consigneeProvince;
    }

    public String getConsigneeCity() {
        return consigneeCity;
    }

    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity;
    }

    public String getConsigneeArea() {
        return consigneeArea;
    }

    public void setConsigneeArea(String consigneeArea) {
        this.consigneeArea = consigneeArea;
    }

    public String getConsigneeTown() {
        return consigneeTown;
    }

    public void setConsigneeTown(String consigneeTown) {
        this.consigneeTown = consigneeTown;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeZipCode() {
        return consigneeZipCode;
    }

    public void setConsigneeZipCode(String consigneeZipCode) {
        this.consigneeZipCode = consigneeZipCode;
    }

    public int getDefaultAddr() {
        return defaultAddr;
    }

    public void setDefaultAddr(int defaultAddr) {
        this.defaultAddr = defaultAddr;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


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
        dest.writeString(this.addressCode);
        dest.writeString(this.consigneeName);
        dest.writeString(this.consigneeMobile);
        dest.writeString(this.consigneePhoneno);
        dest.writeString(this.consigneeCountryCode);
        dest.writeString(this.consigneeProvince);
        dest.writeString(this.consigneeCity);
        dest.writeString(this.consigneeArea);
        dest.writeString(this.consigneeTown);
        dest.writeString(this.consigneeAddress);
        dest.writeString(this.consigneeZipCode);
        dest.writeInt(this.defaultAddr);
        dest.writeString(this.userCode);
    }

    public Address() {
    }

    protected Address(Parcel in) {
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
        this.addressCode = in.readString();
        this.consigneeName = in.readString();
        this.consigneeMobile = in.readString();
        this.consigneePhoneno = in.readString();
        this.consigneeCountryCode = in.readString();
        this.consigneeProvince = in.readString();
        this.consigneeCity = in.readString();
        this.consigneeArea = in.readString();
        this.consigneeTown = in.readString();
        this.consigneeAddress = in.readString();
        this.consigneeZipCode = in.readString();
        this.defaultAddr = in.readInt();
        this.userCode = in.readString();
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
