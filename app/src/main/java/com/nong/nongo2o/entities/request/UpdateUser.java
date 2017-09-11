package com.nong.nongo2o.entities.request;

/**
 * Created by Administrator on 2017-8-19.
 */

public class UpdateUser {

    private String id;
    private String userCode;
    private String profile;

    public UpdateUser() {
    }

    public UpdateUser(String id, String userCode, String profile) {
        this.id = id;
        this.userCode = userCode;
        this.profile = profile;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
