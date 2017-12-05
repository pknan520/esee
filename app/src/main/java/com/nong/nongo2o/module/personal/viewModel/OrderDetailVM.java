package com.nong.nongo2o.module.personal.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.UpdateOrderRequest;
import com.nong.nongo2o.module.common.buy.activity.BuyActivity;
import com.nong.nongo2o.module.common.buy.fragment.PayFragment;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.common.viewModel.ItemTransListVM;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderEvaluateFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AddressUtils;
import com.nong.nongo2o.uils.imUtils.IMUtils;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderDetailVM implements ViewModel {

    private OrderDetailFragment fragment;
    private Order order;
    private boolean isMerchantMode;

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
    public final ObservableField<BigDecimal> transFee = new ObservableField<>();
    // 订单费用
    public final ObservableField<String> orderInfo = new ObservableField<>();
    public final ObservableField<String> moneyInfo = new ObservableField<>();
    //  物流信息
    public final ObservableField<String> exName = new ObservableField<>();
    public final ObservableField<String> exNumber = new ObservableField<>();
    //  物流轨迹
//    public final ObservableList<ItemTransListVM> itemTransListVMs = new ObservableArrayList<>();
//    public final ItemBinding<ItemTransListVM> itemTransBinding = ItemBinding.of(BR.viewModel, R.layout.item_trans_list);
    //  按钮
    public final ObservableField<String> btnStr = new ObservableField<>();

    public OrderDetailVM(OrderDetailFragment fragment, Order order, boolean isMerchantMode) {
        this.fragment = fragment;
        this.order = order;
        this.isMerchantMode = isMerchantMode;

        iniData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean btnVisi = new ObservableBoolean(false);
        public final ObservableBoolean showLogisticsInfo = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void iniData() {
        if (order != null) {
            itemGoodsVMs.clear();
            orderNo.set(order.getOrderCode());
            switch (order.getOrderStatus()) {
                case -1:
                    orderStatus.set("已取消");
                    viewStyle.btnVisi.set(false);
                    break;
                case 0:
                    orderStatus.set("待支付");  //  支付（用户）
                    viewStyle.btnVisi.set(!isMerchantMode);
                    btnStr.set("支付");
                    break;
                case 1:
                    orderStatus.set("待发货");  //  发货（商家）
                    viewStyle.btnVisi.set(isMerchantMode);
                    btnStr.set("发货");
                    break;
                case 2:
                    orderStatus.set("待收货");  //  收货（用户）
                    viewStyle.btnVisi.set(!isMerchantMode);
                    btnStr.set("收货");
                    break;
                case 3:
                    orderStatus.set("待评价");  //  评价（用户）
                    viewStyle.btnVisi.set(!isMerchantMode);
                    btnStr.set("评价");
                    break;
                case 4:
                    orderStatus.set("已完成");
                    viewStyle.btnVisi.set(false);
                    break;
            }

            if (order.getOrderStatus() > 1) {
                viewStyle.showLogisticsInfo.set(true);
                exName.set(order.getExName());
                exNumber.set(order.getExNumber());
            }

            receiver.set(order.getConsigneeName());
            receivePhone.set(order.getConsigneeMobile());
            if (!TextUtils.isEmpty(order.getConsigneeProvince()) && !TextUtils.isEmpty(order.getConsigneeCity())
                    && !TextUtils.isEmpty(order.getConsigneeArea())) {
                List<City> cityList = AddressUtils.getCities(new String[]{order.getConsigneeProvince(), order.getConsigneeCity(), order.getConsigneeArea()});
                receiveAddr.set(AddressUtils.getCityName(cityList) + order.getConsigneeAddress());
            } else {
                receiveAddr.set(order.getConsigneeAddress());
            }

            if (order.getSaleUser() != null) {
                merchantHeadUri.set(order.getSaleUser().getAvatar());
                name.set(order.getSaleUser().getUserNick());
                summary.set(order.getSaleUser().getProfile());
            }

            BigDecimal total = new BigDecimal(0);
            BigDecimal freight = new BigDecimal(0);
            int goodsNum = 0;
            if (order.getOrderDetails() != null) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    itemGoodsVMs.add(new ItemOrderGoodsListVM(detail, order, isMerchantMode, ItemOrderGoodsListVM.FROM_ORDER_DETAIL, fragment));
                    freight = freight.add(detail.getGoods().getFreight());
                    total = total.add(detail.getGoodsSpec().getPrice().multiply(new BigDecimal(detail.getGoodsNum())));
                    goodsNum += detail.getGoodsNum();
                }
            }
            transFee.set(freight);
            orderInfo.set("共" + goodsNum + "件，合计¥" + total.add(freight) + "（含运费¥" + freight + "）");
            moneyInfo.set("应收：¥" + total.add(freight));

        }
    }

    /**
     * 跳转地址管理
     */
    public final ReplyCommand toAddrMgrClick = new ReplyCommand(() -> {
//        fragment.getActivity().startActivity(AddressMgrActivity.newIntent(fragment.getActivity()));
//        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 跳转商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), order.getSaleUser()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 联系商家
     */
    public final ReplyCommand contactClick = new ReplyCommand(() -> {
        IMUtils.checkIMLogin(isSuccess -> {
            if (isSuccess) {
                String userName = order.getSaleUser().getId();
                if (userName.equals(EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(fragment.getActivity(), "您不能自言自语了啦^.^", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
                intent.putExtra("userId", userName);
                fragment.getActivity().startActivity(intent);
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            } else {
                Toast.makeText(fragment.getActivity(), "聊天可能有点问题，请稍候再试", Toast.LENGTH_SHORT).show();
            }
        });
    });

    /**
     * 操作按钮点击事件
     */
    public final ReplyCommand operateClick = new ReplyCommand(() -> {
        switch (order.getOrderStatus()) {
            case 0:
                //  支付（用户）
                fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity(), null, order, true));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                break;
            case 1:
                //  发货（商家）
                send();
                break;
            case 2:
                //  收货（用户）
                receive();
                break;
            case 3:
                //  评价（用户）
                ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment, OrderEvaluateFragment.newInstance(order), OrderEvaluateFragment.TAG);
                break;
        }
    });

    /**
     * 商家发货
     */
    private void send() {
        fragment.showExDialog((exName, exNumber, remark) -> {
            UpdateOrderRequest request = new UpdateOrderRequest();
            request.setExName(exName);
            request.setExNumber(exNumber);
            request.setRemark(remark);

            changeOrderStatus(request, 2);
        });
    }

    /**
     * 用户确认收货
     */
    private void receive() {
        fragment.showReceiveDialog(() -> {
            changeOrderStatus(new UpdateOrderRequest(), 3);
        });
    }

    /**
     * 改变订单状态
     */
    private void changeOrderStatus(UpdateOrderRequest request, int newStatus) {
        if (request != null) {
            request.setOrderCode(order.getOrderCode());
            request.setUserCode(order.getUserCode());
            request.setOrderStatus(newStatus);

            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(request));

            RetrofitHelper.getOrderAPI()
                    .updateOrder(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        fragment.getFragmentManager().popBackStack();
                        //  通知刷新订单列表
                        Intent intent = new Intent("updateOrderList");
                        LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                    }, throwable -> {
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
