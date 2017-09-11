package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.module.common.fragment.CreateOrderFragment;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;

import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-17.
 */

public class CreateOrderVM implements ViewModel {

    private CreateOrderFragment fragment;
    //  收货信息
    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> receivePhone = new ObservableField<>();
    public final ObservableField<String> receiveAddr = new ObservableField<>();
    //  商家信息
    @DrawableRes
    public final int merchantHeadPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> merchantHeadUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    //  商品列表
    public final ObservableList<ItemOrderGoodsListVM> itemGoodsVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemOrderGoodsListVM> itemGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_goods_list);
    //  运费
    public final ObservableField<Double> transFee = new ObservableField<>();
    //  订单总计
    public final ObservableField<String> orderInfo = new ObservableField<>();
    public final ObservableField<String> moneyInfo = new ObservableField<>();

    public CreateOrderVM(CreateOrderFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean hasDefaultAddr = new ObservableBoolean(false);
        public final ObservableBoolean isComplete = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        viewStyle.hasDefaultAddr.set(true);
        receiver.set("张先生");
        receivePhone.set("18192317210");
        receiveAddr.set("广东省 广州市 天河区 员村二横路天鹅花苑7栋1204");

        name.set("果酱妈咪09");
        summary.set("这家伙很懒，什么都没留下~");

        for (int i = 0; i < 2; i++) {
            itemGoodsVMs.add(new ItemOrderGoodsListVM(ItemOrderGoodsListVM.FROM_ORDER_DETAIL, fragment));
        }

        transFee.set(10.00);

        orderInfo.set("共n件，合计¥107.60（含运费¥10.00）");
        moneyInfo.set("应收：¥107.60");
    }

    /**
     * 选择地址
     */
    public final ReplyCommand selectAddrClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(AddressMgrActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 查看商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });


}
