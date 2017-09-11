package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.common.viewModel.ItemTransListVM;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;

import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderDetailVM implements ViewModel {

    private OrderDetailFragment fragment;

    //  订单状态
    public final ObservableField<String> orderNo = new ObservableField<>();
    public final ObservableField<String> orderStatus = new ObservableField<>();
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
    // 订单费用
    public final ObservableField<String> orderInfo = new ObservableField<>();
    public final ObservableField<String> moneyInfo = new ObservableField<>();
    //  物流信息
    public final ObservableField<String> transOrderNo = new ObservableField<>();
    public final ObservableField<String> transStatus = new ObservableField<>();
    public final ObservableField<String> transCreateDate = new ObservableField<>();
    //  物流轨迹
    public final ObservableList<ItemTransListVM> itemTransListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemTransListVM> itemTransBinding = ItemBinding.of(BR.viewModel, R.layout.item_trans_list);

    public OrderDetailVM(OrderDetailFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        orderNo.set("T12000482");
        orderStatus.set("待发货");

        receiver.set("李先生");
        receivePhone.set("18023721820");
        receiveAddr.set("广东省佛山市顺德区北滘美的全球创新中心");


        name.set("果酱妈咪09");
        summary.set("这家伙很懒，什么都没留下~");

        for (int i = 0; i < 2; i++) {
            itemGoodsVMs.add(new ItemOrderGoodsListVM(ItemOrderGoodsListVM.FROM_ORDER_DETAIL, fragment));
        }

        transFee.set(10.00);

        orderInfo.set("共n件，合计¥107.60（含运费¥10.00）");
        moneyInfo.set("应收：¥107.60");

        transOrderNo.set("AD17060113304373");
        transStatus.set("已签收");
        transCreateDate.set("2017-06-01 14:10:04");

        itemTransListVMs.add(new ItemTransListVM(true));
        for (int i = 0; i < 4; i++) {
            itemTransListVMs.add(new ItemTransListVM(false));
        }
    }

    /**
     * 跳转地址管理
     */
    public final ReplyCommand toAddrMgrClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(AddressMgrActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 跳转商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 操作按钮点击事件
     */
    public final ReplyCommand operateClick = new ReplyCommand(() -> {
        Toast.makeText(fragment.getActivity(), "你点击了操作按钮", Toast.LENGTH_SHORT).show();
    });
}
