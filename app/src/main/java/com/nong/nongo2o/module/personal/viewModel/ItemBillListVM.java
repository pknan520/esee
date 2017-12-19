package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.Bill;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017-7-17.
 */

public class ItemBillListVM implements ViewModel {

    private Bill bill;

    public final ObservableField<String> type = new ObservableField<>();
    public final ObservableField<String> status = new ObservableField<>();
    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<BigDecimal> money = new ObservableField<>();
    public final ObservableField<String> no = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();

    public ItemBillListVM(Bill bill) {
        this.bill = bill;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableField<Integer> billType = new ObservableField<>();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (bill != null) {
            viewStyle.billType.set(bill.getBillType());

            switch (bill.getBillType()) {
                case -1:type.set("提现");break;
                case 0:type.set("收入");break;
                case 1:type.set("支出");break;
                case 2:type.set("退款");break;
            }

            switch (bill.getBillStatus()) {
                case -1:status.set("已作废");break;
                case 0:status.set("处理中");break;
                case 1:status.set("处理成功");break;
            }

            name.set(bill.getUser().getUserNick());
            headUri.set(bill.getUser().getAvatar());
            money.set(bill.getBillMoney());
            no.set(bill.getBillCode());
            date.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(bill.getUpdateTime()));
        }
    }
}
