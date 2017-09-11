package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;

/**
 * Created by Administrator on 2017-7-14.
 */

public class ItemTransListVM implements ViewModel {

    public final ObservableField<String> time = new ObservableField<>();
    public final ObservableField<String> day = new ObservableField<>();
    @DrawableRes
    public final int routeBlue = R.drawable.route_blue;
    @DrawableRes
    public final int routeGrey = R.drawable.route_grey;

    public final ObservableField<String> content = new ObservableField<>();

    public ItemTransListVM(boolean currentStatus) {
        viewStyle.currentStatus.set(currentStatus);

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean currentStatus = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        time.set("20:03:38");
        day.set("2017-06-30");
        content.set("【已签收】您的订单已签收成功，签收人：门把，感谢你使用安得物流");
    }
}
