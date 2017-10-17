package com.nong.nongo2o.entity.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2017-9-28.
 */

@Entity
public class EaseUserInfo {

    @Id
    private String userId;
    private String userNick;
    private String avatar;

    @Generated(hash = 47451471)
    public EaseUserInfo(String userId, String userNick, String avatar) {
        this.userId = userId;
        this.userNick = userNick;
        this.avatar = avatar;
    }
    @Generated(hash = 1625041234)
    public EaseUserInfo() {
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserNick() {
        return this.userNick;
    }
    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
