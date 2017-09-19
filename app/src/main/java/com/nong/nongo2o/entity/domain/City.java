package com.nong.nongo2o.entity.domain;

import com.nong.nongo2o.entity.base.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017-9-19.
 */

@Entity(createInDb = false)
public class City extends Model{

    private String city_code;
    private String parent_code;
    private String city_name;
    @Generated(hash = 1682325044)
    public City(String city_code, String parent_code, String city_name) {
        this.city_code = city_code;
        this.parent_code = parent_code;
        this.city_name = city_name;
    }
    @Generated(hash = 750791287)
    public City() {
    }
    public String getCity_code() {
        return this.city_code;
    }
    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }
    public String getParent_code() {
        return this.parent_code;
    }
    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
    public String getCity_name() {
        return this.city_name;
    }
    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    
}
