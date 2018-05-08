package com.nong.nongo2o.module.main.viewModel.personal;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.request.WithdrawReq;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.fragment.personal.PersonalFragment;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.activity.BillActivity;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.GoodsManagerActivity;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.nong.nongo2o.module.personal.activity.InviteActivity;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.activity.SettingActivity;
import com.nong.nongo2o.module.personal.activity.WithdrawActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.BeanUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017-6-22.
 */

public class PersonalVM implements ViewModel {

    private PersonalFragment fragment;
    private Gson gson;
    private String userCode = "";

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_wode_98;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<BigDecimal> balance = new ObservableField<>();
    public final ObservableField<BigDecimal> nowAsset = new ObservableField<>();
    public final ObservableField<Integer> unpaidBadge = new ObservableField<>();
    public final ObservableField<Integer> deliveryBadge = new ObservableField<>();
    public final ObservableField<Integer> takeoverBadge = new ObservableField<>();
    public final ObservableField<Integer> evaBadge = new ObservableField<>();
    public final ObservableField<Integer> refundBadge = new ObservableField<>();
//    public final ObservableField<String> authenticationStatus = new ObservableField<>();

    public PersonalVM(PersonalFragment fragment) {
        this.fragment = fragment;
        gson = new Gson();
        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isMerchantMode = new ObservableBoolean(false);
        public final ObservableBoolean isSaler = new ObservableBoolean(false);
        public final ObservableBoolean isChecking = new ObservableBoolean(false);

        public final ObservableBoolean notLogin = new ObservableBoolean(false);
    }

    public void initData() {
        viewStyle.notLogin.set(TextUtils.isEmpty(UserInfo.getInstance().getSessionToken()));

        if (!viewStyle.notLogin.get()) getPersonalInfo();
        else {
            name.set("请登录");
            balance.set(new BigDecimal(0.0));
            viewStyle.isSaler.set(false);
        }

//        authenticationStatus.set(R.string.saler_authentication);
    }

    public void getPersonalInfo() {
        RetrofitHelper.getUserAPI().userProfile()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    headUri.set(resp.getAvatar());
                    name.set(resp.getUserNick());
                    nowAsset.set(resp.getNowAsset() == null ? new BigDecimal(0.00) : resp.getNowAsset());
                    balance.set(resp.getBalance() == null ? new BigDecimal(0.00) : resp.getBalance());
                    viewStyle.isSaler.set(resp.getUserType() == 1);
                    viewStyle.isChecking.set(resp.getUserType() == 10);
                    userCode = resp.getUserCode();

                    UserInfo.getInstance().setUserNick(resp.getUserNick());
                    UserInfo.getInstance().setUserType(resp.getUserType());
                    getOrderCount(userCode);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void getOrderCount(String userCode) {
        reset();
        String type = "";
        Map<String, String> paramMap = new HashMap<>();
        if (viewStyle.isMerchantMode.get()) {
            type = "saler_order_status";
            paramMap.put("salerCode", userCode);
        } else {
            type = "buyer_order_status";
            paramMap.put("buyerCode", userCode);
        }
        Log.d("Personal", "param: " + gson.toJson(paramMap));
        try {
            RetrofitHelper.getUserAPI()
                    .userDbWrapper(type, URLEncoder.encode(gson.toJson(paramMap), "utf-8"))
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        for (Map<String, Object> countMap : resp) {
                            switch ((int) Double.parseDouble(countMap.get("order_status").toString())) {
                                case 0:
                                    unpaidBadge.set((int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 1:
                                    deliveryBadge.set((int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 2:
//                                    if (!viewStyle.isMerchantMode.get()) {
                                        takeoverBadge.set((int) Double.parseDouble(countMap.get("count").toString()));
//                                    }
                                    break;
                                case 3:
//                                    if (!viewStyle.isMerchantMode.get()) {
                                        evaBadge.set((int) Double.parseDouble(countMap.get("count").toString()));
//                                    }
                                    break;

                                case 5: //  退款申请
                                    refundBadge.set(refundBadge.get() + (int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 6: //  退款中
                                    refundBadge.set(refundBadge.get() + (int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                            }
                        }
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show())
            ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换模式
     */
    public final ReplyCommand switchModeClick = new ReplyCommand(() -> {
        viewStyle.isMerchantMode.set(!viewStyle.isMerchantMode.get());
        fragment.startWaveAnim();
        getOrderCount(userCode);
    });

    private void reset() {
        unpaidBadge.set(0);
        deliveryBadge.set(0);
        takeoverBadge.set(0);
        evaBadge.set(0);
        refundBadge.set(0);
    }

    /**
     * 去提现
     */
    public final ReplyCommand withdrawClick = new ReplyCommand(() -> startActivityClick(WithdrawActivity.newIntent(fragment.getActivity())));

    /**
     * 去账单
     */
    public final ReplyCommand toBillClick = new ReplyCommand(() -> startActivityClick(BillActivity.newIntent(fragment.getActivity())));

    /**
     * 去个人设置
     */
    public final ReplyCommand toSettingClick = new ReplyCommand(() -> startActivityClick(SettingActivity.newIntent(fragment.getActivity())));

    /**
     * 去个人主页
     */
    public final ReplyCommand toPersonalHomeClick = new ReplyCommand(() -> startActivityClick(PersonalHomeActivity.newIntent(fragment.getActivity(), (SimpleUser) BeanUtils.Copy(new SimpleUser(), UserInfo.getInstance(), true))));

    /**
     * 去订单管理
     */
    public final ReplyCommand toOrderCenterClick = new ReplyCommand(() -> startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(), viewStyle.isMerchantMode.get(), 0)));

    /**
     * 去待付款订单
     */
    public final ReplyCommand toUnPaidOrderClick = new ReplyCommand(() ->
            startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(), viewStyle.isMerchantMode.get(), 0))
    );

    /**
     * 去待发货订单
     */
    public final ReplyCommand toUnSendOrderClick = new ReplyCommand(() ->
            startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(), viewStyle.isMerchantMode.get(), viewStyle.isMerchantMode.get() ? 0 : 1))
    );

    /**
     * 去待收货订单
     */
    public final ReplyCommand toUnReceiveOrderClick = new ReplyCommand(() ->
            startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(), viewStyle.isMerchantMode.get(), viewStyle.isMerchantMode.get() ? 1 : 2))
    );

    /**
     * 去待评价订单
     */
    public final ReplyCommand toUnEvaOrderClick = new ReplyCommand(() ->
            startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(), viewStyle.isMerchantMode.get(), viewStyle.isMerchantMode.get() ? 2 : 3))
    );

    /**
     * 去退款中订单
     */
    public final ReplyCommand toRefundClick = new ReplyCommand(() ->
            startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(), viewStyle.isMerchantMode.get(), viewStyle.isMerchantMode.get() ? 3 : 4))
    );

    /**
     * 去地址管理
     */
    public final ReplyCommand toAddressMgrClick = new ReplyCommand(() -> startActivityClick(AddressMgrActivity.newIntent(fragment.getActivity(), AddressMgrActivity.ADDR_MGR)));

    /**
     * 去商品管理
     */
    public final ReplyCommand toGoodsManagerClick = new ReplyCommand(() -> startActivityClick(GoodsManagerActivity.newIntent(fragment.getActivity())));

    /**
     * 去粉丝管理
     */
    public final ReplyCommand toFansMgrClick = new ReplyCommand(() -> startActivityClick(FansMgrActivity.newIntent(fragment.getActivity())));

    /**
     * 去邀请码
     */
    public final ReplyCommand toInviteClick = new ReplyCommand(() -> startActivityClick(InviteActivity.newIntent(fragment.getActivity())));

    /**
     * 去实名认证
     */
    public final ReplyCommand toIdentifyClick = new ReplyCommand(() -> startActivityClick(IdentifyActivity.newIntent(fragment.getActivity())));

    private void startActivityClick(Intent intent) {
        if (!viewStyle.notLogin.get()) fragment.getActivity().startActivity(intent);
        else
            fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), true));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    }

    public class WithdrawDialogVM implements ViewModel {

        public final ObservableField<BigDecimal> limit = new ObservableField<>();
        public final ObservableField<String> money = new ObservableField<>();

        public WithdrawDialogVM() {
            initData();
        }

        private void initData() {
            limit.set(balance.get());
        }

        /**
         * 取消按键
         */
        public final ReplyCommand cancelClick = new ReplyCommand(() -> fragment.hideWithdrawDialog());

        /**
         * 确认按键
         */
        public final ReplyCommand confirmClick = new ReplyCommand(this::withdraw);

        private void withdraw() {
//            if (TextUtils.isEmpty(money.get())) {
//                Toast.makeText(fragment.getActivity(), "请输入提现金额", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            BigDecimal withdrawMoney = new BigDecimal(money.get());
//            if (withdrawMoney.compareTo(BigDecimal.ZERO) == 0
//                    || withdrawMoney.compareTo(BigDecimal.ZERO) == -1
//                    || withdrawMoney.compareTo(limit.get()) == 1) {
//                Toast.makeText(fragment.getActivity(), "请输入合法的提现金额", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            ((RxBaseActivity) fragment.getActivity()).showLoading();
//            WithdrawReq req = new WithdrawReq(withdrawMoney);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
//                    new Gson().toJson(req));
//            RetrofitHelper.getUserAPI()
//                    .withdraw(requestBody)
//                    .subscribeOn(Schedulers.io())
//                    .map(new ApiResponseFunc<>())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(resp -> {
//                        limit.set(limit.get().subtract(withdrawMoney));
//                        balance.set(balance.get().subtract(withdrawMoney));
//                        fragment.hideWithdrawDialog();
//                        Toast.makeText(fragment.getActivity(), "提现成功", Toast.LENGTH_SHORT).show();
//                    }, throwable -> {
//                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                        ((RxBaseActivity) fragment.getActivity()).dismissLoading();
//                    }, () -> ((RxBaseActivity) fragment.getActivity()).dismissLoading());
        }
    }
}
