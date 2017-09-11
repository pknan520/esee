package com.nong.nongo2o.module.login.viewModel;

import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.entities.request.UpdateUser;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.module.login.fragment.LoginFragment;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
                .login("user123", "e10adc3949ba59abbe56e057f20f883e")
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    User.getInstance().setUser(user);
                    fragment.getActivity().startActivity(MainActivity.newIntent(fragment.getActivity()));
                    fragment.getActivity().finish();
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public final ReplyCommand testClick = new ReplyCommand(this::test);

    private void test() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(new UpdateUser("51", "user123", "askdjaskldjlaskdj")));

        RetrofitHelper.getUserAPI()
                .updateUser(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {

                }, throwable -> {});
    }
}
