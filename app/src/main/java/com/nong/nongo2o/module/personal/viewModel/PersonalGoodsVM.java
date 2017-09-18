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
import com.nong.nongo2o.entity.bean.SalerInfo;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.common.viewModel.ItemGoodsListVM;
import com.nong.nongo2o.module.personal.fragment.PersonalGoodsFragment;
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

public class PersonalGoodsVM implements ViewModel {

    private PersonalGoodsFragment fragment;
    private SimpleUser user;

    //  商品列表
    public final ObservableList<ItemGoodsListVM> itemGoodsListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemGoodsListVM> itemGoodsListBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_list);

    private int pageSize = 10;
    private int total = 0;

    public PersonalGoodsVM(PersonalGoodsFragment fragment, SimpleUser user) {
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
        getGoodList(1, true);
    }

    /**
     * 获取商品列表
     */
    private void getGoodList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getGoodsAPI()
                .getSalerGoods(user.getUserCode(), page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) itemGoodsListVMs.clear();

                    total = resp.getTotal();
                    Intent intent = new Intent("updateGoodsNum");
                    intent.putExtra("goodsNum", total);
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);

                    for (Goods good : resp.getRows()) {
                        itemGoodsListVMs.add(new ItemGoodsListVM(fragment, good));
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
        getGoodList(1, true);
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemGoodsListVMs.size() < total) {
            getGoodList(itemGoodsListVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }


}
