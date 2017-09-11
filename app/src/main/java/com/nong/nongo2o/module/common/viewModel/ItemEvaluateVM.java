package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.RatingBar;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;

/**
 * Created by Administrator on 2017-7-12.
 */

public class ItemEvaluateVM implements ViewModel {

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<Float> rating = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();

    public ItemEvaluateVM() {

        initFakeData();
    }

    /**
     *  假数据
     */
    private void initFakeData() {
        rating.set((float) (Math.random() * 5));
        name.set("NeilsonLo");
        content.set("买给老婆的，老婆非常爱吃这家店的水果，新鲜又超级超级好吃哒，大好评！");
        time.set("2017/06/30");
    }
}
