package com.nong.nongo2o.entities.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-8-21.
 */

public class Fans implements Parcelable {

    private String id;
    /**
     * 关注方用户编码
     */
    private String userCode;
    /**
     * 被关注方用户编码
     */
    private String targetCode;

    /**
     * 是否互相关注？ 1--互相关注 0--否
     */
    private Integer eachOther;

    /**
     * 我的粉丝
     */
    private FansInfo userInfo;

    /**
     * 我的关注
     */
    private FansInfo targetUserInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getEachOther() {
        return eachOther;
    }

    public void setEachOther(Integer eachOther) {
        this.eachOther = eachOther;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTargetCode() {
        return this.targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public FansInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(FansInfo userInfo) {
        this.userInfo = userInfo;
    }

    public FansInfo getTargetUserInfo() {
        return targetUserInfo;
    }

    public void setTargetUserInfo(FansInfo targetUserInfo) {
        this.targetUserInfo = targetUserInfo;
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
        dest.writeString(this.userCode);
        dest.writeString(this.targetCode);
        dest.writeValue(this.eachOther);
        dest.writeParcelable(this.userInfo, flags);
        dest.writeParcelable(this.targetUserInfo, flags);
    }

    public Fans() {
    }

    protected Fans(Parcel in) {
        this.id = in.readString();
        this.userCode = in.readString();
        this.targetCode = in.readString();
        this.eachOther = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userInfo = in.readParcelable(FansInfo.class.getClassLoader());
        this.targetUserInfo = in.readParcelable(FansInfo.class.getClassLoader());
    }

    public static final Creator<Fans> CREATOR = new Creator<Fans>() {
        @Override
        public Fans createFromParcel(Parcel source) {
            return new Fans(source);
        }

        @Override
        public Fans[] newArray(int size) {
            return new Fans[size];
        }
    };
}
