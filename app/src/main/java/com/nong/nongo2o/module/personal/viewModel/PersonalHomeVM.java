package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.personal.fragment.PersonalHomeFragment;

/**
 * Created by Administrator on 2017-6-30.
 */

public class PersonalHomeVM implements ViewModel {

    private PersonalHomeFragment fragment;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_homepage_72;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    //  数据
    public final ObservableField<Integer> goodsNum = new ObservableField<>();
    public final ObservableField<Integer> fansNum = new ObservableField<>();
    public final ObservableField<Integer> dynamicNum = new ObservableField<>();
    //  地区
    public final ObservableField<String> city = new ObservableField<>();
    //  是否关注
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus_white;
    @DrawableRes
    public final int isFocus = R.mipmap.icon_like;

    public PersonalHomeVM(PersonalHomeFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isFocus = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        name.set("NeilsonLo");
        goodsNum.set(23);
        fansNum.set(628);
        dynamicNum.set(41);
        city.set("广东 顺德");
    }
}
