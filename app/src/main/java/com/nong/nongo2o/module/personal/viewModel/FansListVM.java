package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.Follow;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.fragment.FansListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-17.
 */

public class FansListVM implements ViewModel {

    private FansListFragment fragment;
    private int status;

    private final int pageSize = 10;
    private int total;

    public final ObservableList<ItemFansListVM> itemFansListVMs = new ObservableArrayList<>();
    public ItemBinding<ItemFansListVM> itemFansListBinding = ItemBinding.of(BR.viewModel, R.layout.item_fans_list);

    public FansListVM(FansListFragment fragment, int status) {
        this.fragment = fragment;
        this.status = status;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 初始数据
     */
    private void initData() {
        if (status == FansMgrActivity.MY_FOCUS) {
            searchFocus(1);
        } else {
            searchFans(1);
        }
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        itemFansListVMs.clear();
        if (status == FansMgrActivity.MY_FOCUS) {
            searchFocus(1);
        } else {
            searchFans(1);
        }
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemFansListVMs.size() < total) {
            if (status == FansMgrActivity.MY_FOCUS) {
                searchFocus(itemFansListVMs.size() / pageSize + 1);
            } else {
                searchFans(itemFansListVMs.size() / pageSize + 1);
            }
        }
    }

    /**
     * 获取关注列表
     */
    private void searchFocus(int page) {
        viewStyle.isRefreshing.set(true);
        RetrofitHelper.getFollowAPI()
                .userFollowSearch(2,page,pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    total = resp.getTotal();
                    for (Follow follow : resp.getRows()) {
                        itemFansListVMs.add(new ItemFansListVM(fragment, follow, FansMgrActivity.MY_FOCUS));
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 获取粉丝列表
     */
    private void searchFans(int page) {
        viewStyle.isRefreshing.set(true);
        RetrofitHelper.getFollowAPI()
                .userFollowSearch(1,page,pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    total = resp.getTotal();
                    for (Follow follow : resp.getRows()) {
                        itemFansListVMs.add(new ItemFansListVM(fragment, follow, FansMgrActivity.MY_FANS));
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }
}
