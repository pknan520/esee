package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;
import com.nong.nongo2o.module.personal.fragment.SettingFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-6-26.
 */

public class SettingVM implements ViewModel {

    private SettingFragment fragment;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_36;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> nickName = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    public final ObservableField<String> city = new ObservableField<>();

    public SettingVM(SettingFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        /*nickName.set("PKnan0406");
        city.set("所属地区");*/

        RetrofitHelper.getUserAPI().userProfile()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    headUri.set(resp.getAvatar());
                    nickName.set(resp.getUserNick());
                    summary.set(resp.getRemark());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }, () -> {});
    }

    /**
     * 选择地区
     */
    public final ReplyCommand selectCityClick = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment, SelectAreaFragment.newInstance(), SelectAreaFragment.TAG);
    });

    /**
     * 意见反馈
     */
    public final ReplyCommand feedbackClick = new ReplyCommand(() -> {

    });

    /**
     * 关于我们
     */
    public final ReplyCommand aboutClick = new ReplyCommand(() -> {

    });

    /**
     * 登出
     */
    public final ReplyCommand logoutClick = new ReplyCommand(this::logout);

    private void logout() {

    }
}
