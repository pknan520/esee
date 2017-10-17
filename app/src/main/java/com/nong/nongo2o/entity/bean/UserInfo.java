package com.nong.nongo2o.entity.bean;

import com.nong.nongo2o.entity.base.Model;
import com.nong.nongo2o.entity.domain.User;
import com.nong.nongo2o.uils.BeanUtils;

/**
 * Created by Administrator on 2017-9-12.
 */
public class UserInfo extends Model {
    private static UserInfo ourInstance = new UserInfo();

    public static UserInfo getInstance() {
        return ourInstance;
    }

    private UserInfo() {
    }

    private String id;

    /**
     * 用户编码
     */
    private String userCode;
    private String openId;
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
     * 个人简介
     */
    private String profile;
    /**
     * 所属地区
     */
    private String location;
    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请者【userCode】
     */
    private String inviter;

    private String sessionToken;

    public UserInfo(UserInfo user) {
        BeanUtils.Copy(this, user, true);
    }

    public static UserInfo getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(UserInfo ourInstance) {
        UserInfo.ourInstance = ourInstance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public void clearUser() {
        setOurInstance(new UserInfo());
    }
}
