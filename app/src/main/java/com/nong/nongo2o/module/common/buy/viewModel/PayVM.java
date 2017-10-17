package com.nong.nongo2o.module.common.buy.viewModel;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.common.buy.fragment.PayFragment;

import java.math.BigDecimal;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-9-19.
 */

public class PayVM implements ViewModel {

    private PayFragment fragment;
    private Order order;

    public final ObservableField<BigDecimal> totalPrice = new ObservableField<>();

    public PayVM(PayFragment fragment, Order order) {
        this.fragment = fragment;
        this.order = order;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        totalPrice.set(order.getTotalPrice());
    }

    /**
     * 支付按钮
     */
    public final ReplyCommand payClick = new ReplyCommand(this::pay);

    private void pay() {

    }
}
