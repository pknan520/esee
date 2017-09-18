package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.personal.fragment.PersonalHomeFragment;
import com.nong.nongo2o.uils.FocusUtils;

/**
 * Created by Administrator on 2017-6-30.
 */

public class PersonalHomeVM implements ViewModel {

    private PersonalHomeFragment fragment;
    private SimpleUser user;

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

    // TODO: 2017-9-18 临时容错，以后删除
    public PersonalHomeVM(PersonalHomeFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public PersonalHomeVM(PersonalHomeFragment fragment, SimpleUser user) {
        this.fragment = fragment;
        this.user = user;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isFocus = new ObservableBoolean(false);
        public final ObservableBoolean isMySelf = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (user != null) {
            headUri.set(user.getAvatar());
            name.set(user.getUserNick());
            fansNum.set(user.getFollowers());
            city.set(user.getLocation());
            viewStyle.isMySelf.set(user.getUserCode().equals(UserInfo.getInstance().getUserCode()));
            if (!viewStyle.isMySelf.get()) {
                viewStyle.isFocus.set(FocusUtils.checkIsFocus(user.getUserCode()));
            }
        }
    }

    public void setGoodsNum(int num) {
        goodsNum.set(num);
    }

    public void setDynamicNum(int num) {
        dynamicNum.set(num);
    }
}
