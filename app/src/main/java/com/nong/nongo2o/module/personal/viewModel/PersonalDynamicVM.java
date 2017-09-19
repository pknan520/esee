package com.nong.nongo2o.module.personal.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.common.viewModel.ItemDynamicListVM;
import com.nong.nongo2o.module.personal.fragment.PersonalDynamicFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-15.
 */

public class PersonalDynamicVM implements ViewModel {

    private PersonalDynamicFragment fragment;
    private SimpleUser user;
    //  动态列表
    public final ObservableList<ItemDynamicListVM> itemDynamicVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDynamicListVM> itemDynamicBinding = ItemBinding.of(BR.viewModel, R.layout.item_dynamic_list);

    private int total = 0;
    private int pageSize = 10;

    public PersonalDynamicVM(PersonalDynamicFragment fragment, SimpleUser user) {
        this.fragment = fragment;
        this.user = user;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getMomentList(1, true);
    }

    /**
     * 获取动态列表
     */
    private void getMomentList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getDynamicAPI()
                .getUserDynamicList(3, user.getUserCode(), page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) {
                        itemDynamicVMs.clear();
                    }

                    total = resp.getTotal();
                    Intent intent = new Intent("updateDynamicNum");
                    intent.putExtra("dynamicNum", total);
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);

                    for (Moment moment : resp.getRows()) {
                        itemDynamicVMs.add(new ItemDynamicListVM(fragment, moment));
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
        getMomentList(1, true);
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemDynamicVMs.size() < total) {
            getMomentList(itemDynamicVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }
}
