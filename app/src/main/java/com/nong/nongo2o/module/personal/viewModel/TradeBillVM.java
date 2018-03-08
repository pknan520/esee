package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.Bill;
import com.nong.nongo2o.module.personal.activity.OrderCenterActivity;
import com.nong.nongo2o.module.personal.fragment.TradeBillFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by PANYJ7 on 2018-3-7.
 */

public class TradeBillVM implements ViewModel {

    private TradeBillFragment fragment;

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    private int pageSize = 10;
    private int total = 0;
    private int itemQty = 0;
    public final ObservableList<ItemTradeBillVM> itemVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemTradeBillVM> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_trade_bill);

    public TradeBillVM(TradeBillFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getBillList(true, 1);
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getBillList(true, 1);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreData = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemQty < total) {
            getBillList(false, itemQty / pageSize + 1);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBillList(boolean isForce, int page) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getBillServiceAPI()
                .getBillList("0,1,2", page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (isForce) {
                        itemVMs.clear();
                        itemQty = 0;
                    }
                    total = resp.getTotal();
                    for (Bill bill : resp.getRows()) {
                        if (itemVMs.isEmpty()) {
                            itemVMs.add(new ItemTradeBillVM(bill));
                        } else {
                            if (itemVMs.get(itemVMs.size() - 1).bill.getBusinessCode().equals(bill.getBusinessCode())) {
                                itemVMs.get(itemVMs.size() - 1).appendChild(bill);
                            } else {
                                itemVMs.add(new ItemTradeBillVM(bill));
                            }
                        }
                        itemQty += 1;
                    }
                    viewStyle.isEmpty.set(itemVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    public class ItemTradeBillVM implements ViewModel {

        private Bill bill;

        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_36;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> orderNo = new ObservableField<>();

        public final ObservableList<ItemTradeDetailVM> itemDetailVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemTradeDetailVM> itemDetailBinding = ItemBinding.of(BR.viewModel, R.layout.item_trade_detail);

        public ItemTradeBillVM(Bill bill) {
            this.bill = bill;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            headUri.set(bill.getBusinessObject().getUser().getAvatar());
            name.set(bill.getBusinessObject().getUser().getUserNick());
            orderNo.set(bill.getBusinessCode());

            itemDetailVMs.add(new ItemTradeDetailVM(bill));
        }

        private void appendChild(Bill bill) {
            itemDetailVMs.add(0, new ItemTradeDetailVM(bill));
        }

        /**
         * 点击事件
         */
        public final ReplyCommand itemClick = new ReplyCommand(this::checkOrderDetail);

        /**
         * 查询订单详情
         */
        private void checkOrderDetail() {
            viewStyle.isRefreshing.set(true);

            RetrofitHelper.getOrderAPI()
                    .getOrderDetail(bill.getBusinessCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (resp.getCode().equals("0")) {
                            if (resp.getData() != null) {
                                fragment.getActivity().startActivity(OrderCenterActivity.newIntent(fragment.getActivity(), resp.getData(), true));
                            } else {
                                Toast.makeText(fragment.getActivity(), "找不到订单，请联系客服", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(fragment.getActivity(), resp.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        viewStyle.isRefreshing.set(false);
                    });
        }

        public class ItemTradeDetailVM implements ViewModel {

            private Bill bill;

            public final ObservableField<String> type = new ObservableField<>();
            public final ObservableField<BigDecimal> money = new ObservableField<>();
            public final ObservableField<String> time = new ObservableField<>();

            public ItemTradeDetailVM(Bill bill) {
                this.bill = bill;

                initData();
            }

            public final ViewStyle viewStyle = new ViewStyle();

            public class ViewStyle {
                public final ObservableField<Integer> billType = new ObservableField<>();
            }

            /**
             * 初始化数据
             */
            private void initData() {
                viewStyle.billType.set(bill.getBillType());
                switch (bill.getBillType()) {
                    case 0:
                        type.set("收入");
                        money.set(bill.getBillMoney());
                        break;
                    case 1:
                        type.set("退款");
                        money.set(bill.getBusinessObject() != null ? bill.getBusinessObject().getRefundAmount() : new BigDecimal(0.00));
                        break;
                    case 2:
                        type.set("退款");
                        money.set(bill.getBusinessObject() != null ? bill.getBusinessObject().getRefundAmount() : new BigDecimal(0.00));
                        break;
                }
                time.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(bill.getCreateTime()));
            }

            /**
             * 点击事件
             */
            public final ReplyCommand itemClick = new ReplyCommand(ItemTradeBillVM.this::checkOrderDetail);
        }
    }
}
