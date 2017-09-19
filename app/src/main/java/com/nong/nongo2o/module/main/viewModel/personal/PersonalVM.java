package com.nong.nongo2o.module.main.viewModel.personal;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.main.fragment.personal.PersonalFragment;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.activity.BillActivity;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.GoodsManagerActivity;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.activity.SettingActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.BeanUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-6-22.
 */

public class PersonalVM implements ViewModel {

    private PersonalFragment fragment;
    private Gson gson;
    private String userCode="";

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_wode_98;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<BigDecimal> balance = new ObservableField<>();
    public final ObservableField<Integer> unpaidBadge = new ObservableField<>();
    public final ObservableField<Integer> deliveryBadge = new ObservableField<>();
    public final ObservableField<Integer> takeoverBadge = new ObservableField<>();
    public final ObservableField<Integer> evaBadge = new ObservableField<>();
//    public final ObservableField<String> authenticationStatus = new ObservableField<>();

    public PersonalVM(PersonalFragment fragment) {
        this.fragment = fragment;
        gson = new Gson();
        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean canAuthentication = new ObservableBoolean(true);
        public final ObservableBoolean hasAuthentication = new ObservableBoolean(false);
        public final ObservableBoolean isMerchantMode = new ObservableBoolean(false);
        public final ObservableBoolean isSaler = new ObservableBoolean(false);
    }

    private void initData() {
        RetrofitHelper.getUserAPI().userProfile()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    headUri.set(resp.getAvatar());
                    name.set(resp.getUserNick());
                    balance.set(resp.getBalance());
                    viewStyle.isSaler.set( resp.getUserType() == 1);
                    userCode = resp.getUserCode();
                    getOrderCount(userCode);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }, () -> {
                });

        // TODO: 2017-9-18 合并代码出现问题
//        unpaidBadge.set("12");
//        deliveryBadge.set("3");
//        takeoverBadge.set("25");
//        evaBadge.set("99+");

//        authenticationStatus.set(R.string.saler_authentication);
    }

    private void getOrderCount(String userCode){
        reSet();
        String type = "";
        Map<String,String> paramMap = new HashMap<>();
        if(viewStyle.isMerchantMode.get()){
            type = "saler_order_status";
            paramMap.put("salerCode",userCode);
        }else{
            type = "buyer_order_status";
            paramMap.put("buyerCode",userCode);
        }
        RetrofitHelper.getUserAPI().userDbWrapper(type,gson.toJson(paramMap))
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    for(Map<String,Object> countMap : resp){
                        switch ((int)Double.parseDouble(countMap.get("order_status").toString()) ){
                            case 0:
                                unpaidBadge.set((int)Double.parseDouble(countMap.get("count").toString()));
                                break;
                            case 1:
                                deliveryBadge.set((int)Double.parseDouble(countMap.get("count").toString()));
                                break;
                            case 2:
                                takeoverBadge.set((int)Double.parseDouble(countMap.get("count").toString()));
                                break;
                            case 3:
                                evaBadge.set((int)Double.parseDouble(countMap.get("count").toString()));
                                break;
                        }
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }, () -> {});
    }
    /**
     * 切换模式
     */
    public final ReplyCommand switchModeClick = new ReplyCommand(() -> {
        viewStyle.isMerchantMode.set(!viewStyle.isMerchantMode.get());
        fragment.startWaveAnim();
        getOrderCount(userCode);
    });

    private void reSet(){
        unpaidBadge.set(0);
        deliveryBadge.set(0);
        takeoverBadge.set(0);
        evaBadge.set(0);
    }

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
    public final ReplyCommand toOrderCenterClick = new ReplyCommand(() -> startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity(),viewStyle.isMerchantMode.get())));

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
     * 去实名认证
     */
    public final ReplyCommand toIdentifyClick = new ReplyCommand(() -> startActivityClick(IdentifyActivity.newIntent(fragment.getActivity())));

    private void startActivityClick(Intent intent) {
        fragment.getActivity().startActivity(intent);
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    }
}
