package com.nong.nongo2o.module.common.buy.viewModel;

import android.databinding.ObservableField;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.bean.WechatPayInfo;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.request.CreatePaymentRequest;
import com.nong.nongo2o.module.common.buy.fragment.PayFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
        ((RxBaseActivity) fragment.getActivity()).showLoading();
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setUserCode(order.getUserCode());
        req.setOrderCode(order.getOrderCode());
        req.setPaymentStatus(order.getOrderStatus());
        req.setPaymentType(order.getPayType());
        req.setPaymentPrice(order.getTotalPrice());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(req));

        RetrofitHelper.getPaymentAPI()
                .payWithWechat(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(wechatPayInfo -> {
                    ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                    payWithWechat(wechatPayInfo);
                }, throwable -> {
                    ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void payWithWechat(WechatPayInfo info) {
        PayReq req = new PayReq();
        req.appId = info.getAppId();
        req.partnerId = info.getPartnerId();
        req.prepayId = info.getPrepayId();
        req.packageValue = info.getPackages();
        req.nonceStr = info.getNonceStr();
        req.timeStamp = info.getTimeStamp();
        req.sign = info.getSign();

        AdventurerApp.wxApi.sendReq(req);
    }

    public void cancelPay() {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setOrderCode(order.getOrderCode());
        req.setUserCode(order.getUserCode());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(req));

        RetrofitHelper.getOrderAPI()
                .cancelPey(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {}, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
