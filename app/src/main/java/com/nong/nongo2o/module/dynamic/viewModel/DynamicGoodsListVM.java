package com.nong.nongo2o.module.dynamic.viewModel;

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
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.common.viewModel.ItemSelectGoodsVM;
import com.nong.nongo2o.module.dynamic.fragment.DynamicGoodsListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-7.
 */

public class DynamicGoodsListVM implements ViewModel {

    private DynamicGoodsListFragment fragment;
    private int status;
    private ItemSelectGoodsVM.SelectClickListener listener = new ItemSelectGoodsVM.SelectClickListener() {
        @Override
        public void selectClick(Goods goods) {
            for (ItemSelectGoodsVM item : itemSelectGoodsVMs) {
                item.isSelected.set(false);
            }
            selectGoods = goods;
        }
    };

    //  选择商品列表
    public final ObservableList<ItemSelectGoodsVM> itemSelectGoodsVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemSelectGoodsVM> itemSelectGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_select_goods_list);

    private int total = 0;
    private final int pageSize = 10;
    private Goods selectGoods;

    public DynamicGoodsListVM(DynamicGoodsListFragment fragment, int status) {
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
    private void initData() {
        getGoodsList(1, true);
    }

    /**
     * 获取商品列表
     */
    private void getGoodsList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        if (status == 0) {
            //  我的商品
            RetrofitHelper.getGoodsAPI()
                    .userGoodsSearch(1, page, pageSize)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (force) itemSelectGoodsVMs.clear();
                        total = resp.getTotal();
                        for (Goods good : resp.getRows()) {
                            itemSelectGoodsVMs.add(new ItemSelectGoodsVM(fragment, listener, good));
                        }
                    }, throwable -> {
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        viewStyle.isRefreshing.set(false);
                    }, () -> viewStyle.isRefreshing.set(false));
        } else if (status == 1) {
            //  我买到的商品
            RetrofitHelper.getGoodsAPI()
                    .getBoughtGoodsList(page, pageSize)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (force) itemSelectGoodsVMs.clear();
                        total = resp.getTotal();
                        for (Goods good : resp.getRows()) {
                            itemSelectGoodsVMs.add(new ItemSelectGoodsVM(fragment, listener, good));
                        }
                    }, throwable -> {
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        viewStyle.isRefreshing.set(false);
                    }, () -> viewStyle.isRefreshing.set(false));
        }
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getGoodsList(1, true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemSelectGoodsVMs.size() < total) {
            getGoodsList(itemSelectGoodsVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 确定添加按键
     */
    public final ReplyCommand confirmClick = new ReplyCommand(() -> {
        if (selectGoods == null) {
            Toast.makeText(fragment.getActivity(), "请选择一件商品", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent("selectGoods");
        intent.putExtra("Goods", selectGoods);
        LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
        fragment.getActivity().getFragmentManager().popBackStack();
    });
}
