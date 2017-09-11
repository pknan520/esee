package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.personal.fragment.BillListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-17.
 */

public class BillListVM implements ViewModel {

    private BillListFragment fragment;
    private int billType;

    public final ObservableList<ItemBillListVM> itemBillListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemBillListVM> itemBillListBinding = ItemBinding.of(BR.viewModel, R.layout.item_bill_list);

    public BillListVM(BillListFragment fragment, int billType) {
        this.fragment = fragment;
        this.billType = billType;

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        for (int i = 0; i < 10; i++) {
            itemBillListVMs.add(new ItemBillListVM(billType));
        }
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        viewStyle.isRefreshing.set(true);

        Observable.just(0)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    viewStyle.isRefreshing.set(false);
                });
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreData = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        for (int i = 0; i < 10; i++) {
            itemBillListVMs.add(new ItemBillListVM(billType));
        }
    }
}
