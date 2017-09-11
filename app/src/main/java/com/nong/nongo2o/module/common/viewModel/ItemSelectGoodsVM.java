package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;

/**
 * Created by Administrator on 2017-7-7.
 */

public class ItemSelectGoodsVM implements ViewModel {

    private SelectClickListener listener;

    public final ObservableBoolean isSelected = new ObservableBoolean(false);
    @DrawableRes
    public final int imgPlaceHolder = R.mipmap.ic_launcher;
    public final ObservableField<String> imgUrl = new ObservableField<>();
    public final ObservableField<String> goodsName = new ObservableField<>();
    public final ObservableField<String> goodsSummary = new ObservableField<>();
    public final ObservableField<String> goodsPrice = new ObservableField<>();

    public interface SelectClickListener {
        void selectClick();
    }

    public ItemSelectGoodsVM(SelectClickListener listener) {
        this.listener = listener;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        goodsName.set("墨西哥进口牛油果");
        goodsSummary.set("精装4只/盒");
        goodsPrice.set("¥48.80");
    }

    public final ReplyCommand selectClick = new ReplyCommand(() -> {
        listener.selectClick();
        isSelected.set(!isSelected.get());
    });
}