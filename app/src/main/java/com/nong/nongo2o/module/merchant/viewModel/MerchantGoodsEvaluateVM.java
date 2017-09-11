package com.nong.nongo2o.module.merchant.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemEvaluateVM;
import com.nong.nongo2o.module.merchant.fragment.MerchantGoodsEvaluateFragment;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-12.
 */

public class MerchantGoodsEvaluateVM implements ViewModel {

    private MerchantGoodsEvaluateFragment fragment;

    public MerchantGoodsEvaluateVM(MerchantGoodsEvaluateFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        for (int i = 0; i < 10; i++) {
            itemEvaluateVMs.add(new ItemEvaluateVM());
        }
    }

    /**
     * 评价列表
     */
    public final ObservableList<ItemEvaluateVM> itemEvaluateVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemEvaluateVM> itemEvaluateBinding = ItemBinding.of(BR.viewModel, R.layout.item_evaluate_list);

    /**
     * 上拉加载
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(integer -> {
        for (int i = 0; i < 10; i++) {
            itemEvaluateVMs.add(new ItemEvaluateVM());
        }
    });
}
