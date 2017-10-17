package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.RatingBar;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.GoodsComment;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Administrator on 2017-7-12.
 */

public class ItemEvaluateVM implements ViewModel {

    private GoodsComment comment;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<Float> rating = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();

    public ItemEvaluateVM(GoodsComment comment) {
        this.comment = comment;

        initData();
    }

    /**
     *  初始化数据
     */
    private void initData() {
        rating.set(Float.parseFloat(String.valueOf(comment.getStar())));
        headUri.set(comment.getUser().getAvatar());
        name.set(comment.getUser().getUserNick());
        content.set(comment.getContent());
        time.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(comment.getCreateTime()));
    }
}
