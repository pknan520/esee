package com.nong.nongo2o.entity.bean;


import com.nong.nongo2o.entity.base.PagingEntity;
import com.nong.nongo2o.entity.domain.User;
import com.nong.nongo2o.uils.BeanUtils;

public class SimpleUser extends PagingEntity {

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
     * 性别： 0-男 1-女
     */
    private Integer sex;

    /**
     * 所属地区
     */
    private String location;

    /**
     * 个人简介
     */
    private String profile;

    public SimpleUser() { }

    public SimpleUser(User user) {
        BeanUtils.Copy(this, user, true);
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowOthers() {
        return followOthers;
    }

    public void setFollowOthers(Integer followOthers) {
        this.followOthers = followOthers;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
