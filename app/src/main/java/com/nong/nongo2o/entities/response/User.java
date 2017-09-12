package com.nong.nongo2o.entities.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017-8-4.
 */
public class User implements Parcelable {
    private static User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    private String id;
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String userNick;
    /**
     * 用户状态 0-invalid  1-正常
     */
    private Integer userStatus;
    /**
     * 用户类型： 0-用户 1-商家
     */
    private Integer userType;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 关注我的数量
     */
    private Integer followers;
    /**
     * 我关注的他人的数量
     */
    private Integer followOthers;
    /**
     * 电话号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 性别： 0-男 1-女
     */
    private Integer sex;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 个人简介
     */
    private String profile;
    /**
     * 所属地区
     */
    private String location;
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 身份证正面照片
     */
    private String idFront;
    /**
     * 身份证反面照片
     */
    private String idBack;
    /**
     * 总收入
     */
    private java.math.BigDecimal totalAsset;
    /**
     * 可提现
     */
    private java.math.BigDecimal balance;

    /**
     * app登录token
     */
    private String sessionToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Integer getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserType() {
        return this.userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getFollowers() {
        return this.followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowOthers() {
        return this.followOthers;
    }

    public void setFollowOthers(Integer followOthers) {
        this.followOthers = followOthers;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile() {
        return this.profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdFront() {
        return this.idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public String getIdBack() {
        return this.idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public java.math.BigDecimal getTotalAsset() {
        return this.totalAsset;
    }

    public void setTotalAsset(java.math.BigDecimal totalAsset) {
        this.totalAsset = totalAsset;
    }

    public java.math.BigDecimal getBalance() {
        return this.balance;
    }

    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void setUser(User user) {
        ourInstance.setId(user.getId());
        ourInstance.setUserCode(user.getUserCode());
        ourInstance.setUserName(user.getUserName());
        ourInstance.setUserNick(user.getUserNick());
        ourInstance.setUserStatus(user.getUserStatus());
        ourInstance.setUserType(user.getUserType());
        ourInstance.setAvatar(user.getAvatar());
        ourInstance.setFollowers(user.getFollowers());
        ourInstance.setFollowOthers(user.getFollowOthers());
        ourInstance.setPhone(user.getPhone());
        ourInstance.setEmail(user.getEmail());
        ourInstance.setSex(user.getSex());
        ourInstance.setPassword(user.getPassword());
        ourInstance.setProfile(user.getProfile());
        ourInstance.setLocation(user.getLocation());
        ourInstance.setIdNumber(user.getIdNumber());
        ourInstance.setIdFront(user.getIdFront());
        ourInstance.setIdBack(user.getIdBack());
        ourInstance.setTotalAsset(user.getTotalAsset());
        ourInstance.setBalance(user.getBalance());
        ourInstance.setSessionToken(user.getSessionToken());
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
        dest.writeString(this.userName);
        dest.writeString(this.userNick);
        dest.writeValue(this.userStatus);
        dest.writeValue(this.userType);
        dest.writeString(this.avatar);
        dest.writeValue(this.followers);
        dest.writeValue(this.followOthers);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeValue(this.sex);
        dest.writeString(this.password);
        dest.writeString(this.profile);
        dest.writeString(this.location);
        dest.writeString(this.idNumber);
        dest.writeString(this.idFront);
        dest.writeString(this.idBack);
        dest.writeSerializable(this.totalAsset);
        dest.writeSerializable(this.balance);
        dest.writeString(this.sessionToken);
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.userCode = in.readString();
        this.userName = in.readString();
        this.userNick = in.readString();
        this.userStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.avatar = in.readString();
        this.followers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.followOthers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.phone = in.readString();
        this.email = in.readString();
        this.sex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.password = in.readString();
        this.profile = in.readString();
        this.location = in.readString();
        this.idNumber = in.readString();
        this.idFront = in.readString();
        this.idBack = in.readString();
        this.totalAsset = (BigDecimal) in.readSerializable();
        this.balance = (BigDecimal) in.readSerializable();
        this.sessionToken = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
