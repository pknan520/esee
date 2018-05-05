package com.nong.nongo2o.module.login.viewModel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.request.CreateUserRequest;
import com.nong.nongo2o.entity.response.WxAccessToken;
import com.nong.nongo2o.entity.response.WxInfo;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.login.fragment.BindMobileFragment;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.module.personal.fragment.AddressEditFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.service.InitDataService;
import com.nong.nongo2o.uils.SPUtils;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-9-23.
 */

public class BindMobileVM implements ViewModel {

    private BindMobileFragment fragment;
    private WxAccessToken wxAccessToken;
    private WxInfo wxInfo;

    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> validCode = new ObservableField<>();
    public final ObservableField<String> city = new ObservableField<>();

    private City cityP, cityC, cityD;

    public BindMobileVM(BindMobileFragment fragment, WxAccessToken wxAccessToken, WxInfo wxInfo) {
        this.fragment = fragment;
        this.wxAccessToken = wxAccessToken;
        this.wxInfo = wxInfo;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean btnTimeClickable = new ObservableBoolean(false);
        public final ObservableBoolean hasSelectArea = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    public final ReplyCommand<String> afterMobileChange = new ReplyCommand<>(s -> {
        viewStyle.btnTimeClickable.set(!TextUtils.isEmpty(s) && s.length() == 11);
    });

    /**
     * 获取验证码
     */
    public final ReplyCommand getValidCodeClick = new ReplyCommand(this::getValidCOde);

    private void getValidCOde() {
        if (TextUtils.isEmpty(mobile.get()) || mobile.get().length() != 11) {
            Toast.makeText(fragment.getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitHelper.getUserAPI()
                .getValidCode(mobile.get(), wxAccessToken.getOpenid())
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> Toast.makeText(fragment.getActivity(), "请注意查收验证码短信", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * 选择城市
     */
    public final ReplyCommand selectCityClick = new ReplyCommand(() -> {
        fragment.startActivityForResult(SelectAreaActivity.newIntent(fragment.getActivity()), AddressEditFragment.GET_AREA);
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 设置城市
     */
    public void setCities(City cityP, City cityC, City cityD, City cityS) {
        viewStyle.hasSelectArea.set(true);
        this.cityP = cityP;
        this.cityC = cityC;
        this.cityD = cityD;
        city.set(cityP.getCity_name() + " " + cityC.getCity_name() + " " + cityD.getCity_name());
    }

    /**
     * 提交注册
     */
    public final ReplyCommand bindClick = new ReplyCommand(this::bindMobile);

    private void bindMobile() {
        if (TextUtils.isEmpty(mobile.get()) || mobile.get().length() != 11) {
            Toast.makeText(fragment.getActivity(), "请输入合法的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(validCode.get())) {
            Toast.makeText(fragment.getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cityP == null || cityC == null || cityD == null) {
            Toast.makeText(fragment.getActivity(), "请选择你的所属地", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateUserRequest request = new CreateUserRequest();
        request.setOpenId(wxInfo.getOpenid());
        request.setPhone(mobile.get());
        request.setValidCode(validCode.get());
        request.setUserName(wxInfo.getNickname());
        request.setUserNick(wxInfo.getNickname());
        request.setSex(wxInfo.getSex());
        request.setLocation(wxInfo.getProvince());
        request.setAvatar(wxInfo.getHeadimgurl());
        request.setConsigneeAddress(cityP.getCity_name() + cityC.getCity_name() + cityD.getCity_name());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(request));

        RetrofitHelper.getAccountAPI()
                .register(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ApiResponseFunc<>())
                .subscribe(user -> {
                    UserInfo.setOurInstance(new UserInfo(user));

                    SPUtils.put(fragment.getActivity(), "WX_ACCESS_TOKEN", wxAccessToken.getAccess_token());
                    SPUtils.put(fragment.getActivity(), "WX_REFRESH_TOKEN", wxAccessToken.getRefresh_token());
                    SPUtils.put(fragment.getActivity(), "WX_ACCESS_EXPIRE", wxAccessToken.getExpires_in() * 1000 + Calendar.getInstance().getTimeInMillis());
                    SPUtils.put(fragment.getActivity(), "WX_REFRESH_EXPIRE", 30 * 24 * 60 * 1000 + Calendar.getInstance().getTimeInMillis());
                    SPUtils.put(fragment.getActivity(), "WX_OPENID", wxAccessToken.getOpenid());

                    Intent intent = new Intent("loginSuccess");
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                    fragment.getActivity().startService(new Intent(fragment.getActivity(), InitDataService.class));

                    fragment.getActivity().startActivity(MainActivity.newIntent(fragment.getActivity()));
                    fragment.getActivity().finish();
                    fragment.getActivity().overridePendingTransition(0, R.anim.anim_right_out);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
