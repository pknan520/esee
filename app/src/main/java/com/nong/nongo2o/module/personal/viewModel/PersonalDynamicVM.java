package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemDynamicListVM;
import com.nong.nongo2o.module.personal.fragment.PersonalDynamicFragment;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-15.
 */

public class PersonalDynamicVM implements ViewModel {

    private PersonalDynamicFragment fragment;
    //  动态列表
    public final ObservableList<ItemDynamicListVM> itemDynamicVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDynamicListVM> itemDynamicBinding = ItemBinding.of(BR.viewModel, R.layout.item_dynamic_list);

    public PersonalDynamicVM(PersonalDynamicFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        for (int i = 0; i < 10; i++) {
            itemDynamicVMs.add(new ItemDynamicListVM(fragment, null));
        }
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(new Consumer<Integer>() {
        @Override
        public void accept(@NonNull Integer integer) throws Exception {
            for (int i = 0; i < 10; i++) {
                itemDynamicVMs.add(new ItemDynamicListVM(fragment, null));
            }
        }
    });
}
