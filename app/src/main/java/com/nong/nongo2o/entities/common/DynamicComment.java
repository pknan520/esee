package com.nong.nongo2o.entities.common;

import com.nong.nongo2o.entities.response.User;

/**
 * Created by Administrator on 2017-8-26.
 */

public class DynamicComment {

    private String id;
    private String momentCode;
    private String userCode;
    private String userName;
    private String toUserCode;
    private String toUserName;
    private String content;
    private User user;
    private User toUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToUserCode() {
        return toUserCode;
    }

    public void setToUserCode(String toUserCode) {
        this.toUserCode = toUserCode;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }
}
