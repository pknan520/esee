package com.nong.nongo2o.entity.request;

import java.util.List;

/**
 * Created by PANYJ7 on 2018-3-6.
 */

public class WithdrawReq {

    private List<String> orderIds;

    public WithdrawReq(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }
}
