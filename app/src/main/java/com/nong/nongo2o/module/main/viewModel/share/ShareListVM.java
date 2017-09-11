package com.nong.nongo2o.module.main.viewModel.share;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemShareListVM;
import com.nong.nongo2o.module.main.fragment.share.ShareListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-23.
 */

public class ShareListVM implements ViewModel {

    private ShareListFragment fragment;
    private String[] statusStr = {"分享——最新", "分享——附近"};
    private int status;

    public ShareListVM(ShareListFragment fragment, int status) {
        this.fragment = fragment;
        this.status = status;

        for (int i = 0; i < 20; i++) {
            itemShareListVMs.add(new ItemShareListVM(fragment, statusStr[status] + i));
        }
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 分享列表
     */
    public final ObservableList<ItemShareListVM> itemShareListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemShareListVM> itemShareListBinding = ItemBinding.of(BR.viewModel, R.layout.item_share_list);

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
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> {
        for (int i = 0; i < 10; i++) {
            itemShareListVMs.add(new ItemShareListVM(fragment, statusStr[status] + i));
        }
    });
}
