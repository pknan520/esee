package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;

/**
 * Created by Administrator on 2017-7-17.
 */

public class ItemBillListVM implements ViewModel {

    public static final int TYPE_INCOME = 0, TYPE_DISBURSEMENT = 1;

    public final ObservableField<String> type = new ObservableField<>();
    public final ObservableField<String> status = new ObservableField<>();
    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<Double> money = new ObservableField<>();
    public final ObservableField<String> no = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();

    public ItemBillListVM(int billType) {
        viewStyle.billType.set(billType);

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableField<Integer> billType = new ObservableField<>();
    }


    /**
     * 假数据
     */
    private void initFakeData() {
        type.set(viewStyle.billType.get() == TYPE_INCOME ? "提现" : "支出记录");
        status.set(viewStyle.billType.get() == TYPE_INCOME ? "已完成" : "提现成功");
        name.set("NeilsonLo");
        money.set(48.80);
        no.set(viewStyle.billType.get() == TYPE_INCOME ? "单号：T100000248" : "微信账号：pknan");
        date.set("2017/07/17");
    }
}
