package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.Bill;
import com.nong.nongo2o.module.personal.fragment.BillListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BillListVM implements ViewModel {

    private BillListFragment fragment;
    private String billTypeStr;

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;

    private int pageSize = 10;
    private int total = 0;

    public final ObservableList<ItemBillListVM> itemBillListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemBillListVM> itemBillListBinding = ItemBinding.of(BR.viewModel, R.layout.item_bill_list);

    public BillListVM(BillListFragment fragment, String billTypeStr) {
        this.fragment = fragment;
        this.billTypeStr = billTypeStr;

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
        if (itemBillListVMs.size() < total) {
            getBillList(false, itemBillListVMs.size() / pageSize + 1);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBillList(boolean isForce, int page) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getBillServiceAPI()
                .getBillList(billTypeStr, page)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (isForce) itemBillListVMs.clear();
                    total = resp.getTotal();
                    for (Bill bill : resp.getRows()) {
                        itemBillListVMs.add(new ItemBillListVM(bill));
                    }
                    viewStyle.isEmpty.set(itemBillListVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () ->  viewStyle.isRefreshing.set(false));
    }
}
