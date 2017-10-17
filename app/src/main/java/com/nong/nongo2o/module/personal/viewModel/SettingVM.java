package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.request.UpdateUserRequest;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.personal.fragment.SettingFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AppManager;
import com.nong.nongo2o.uils.SPUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
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

//    public final ObservableField<String> id = new ObservableField<>();
//    public final ObservableField<String> userCode = new ObservableField<>();

    public SettingVM(SettingFragment fragment) {
        this.fragment = fragment;

        initData();
    }


    private void initData() {
        headUri.set(UserInfo.getInstance().getAvatar());
        nickName.set(UserInfo.getInstance().getUserNick());
        summary.set(UserInfo.getInstance().getProfile());
    }

    /**
     * 保存个人资料
     */
    public final ReplyCommand saveUserProfileClick = new ReplyCommand(() -> {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
//        updateUserRequest.setId(id.get());
        updateUserRequest.setUserCode(UserInfo.getInstance().getUserCode());
        updateUserRequest.setProfile(summary.get());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(updateUserRequest));

        RetrofitHelper.getUserAPI().updateUserPofile(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    UserInfo.getInstance().setProfile(summary.get());
                    Toast.makeText(fragment.getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    });

    /**
     * 选择地区
     */
    public final ReplyCommand selectCityClick = new ReplyCommand(() -> {
        fragment.startActivityForResult(SelectAreaActivity.newIntent(fragment.getActivity()), SettingFragment.GET_AREA);
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 设置城市
     */
    public void setCities(City cityP, City cityC, City cityA) {
        city.set(cityP.getCity_name() + " " + cityC.getCity_name() + " " + cityA.getCity_name());
    }

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
        RetrofitHelper.getAccountAPI()
                .logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringApiResponse -> {
                    UserInfo.getInstance().clearUser();
                    EMClient.getInstance().logout(true);

                    SPUtils.remove(fragment.getActivity(), "WX_ACCESS_TOKEN");
                    SPUtils.remove(fragment.getActivity(), "WX_REFRESH_TOKEN");
                    SPUtils.remove(fragment.getActivity(), "WX_ACCESS_EXPIRE");
                    SPUtils.remove(fragment.getActivity(), "WX_REFRESH_EXPIRE");
                    SPUtils.remove(fragment.getActivity(), "WX_OPENID");

                    AppManager.getAppManager().finishAllActivity();
                    fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), false));

                }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
