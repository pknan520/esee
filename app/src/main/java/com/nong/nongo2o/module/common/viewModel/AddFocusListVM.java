package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.fragment.AddFocusListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-19.
 */

public class AddFocusListVM implements ViewModel {

    private AddFocusListFragment fragment;

    public final ObservableField<Integer> numOfLocalFriends= new ObservableField<>();
    //  推荐商家列表
    public final ObservableList<ItemMerchantListVM> itemMerchantListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMerchantListVM> itemMerchantListBinding = ItemBinding.of(BR.viewModel, R.layout.item_merchant_list);

    public AddFocusListVM(AddFocusListFragment fragment) {
        this.fragment = fragment;

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
        numOfLocalFriends.set(16);

        for (int i = 0; i < 10; i++) {
            itemMerchantListVMs.add(new ItemMerchantListVM(fragment));
        }
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        viewStyle.isRefreshing.set(true);

        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    viewStyle.isRefreshing.set(false);
                });
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        for (int i = 0; i < 10; i++) {
            itemMerchantListVMs.add(new ItemMerchantListVM(fragment));
        }
    }
}
