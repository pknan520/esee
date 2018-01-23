package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.personal.fragment.MyInvitersFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by PANYJ7 on 2018-1-10.
 */

public class MyInvitersVM implements ViewModel {

    private MyInvitersFragment fragment;

    public final ObservableList<ItemMyInvitersVM> itemMyInviterVMs = new ObservableArrayList<>();
    public ItemBinding<ItemMyInvitersVM> itemMyInviterBinding = ItemBinding.of(BR.viewModel, R.layout.item_my_inviters);
    private int pageSize = 10;
    private int total;

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;

    public MyInvitersVM(MyInvitersFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
    }

    /**
     * 初始数据
     */
    private void initData() {
        getMyInviters(1, true);
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getMyInviters(1, true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemMyInviterVMs.size() < total) {
            getMyInviters(itemMyInviterVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "已经到底了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取我关注的人
     */
    private void getMyInviters(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getUserAPI()
                .getMyInviters(UserInfo.getInstance().getInviteCode(), page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) {
                        itemMyInviterVMs.clear();
                        total = resp.getTotal();
                    }
                    for (SimpleUser user : resp.getRows()) {
                        if (!TextUtils.isEmpty(user.getUserName()) && !TextUtils.isEmpty(user.getAvatar())) {
                            itemMyInviterVMs.add(new ItemMyInvitersVM(fragment, user));
                        } else {
                            total -= 1;
                        }
                    }
                    viewStyle.isEmpty.set(itemMyInviterVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }
}
