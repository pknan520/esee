package com.nong.nongo2o.entity.request;

import java.math.BigDecimal;

/**
 * Created by PANYJ7 on 2018-3-6.
 */

public class WithdrawReq {

    private BigDecimal withdrawMoney;

    public WithdrawReq(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public BigDecimal getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(BigDecimal withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }
}
