package com.nong.nongo2o.module.main.viewModel.personal;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.module.main.fragment.personal.PersonalFragment;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.activity.BillActivity;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.GoodsManagerActivity;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.activity.SettingActivity;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017-6-22.
 */

public class PersonalVM implements ViewModel {

    private PersonalFragment fragment;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_wode_98;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<BigDecimal> balance = new ObservableField<>();
    public final ObservableField<String> unpaidBadge = new ObservableField<>();
    public final ObservableField<String> deliveryBadge = new ObservableField<>();
    public final ObservableField<String> takeoverBadge = new ObservableField<>();
    public final ObservableField<String> evaBadge = new ObservableField<>();
    public final ObservableField<String> authenticationStatus = new ObservableField<>();

    public PersonalVM(PersonalFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean canAuthentication = new ObservableBoolean(true);
        public final ObservableBoolean hasAuthentication = new ObservableBoolean(false);
        public final ObservableBoolean isMerchantMode = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initData() {
        headUri.set(User.getInstance().getAvatar());
        name.set(User.getInstance().getUserNick());
        balance.set(User.getInstance().getBalance() == null ? BigDecimal.ZERO : User.getInstance().getBalance());

        unpaidBadge.set("12");
        deliveryBadge.set("3");
        takeoverBadge.set("25");
        evaBadge.set("99+");

        authenticationStatus.set("未认证");
    }

    /**
     * 切换模式
     */
    public final ReplyCommand switchModeClick = new ReplyCommand(() -> {
        viewStyle.isMerchantMode.set(!viewStyle.isMerchantMode.get());
        fragment.startWaveAnim();
    });


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
    public final ReplyCommand toPersonalHomeClick = new ReplyCommand(() -> startActivityClick(PersonalHomeActivity.newIntent(fragment.getActivity())));

    /**
     * 去订单管理
     */
    public final ReplyCommand toOrderCenterClick = new ReplyCommand(() -> startActivityClick(OrderCenterActivity.newIntent(fragment.getActivity())));

    /**
     * 去地址管理
     */
    public final ReplyCommand toAddressMgrClick = new ReplyCommand(() -> startActivityClick(AddressMgrActivity.newIntent(fragment.getActivity())));

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
