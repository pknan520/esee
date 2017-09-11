package com.nong.nongo2o.module.dynamic.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemSelectGoodsVM;
import com.nong.nongo2o.module.dynamic.fragment.DynamicGoodsListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-7.
 */

public class DynamicGoodsListVM implements ViewModel {

    private DynamicGoodsListFragment fragment;
    private ItemSelectGoodsVM.SelectClickListener listener = new ItemSelectGoodsVM.SelectClickListener() {
        @Override
        public void selectClick() {
            for (ItemSelectGoodsVM item : itemSelectGoodsVMs) {
                item.isSelected.set(false);
            }
        }
    };

    public DynamicGoodsListVM(DynamicGoodsListFragment fragment) {
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
        for (int i = 0; i < 20; i++) {
            itemSelectGoodsVMs.add(new ItemSelectGoodsVM(listener));
        }
    }

    /**
     * 确定添加按键
     */
    public final ReplyCommand confirmClick = new ReplyCommand(() -> {
        Toast.makeText(fragment.getActivity(), "你点击了确定添加按钮", Toast.LENGTH_SHORT).show();
    });

    /**
     * 选择商品列表
     */
    public final ObservableList<ItemSelectGoodsVM> itemSelectGoodsVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemSelectGoodsVM> itemSelectGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_select_goods_list);

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(new Action() {
        @Override
        public void run() throws Exception {
            refreshData();
        }
    });

    private void refreshData() {
        viewStyle.isRefreshing.set(true);

        Observable.just(0)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    viewStyle.isRefreshing.set(false);
                });
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> {
        for (int i = 0; i < 10; i++) {
            itemSelectGoodsVMs.add(new ItemSelectGoodsVM(listener));
        }
    });
}
