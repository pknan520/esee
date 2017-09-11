package com.nong.nongo2o.module.main.viewModel.share;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.main.fragment.share.ShareMineFragment;
import com.nong.nongo2o.module.share.activity.ShareDetailActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-23.
 */

public class ShareMineVM implements ViewModel {

    private ShareMineFragment fragment;

    public ShareMineVM(ShareMineFragment fragment) {
        this.fragment = fragment;

        for (int i = 0; i < 20; i++) {
            itemShareMineVMs.add(new ItemShareMineVM("分享——我的" + i));
        }
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
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
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> {
        loadMore();
    });

    private void loadMore() {
        for (int i = 0; i < 10; i++) {
            itemShareMineVMs.add(new ItemShareMineVM("分享——我的" + i));
        }
    }

    /**
     * 我的分享列表
     */
    public final ObservableList<ItemShareMineVM> itemShareMineVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemShareMineVM> itemShareMineBinding = ItemBinding.of(BR.viewModel, R.layout.item_share_mine);

    public class ItemShareMineVM implements ViewModel {

        public final ObservableField<String> statusStr = new ObservableField<>();

        public ItemShareMineVM(String str) {
            statusStr.set(str);
        }

        /**
         * 点击查看详情
         */
        public final ReplyCommand detailClick = new ReplyCommand(new Action() {
            @Override
            public void run() throws Exception {
                fragment.getActivity().startActivity(ShareDetailActivity.newIntent(fragment.getActivity()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            }
        });
    }
}
