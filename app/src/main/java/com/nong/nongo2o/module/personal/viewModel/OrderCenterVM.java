package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.module.personal.fragment.OrderListTotalFragment;

/**
 * Created by Administrator on 2017-7-14.
 */

public class OrderCenterVM implements ViewModel {

    private OrderListTotalFragment fragment;
    private boolean isMerchantMode;

    public OrderCenterVM(OrderListTotalFragment fragment,boolean isMerchantMode) {
        this.fragment = fragment;
        this.isMerchantMode = isMerchantMode;
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
