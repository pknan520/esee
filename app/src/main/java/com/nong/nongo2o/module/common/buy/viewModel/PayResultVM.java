package com.nong.nongo2o.module.common.buy.viewModel;

import android.databinding.ObservableBoolean;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.buy.fragment.PayResultFragment;
import com.nong.nongo2o.uils.AppManager;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-9-19.
 */

public class PayResultVM implements ViewModel {

    private PayResultFragment fragment;
    private boolean payResult;

    @DrawableRes
    public final int success = R.mipmap.pay_success;
    @DrawableRes
    public final int fail = R.mipmap.pay_fail;

    public PayResultVM(PayResultFragment fragment, boolean payResult) {
        this.fragment = fragment;
        this.payResult = payResult;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isSuccess = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        viewStyle.isSuccess.set(payResult);
    }

    public final ReplyCommand backToHomeClick = new ReplyCommand(() -> {
        AppManager.getAppManager().backToHome();
    });
}
