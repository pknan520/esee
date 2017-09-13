package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.request.IdRequest;
import com.nong.nongo2o.entity.request.UpdateUserRequest;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;
import com.nong.nongo2o.module.personal.fragment.SettingFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    public final ObservableField<String> id = new ObservableField<>();
    public final ObservableField<String> userCode = new ObservableField<>();

    public SettingVM(SettingFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }


    private void initFakeData() {

        RetrofitHelper.getUserAPI().userProfile()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    headUri.set(resp.getAvatar());
                    nickName.set(resp.getUserNick());
                    summary.set(resp.getProfile());
                    id.set(resp.getId());
                    userCode.set(resp.getUserCode());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }, () -> {});
    }

    /**
     * 保存个人资料
     */
    public final ReplyCommand saveUserProfileClick = new ReplyCommand(() -> {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setId(id.get());
        updateUserRequest.setUserCode(userCode.get());
        updateUserRequest.setProfile(summary.get());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(updateUserRequest));

        RetrofitHelper.getUserAPI().updateUserPofile(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    Toast.makeText(fragment.getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    });

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
