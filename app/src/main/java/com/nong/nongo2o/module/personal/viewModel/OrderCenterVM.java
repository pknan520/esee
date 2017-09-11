package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.module.personal.fragment.OrderListTotalFragment;

/**
 * Created by Administrator on 2017-7-14.
 */

public class OrderCenterVM implements ViewModel {

    private OrderListTotalFragment fragment;

    public OrderCenterVM(OrderListTotalFragment fragment) {
        this.fragment = fragment;
    }

    public class ItemTabVM implements ViewModel {

        public final ObservableField<String> tabBadgeText = new ObservableField<>();

        public ItemTabVM() {

            initFakeData();
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            tabBadgeText.set(String.valueOf((int) (Math.random() * 100)));
        }

    }

}
