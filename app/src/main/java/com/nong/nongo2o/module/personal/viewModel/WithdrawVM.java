package com.nong.nongo2o.module.personal.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.request.WithdrawReq;
import com.nong.nongo2o.module.personal.fragment.WithdrawFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by PANYJ7 on 2018-3-20.
 */

public class WithdrawVM implements ViewModel {

    private WithdrawFragment fragment;
    private String status;

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    private int pageSize = 10;
    private int total = 0;
    public final ObservableList<ItemWithdrawOrderVM> itemVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemWithdrawOrderVM> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_withdraw_order);
    public final ObservableBoolean allCheck = new ObservableBoolean(false);

    public WithdrawVM(WithdrawFragment fragment) {
        this.fragment = fragment;

        status = fragment.getArguments().getString("status");

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        public final ObservableBoolean applyWithdraw = new ObservableBoolean(false);
    }

    /**
     * 全选按钮
     */
    public final ReplyCommand allCheckClick = new ReplyCommand(() -> {
        allCheck.set(!allCheck.get());
        for (ItemWithdrawOrderVM vm : itemVMs) {
            vm.isCheck.set(allCheck.get());
        }
    });

    /**
     * 提现按钮
     */
    public final ReplyCommand withdrawClick = new ReplyCommand(() -> {
        if (isAllUnChecked()) {
            Toast.makeText(fragment.getActivity(), "请选择需要提现的订单", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> orderIds = new ArrayList<>();
        for (ItemWithdrawOrderVM vm : itemVMs) {
            if (vm.isCheck.get()) orderIds.add(vm.order.getOrderCode());
        }
        WithdrawReq req = new WithdrawReq(orderIds);
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(req));
        RetrofitHelper.getUserAPI()
                .withdraw(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(new Intent("updateWithdrawList"));
                    Toast.makeText(fragment.getActivity(), "提现成功", Toast.LENGTH_SHORT).show();
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                }, () -> ((RxBaseActivity) fragment.getActivity()).dismissLoading());
    });

    /**
     * 初始化数据
     */
    public void initData() {
        viewStyle.applyWithdraw.set(status.equals("8"));
        getOrderList(true, 1);
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getOrderList(true, 1);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreData = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemVMs.size() < total) {
            getOrderList(false, itemVMs.size() / pageSize + 1);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载提现订单
     */
    private void getOrderList(boolean force, int page) {
        viewStyle.isRefreshing.set(true);
        RetrofitHelper.getOrderAPI()
                .userOrderSearch(status, 1, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) {
                        itemVMs.clear();
                    }
                    total = resp.getTotal();
                    for (Order order : resp.getRows()) {
                        itemVMs.add(new ItemWithdrawOrderVM(order, viewStyle.applyWithdraw.get()));
                    }
                    viewStyle.isEmpty.set(itemVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    public class ItemWithdrawOrderVM implements ViewModel {

        private Order order;

        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_36;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> orderNo = new ObservableField<>();
        public final ObservableField<String> status = new ObservableField<>();

        public final ObservableField<BigDecimal> orderMoney = new ObservableField<>();
        public final ObservableField<BigDecimal> refundMoney = new ObservableField<>();
        public final ObservableField<BigDecimal> withdrawMoney = new ObservableField<>();

        public final ObservableBoolean isCheck = new ObservableBoolean(false);

        public ItemWithdrawOrderVM(Order order, boolean applyWithdraw) {
            this.order = order;
            viewStyle.applyWithdraw.set(applyWithdraw);

            initData();
        }

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean hasRefund = new ObservableBoolean(false);
            public final ObservableBoolean applyWithdraw = new ObservableBoolean(false);
        }

        /**
         * 初始化数据
         */
        private void initData() {
            headUri.set(order.getUser().getAvatar());
            name.set(order.getUser().getUserNick());
            orderNo.set(order.getOrderCode());
            switch (order.getOrderStatus()) {
                case 9:
                    status.set("提现中");
                    break;
                case 10:
                    status.set("提现完成");
                    break;
            }

            orderMoney.set(order.getTotalPrice());
            viewStyle.hasRefund.set(order.getRefundAmount() != null);
            if (order.getRefundAmount() != null) {
                refundMoney.set(order.getRefundAmount());
                withdrawMoney.set(order.getTotalPrice().subtract(order.getRefundAmount()));
            } else {
                withdrawMoney.set(order.getTotalPrice());
            }
        }

        /**
         * 选择点击事件
         */
        public final ReplyCommand checkClick = new ReplyCommand(() -> {
            isCheck.set(!isCheck.get());
            if (!isCheck.get()) {
                allCheck.set(false);
            } else {
                if (isAllChecked()) {
                    allCheck.set(true);
                }
            }
        });
    }

    private boolean isAllChecked() {
        for (ItemWithdrawOrderVM vm : itemVMs) {
            if (!vm.isCheck.get()) return false;
        }
        return true;
    }

    private boolean isAllUnChecked() {
        for (ItemWithdrawOrderVM vm : itemVMs) {
            if (vm.isCheck.get()) return false;
        }
        return true;
    }

}
