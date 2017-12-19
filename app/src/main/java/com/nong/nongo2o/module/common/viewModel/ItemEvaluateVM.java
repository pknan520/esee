package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.RatingBar;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.GoodsComment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017-7-12.
 */

public class ItemEvaluateVM implements ViewModel {

    private GoodsComment comment;
    private boolean isFirst;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<Float> rating = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();

    public ItemEvaluateVM(GoodsComment comment, boolean isFirst) {
        this.comment = comment;
        this.isFirst = isFirst;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isFirst = new ObservableBoolean(false);
    }

    /**
     *  初始化数据
     */
    private void initData() {
        viewStyle.isFirst.set(isFirst);
//        viewStyle.marginTop.set(isFirst ? "0dp" : "10dp");

        rating.set(Float.parseFloat(String.valueOf(comment.getStar())));
        headUri.set(comment.getUser().getAvatar());
        name.set(comment.getUser().getUserNick());
        content.set(comment.getContent());
        time.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(comment.getCreateTime()));
    }
}
