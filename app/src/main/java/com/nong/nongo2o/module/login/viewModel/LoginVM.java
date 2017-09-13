package com.nong.nongo2o.module.login.viewModel;

import android.util.Log;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.login.fragment.LoginFragment;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-8-4.
 */

public class LoginVM implements ViewModel {

    private LoginFragment fragment;

    public LoginVM(LoginFragment fragment) {
        this.fragment = fragment;
    }

    public final ReplyCommand loginClick = new ReplyCommand(this::login);

    private void login() {

        RetrofitHelper.getAccountAPI()
                .login("123456789abc")
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    UserInfo.setOurInstance(new UserInfo(user));
                    fragment.getActivity().startActivity(MainActivity.newIntent(fragment.getActivity()));
                    fragment.getActivity().finish();
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public final ReplyCommand testClick = new ReplyCommand(this::test);

    private void test() {

    }
}
