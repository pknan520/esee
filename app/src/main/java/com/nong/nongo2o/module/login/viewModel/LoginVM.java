package com.nong.nongo2o.module.login.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.AdventurerApp;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.response.WxAccessToken;
import com.nong.nongo2o.entity.response.WxInfo;
import com.nong.nongo2o.module.login.fragment.BindMobileFragment;
import com.nong.nongo2o.module.login.fragment.LoginFragment;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.service.InitDataService;
import com.nong.nongo2o.uils.Constant;
import com.nong.nongo2o.uils.SPUtils;
import com.nong.nongo2o.wxapi.WXEntryActivity;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-8-4.
 */

public class LoginVM implements ViewModel {

    private LoginFragment fragment;
    private boolean canClose;

    public LoginVM(LoginFragment fragment, boolean canClose) {
        this.fragment = fragment;
        this.canClose = canClose;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean canClose = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        viewStyle.canClose.set(canClose);
    }

    /**
     * 关闭按钮
     */
    public final ReplyCommand closeClick = new ReplyCommand(this::close);

    private void close() {
        fragment.getActivity().finish();
        fragment.getActivity().overridePendingTransition(0, R.anim.anim_right_out);
    }

    /**
     * 登录按钮
     */
    public final ReplyCommand loginClick = new ReplyCommand(this::loginWeixin);

    private void loginWeixin() {
        ((RxBaseActivity) fragment.getActivity()).showLoading();
        WXEntryActivity.loginWeixin(fragment.getActivity(), AdventurerApp.wxApi);
    }

    /**
     * 体验按钮
     */
    public final ReplyCommand experienceClick = new ReplyCommand(new Action() {
        @Override
        public void run() throws Exception {
            fragment.getActivity().startActivity(MainActivity.newIntent(fragment.getActivity()));
            fragment.getActivity().finish();
        }
    });

    /**
     * 测试按钮
     */
    public final ReplyCommand testClick = new ReplyCommand(this::test);

    private void test() {
        final Intent intent = new Intent();

        RetrofitHelper.getAccountAPI()
                .login("246810abc566")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResp -> {
                    if (userResp.getCode().equals("0")) {
                        intent.setAction("loginSuccess");
                        UserInfo.setOurInstance(new UserInfo(userResp.getData()));
                    } else {
                        intent.setAction("loginFail");
                    }
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
