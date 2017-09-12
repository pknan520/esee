package com.nong.nongo2o.module.main.viewModel.dynamic;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
import com.nong.nongo2o.module.common.viewModel.ItemDynamicListVM;
import com.nong.nongo2o.module.main.fragment.dynamic.DynamicListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-22.
 */

public class DynamicListVM implements ViewModel {

    private DynamicListFragment fragment;
    private int status;

    private final int pageSize = 10;
    private int total;

    public DynamicListVM(DynamicListFragment fragment, int status) {
        this.fragment = fragment;
        this.status = status;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        getDynamicList(1, true);
    }

    /**
     * 获取动态列表
     */
    private void getDynamicList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getDynamicAPI()
                .getDynamicList(status, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) {
                        itemDynamicVMs.clear();
                    }
                    total = resp.getTotal();
                    for (Moment moment : resp.getRows()) {
                        itemDynamicVMs.add(new ItemDynamicListVM(fragment, moment));
                    }
                    if (itemDynamicVMs.size() == 0) {
                        fragment.showEmptyView();
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getDynamicList(1, true);
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> omLoadMoreCommand = new ReplyCommand<>(integer -> onLoadMore());

    private void onLoadMore() {
        if (itemDynamicVMs.size() < total) {
            getDynamicList(itemDynamicVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容啦^.^", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加好友
     */
    public final ReplyCommand addFriendsClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(AddFocusActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 动态列表
     */
    public final ObservableList<ItemDynamicListVM> itemDynamicVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDynamicListVM> itemDynamicBinding = ItemBinding.of(BR.viewModel, R.layout.item_dynamic_list);
}
