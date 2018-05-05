package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.module.common.fragment.AddFocusListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-19.
 */

public class AddFocusListVM implements ViewModel {

    private AddFocusListFragment fragment;

    public final ObservableField<Integer> numOfLocalFriends = new ObservableField<>();
    //  推荐商家列表
    public final ObservableList<ItemUserVM> itemUserVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemUserVM> itemUserBinding = ItemBinding.of(BR.viewModel, R.layout.item_user_list);

    private String searchVal = "";
    private final int pageSize = 10;
    private int total;

    public AddFocusListVM(AddFocusListFragment fragment) {
        this.fragment = fragment;

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
        getUserList(searchVal, 1, true);
    }

    /**
     * 查询用户列表
     */
    public void getUserList(String searchVal, int page, boolean isForce) {
        viewStyle.isRefreshing.set(true);
        if (isForce) {
            this.searchVal = searchVal;
        }

        RetrofitHelper.getUserAPI()
                .getUserList(1, page, pageSize, searchVal)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (isForce) itemUserVMs.clear();
                    total = resp.getTotal();
                    for (SimpleUser user : resp.getRows()) {
                        itemUserVMs.add(new ItemUserVM(fragment, user));
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                },  () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getUserList(searchVal, 1, true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemUserVMs.size() < total) {
            getUserList(searchVal,itemUserVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "已经到底了^.^", Toast.LENGTH_SHORT).show();
        }
    }
}
