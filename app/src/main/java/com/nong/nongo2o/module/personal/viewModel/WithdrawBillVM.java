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
import com.nong.nongo2o.module.personal.fragment.WithdrawBillFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by PANYJ7 on 2018-3-7.
 */

public class WithdrawBillVM implements ViewModel {

    private WithdrawBillFragment fragment;

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    private int pageSize = 10;
    private int total = 0;
    public final ObservableList<ItemWithdrawBillVM> itemVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemWithdrawBillVM> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_withdraw_bill);

    public WithdrawBillVM(WithdrawBillFragment fragment) {
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
        if (itemVMs.size() < total) {
            getBillList(false, itemVMs.size() / pageSize + 1);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBillList(boolean isForce, int page) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getBillServiceAPI()
                .getBillList("-1", page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (isForce) itemVMs.clear();
                    total = resp.getTotal();
                    for (Bill bill : resp.getRows()) {
                        itemVMs.add(new ItemWithdrawBillVM(bill));
                    }
                    viewStyle.isEmpty.set(itemVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () ->  viewStyle.isRefreshing.set(false));
    }

    public class ItemWithdrawBillVM implements ViewModel {

        private Bill bill;

        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_36;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> orderNo = new ObservableField<>();

        public final ObservableField<BigDecimal> money = new ObservableField<>();
        public final ObservableField<String> time = new ObservableField<>();

        public ItemWithdrawBillVM(Bill bill) {
            this.bill = bill;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            headUri.set(bill.getUser().getAvatar());
            name.set(bill.getUser().getUserNick());
            orderNo.set(bill.getBusinessObject().getOrderCode());

            money.set(bill.getBillMoney());
            time.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(bill.getCreateTime()));
        }
    }
}
