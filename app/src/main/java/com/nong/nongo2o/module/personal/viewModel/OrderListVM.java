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
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.Follow;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.UpdateOrderRequest;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.login.fragment.LoginFragment;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderEvaluateFragment;
import com.nong.nongo2o.module.personal.fragment.OrderListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListVM implements ViewModel {

    private OrderListFragment fragment;
    public int status;
    public boolean isMerchantMode;
    //  订单列表
    public final ObservableList<ItemOrderVM> itemOrderVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemOrderVM> itemOrderBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_list);

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    @DrawableRes
    public final int notLoginImg = R.mipmap.default_error;

    private int pageSize = 10;
    private int total = 0;

    public OrderListVM(OrderListFragment fragment, int status, boolean isMerchantMode) {
        this.fragment = fragment;
        this.status = status;
        this.isMerchantMode = isMerchantMode;
        initData();
    }


    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        public final ObservableBoolean notLogin = new ObservableBoolean(false);
    }

    /**
     * 初始数据
     */
    public void initData() {
        viewStyle.notLogin.set(TextUtils.isEmpty(UserInfo.getInstance().getSessionToken()));
        if (!viewStyle.notLogin.get()) searchDate(1, true);
    }

    private void searchDate(int page, boolean force) {
        viewStyle.isRefreshing.set(true);
        RetrofitHelper.getOrderAPI()
                .userOrderSearch(-99 == status ? null : status, isMerchantMode ? 1 : 0, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) {
                        itemOrderVMs.clear();
                    }
                    total = resp.getTotal();
                    for (Order order : resp.getRows()) {
                        itemOrderVMs.add(new ItemOrderVM(order));
                    }
                    viewStyle.isEmpty.set(itemOrderVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        if (!viewStyle.notLogin.get()) searchDate(1, true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemOrderVMs.size() < total) {
            searchDate(itemOrderVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转登录
     */
    public final ReplyCommand toLogin = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), true));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public class ItemOrderVM implements ViewModel {

        private Order order;
        //  商家信息
        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_default_60;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();
        public final ObservableField<String> status = new ObservableField<>();
        //  商品列表
        public final ObservableList<ItemOrderGoodsListVM> itemOrderGoodsListVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemOrderGoodsListVM> itemOrderGoodsListBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_goods_list);
        //  订单信息
        public final ObservableField<String> orderInfo = new ObservableField<>();
        //  操作按钮
        public final ObservableField<String> btnStr = new ObservableField<>();

        public ItemOrderVM(Order order) {
            this.order = order;
            initData();
        }

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean btnVisi = new ObservableBoolean(false);
            public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        }

        /**
         * 初始化数据
         */
        private void initData() {
            if (order != null) {
                if (order.getSaleUser() != null) {
                    headUri.set(order.getSaleUser().getAvatar());
                    name.set(order.getSaleUser().getUserNick());
                    summary.set(order.getSaleUser().getProfile());
                }
                switch (order.getOrderStatus()) {
                    case -1:
                        status.set("已取消");
                        viewStyle.btnVisi.set(false);
                        break;
                    case 0:
                        status.set("待支付");  //  支付（用户）
                        viewStyle.btnVisi.set(!isMerchantMode);
                        btnStr.set("支付");
                        break;
                    case 1:
                        status.set("待发货");  //  发货（商家）
                        viewStyle.btnVisi.set(isMerchantMode);
                        btnStr.set("发货");
                        break;
                    case 2:
                        status.set("待收货");  //  收货（用户）
                        viewStyle.btnVisi.set(!isMerchantMode);
                        btnStr.set("收货");
                        break;
                    case 3:
                        status.set("待评价");  //  评价（用户）
                        viewStyle.btnVisi.set(!isMerchantMode);
                        btnStr.set("评价");
                        break;
                    case 4:
                        status.set("已完成");
                        viewStyle.btnVisi.set(false);
                        break;
                }

                int goodNum = 0;
                for (OrderDetail orderDetail : order.getOrderDetails()) {
                    goodNum += orderDetail.getGoodsNum();

                    itemOrderGoodsListVMs.add(new ItemOrderGoodsListVM(orderDetail, order, isMerchantMode, ItemOrderGoodsListVM.FROM_ORDER_LIST, fragment));
                }

                orderInfo.set("共" + goodNum + "件，合计¥ " + order.getTotalPrice());
            }
        }

        /**
         * 查看订单详情
         */
        public final ReplyCommand orderDetailClick = new ReplyCommand(() -> {
            ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                    OrderDetailFragment.newInstance(order, isMerchantMode), OrderDetailFragment.TAG);
        });

        /**
         * 查看商家主页
         */
        public final ReplyCommand personalHomeClick = new ReplyCommand(new Action() {
            @Override
            public void run() throws Exception {
                fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), order.getSaleUser()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            }
        });

        /**
         * 订单操作
         */
        public final ReplyCommand operateClick = new ReplyCommand(() -> {
            switch (order.getOrderStatus()) {
                case 0:
                    //  支付（用户）
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
                    ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                            OrderEvaluateFragment.newInstance(order), OrderEvaluateFragment.TAG);
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
}
