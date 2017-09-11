package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemGoodsListVM;
import com.nong.nongo2o.module.personal.fragment.PersonalGoodsFragment;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-15.
 */

public class PersonalGoodsVM implements ViewModel {

    private PersonalGoodsFragment fragment;

    //  商品列表
    public final ObservableList<ItemGoodsListVM> itemGoodsListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemGoodsListVM> itemGoodsListBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_list);

    public PersonalGoodsVM(PersonalGoodsFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        for (int i = 0; i < 10; i++) {
            itemGoodsListVMs.add(new ItemGoodsListVM(fragment));
        }
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> {
        loadMoreData();
    });

    private void loadMoreData() {
        for (int i = 0; i < 10; i++) {
            itemGoodsListVMs.add(new ItemGoodsListVM(fragment));
        }
    }


}
