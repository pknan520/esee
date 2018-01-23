package com.nong.nongo2o.module.common.buy.viewModel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.support.annotation.DrawableRes;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.common.buy.fragment.PayResultFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AppManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    /**
     * 发表动态
     */
    public void createMoment(Goods goods) {
        ((RxBaseActivity) fragment.getActivity()).showLoading();
        Moment moment = new Moment();

        moment.setHeaderImg(goods.getCovers());
        moment.setTitle("我发现了一个好东西，一起来看看吧");
        moment.setContent(goods.getDetail());
        moment.setGoodsCode(goods.getGoodsCode());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(moment));
        Intent intent = new Intent();

        RetrofitHelper.getDynamicAPI()
                .postMoment(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    intent.setAction("refreshDynamicList");
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                }, throwable -> {
                    ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }, () -> ((RxBaseActivity) fragment.getActivity()).dismissLoading());
    }

    public final ReplyCommand backToHomeClick = new ReplyCommand(() -> {
        AppManager.getAppManager().backToHome();
    });
}
