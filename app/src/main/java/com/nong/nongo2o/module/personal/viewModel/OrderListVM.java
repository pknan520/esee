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
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.CreatePaymentRequest;
import com.nong.nongo2o.entity.request.UpdateOrderRequest;
import com.nong.nongo2o.module.common.ConfirmDialogListener;
import com.nong.nongo2o.module.common.buy.activity.BuyActivity;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderEvaluateFragment;
import com.nong.nongo2o.module.personal.fragment.OrderListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.BeanUtils;

import java.math.BigDecimal;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListVM implements ViewModel {

    private OrderListFragment fragment;
    public String status;
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

    public OrderListVM(OrderListFragment fragment, String status, boolean isMerchantMode) {
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
                .userOrderSearch(status.equals("-99") ? null : status, isMerchantMode ? 1 : 0, page, pageSize)
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
        private UpdateOrderRequest request;
        //  买家/商家信息
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
        public final ObservableField<String> btnL = new ObservableField<>();
        public final ObservableField<String> btnR = new ObservableField<>();

        public ItemOrderVM(Order order) {
            this.order = order;
            initData();
        }

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        }

        /**
         * 初始化数据
         */
        private void initData() {
            if (order != null) {
                request = new UpdateOrderRequest();
                BeanUtils.Copy(request, order, false);

                if (isMerchantMode) {
                    //  商家看买家信息
                    if (order.getUser() != null) {
                        headUri.set(order.getUser().getAvatar());
                        name.set(order.getUser().getUserNick());
                        summary.set(order.getUser().getProfile());
                    }
                } else {
                    //  买家看商家信息
                    if (order.getSaleUser() != null) {
                        headUri.set(order.getSaleUser().getAvatar());
                        name.set(order.getSaleUser().getUserNick());
                        summary.set(order.getSaleUser().getProfile());
                    }
                }
                switch (order.getOrderStatus()) {
                    case -1:
                        status.set("已取消");
                        break;
                    case 0:
                        status.set("待支付");
                        btnL.set("取消订单");
                        if (!isMerchantMode)
                            btnR.set("付款");
                        break;
                    case 1:
                        status.set("待发货");
                        if (isMerchantMode) {
                            btnL.set("退款");
                            btnR.set("确认发货");
                        } else {
                            btnL.set("取消订单");
                        }
                        break;
                    case 2:
                        status.set("待收货");
                        if (!isMerchantMode)
                            btnR.set("确认收货");
                        break;
                    case 3:
                        status.set("待评价");
                        if (!isMerchantMode) {
                            if (order.getPreStatus() == 5 || order.getPreStatus() == 6 || order.getPreStatus() == 7) {
                                btnL.set("");
                            } else {
                                btnL.set("售后退款");
                            }
                            btnR.set("评价订单");
                        }
                        break;
                    case 4:
                        status.set("已完成");
                        break;
                    case 5:
                        status.set("退款申请");
                        if (isMerchantMode) {
//                            btnL.set("取消");
                            btnR.set("退款处理");
                        }
                        break;
                    case 6:
                        status.set("退款中");
                        break;
                    case 7:
                        status.set("已退款");
                        break;
                    case 8:
                        if (isMerchantMode) {
                            status.set("可提现");
                        } else {
                            status.set("已完成");
                        }
                        break;
                    case 9:
                        if (isMerchantMode) {
                            status.set("提现中");
                        } else {
                            status.set("已完成");
                        }
                        break;
                    case 10:
                        if (isMerchantMode) {
                            status.set("已提现");
                        } else {
                            status.set("已完成");
                        }
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
         * 查看个人主页
         */
        public final ReplyCommand personalHomeClick = new ReplyCommand(new Action() {
            @Override
            public void run() throws Exception {
                fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), isMerchantMode ? order.getUser() : order.getSaleUser()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            }
        });

        /**
         * 左边按钮
         */
        public final ReplyCommand lClick = new ReplyCommand(() -> {
            switch (order.getOrderStatus()) {
                case 0:
                    //  both：取消订单
                    fragment.showConfirmDialog("确认取消订单？", () -> {
                        changeOrderStatus(-1);
                    });
                    break;
                case 1:
                    //  买家：申请退款；商家：直接退款
                    if (isMerchantMode)
                        fragment.showConfirmDialog("确认退款至买家？", () -> {
                            refund("商家退款", order.getTotalPrice());
                        });
                    else
//                        fragment.showRefundDialog(order, false, false, (reason, money) -> {
//                            applyRefund(reason);
//                        }
                        fragment.showConfirmDialog("确认取消订单并退款？", () -> {
                            refund("未发货退款", order.getTotalPrice());
                        });
                    break;
                case 3:
                    //  买家：售后退款
                    fragment.showRefundDialog(order, isMerchantMode, false, order.getApplyReason(), (reason, money) -> {
                        applyRefund(reason);
                    });
                    break;
                case 5:
                    //  商家：驳回退款
                    fragment.showRefundDialog(order, isMerchantMode, false, order.getApplyReason(), (reason, money) -> {
                        disagreeRefund(reason);
                    });
                    break;
            }
        });

        /**
         * 右边按钮
         */
        public final ReplyCommand rClick = new ReplyCommand(() -> {
            switch (order.getOrderStatus()) {
                case 0:
                    //  买家：付款
                    fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity(), null, order, true));
                    fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                    break;
                case 1:
                    //  商家：确认发货
                    ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                            OrderDetailFragment.newInstance(order, isMerchantMode), OrderDetailFragment.TAG);
                    break;
                case 2:
                    //  买家：确认收货
                    fragment.showConfirmDialog("确认收货后将不能取消，请确定是否确认收货？", () -> {
                        changeOrderStatus(3);
                    });
                    break;
                case 3:
                    //  买家：评价订单
                    ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                            OrderEvaluateFragment.newInstance(order), OrderEvaluateFragment.TAG);
                    break;
                case 5:
                    //  商家：同意退款
                    fragment.showRefundDialog(order, isMerchantMode, true, order.getApplyReason(), this::refund);
                    break;
            }
        });

        /**
         * 申请退款
         */
        private void applyRefund(String reason) {
            request.setApplyReason(reason);
            changeOrderStatus(5);
        }

        /**
         * 驳回退款
         */
        private void disagreeRefund(String reason) {
            request.setAuditInfo(reason);
            changeOrderStatus(order.getPreStatus());
        }

        /**
         * 退款
         */
        private void refund(String reason, BigDecimal money) {
            CreatePaymentRequest req = new CreatePaymentRequest();
            req.setOrderCode(order.getOrderCode());
            req.setPaymentPrice(money);
            req.setRemark(reason);

            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(req));

            RetrofitHelper.getOrderAPI()
                    .refund(requestBody)
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

        /**
         * 商家发货
         */
        private void send() {
            fragment.showExDialog((type, exName, exNumber, remark) -> {
                request.setPickSelf(type);
                request.setExName(exName);
                request.setExNumber(exNumber);
                request.setRemark(remark);

                changeOrderStatus(2);
            });
        }

        /**
         * 改变订单状态
         */
        private void changeOrderStatus(int newStatus) {
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
