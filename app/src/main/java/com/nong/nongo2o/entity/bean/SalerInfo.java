package com.nong.nongo2o.entity.bean;

import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.User;
import com.nong.nongo2o.uils.BeanUtils;

import java.util.List;

public class SalerInfo extends SimpleUser {

    private Integer goodsNumber;

    private Integer followNumber;

    private Integer momentNumber;

    private List<Goods> goods;

    public SalerInfo() { }

    public SalerInfo(User user) {
        BeanUtils.Copy(this, user, true);
    }

    public SalerInfo(SimpleUser user) {
        BeanUtils.Copy(this, user, true);
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goodss) {
        this.goods = goodss;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public Integer getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(Integer followNumber) {
        this.followNumber = followNumber;
    }

    public Integer getMomentNumber() {
        return momentNumber;
    }

    public void setMomentNumber(Integer momentNumber) {
        this.momentNumber = momentNumber;
    }
}

