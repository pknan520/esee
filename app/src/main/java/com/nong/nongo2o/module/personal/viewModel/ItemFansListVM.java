package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entities.request.AddFocus;
import com.nong.nongo2o.entities.response.Fans;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.entity.domain.Follow;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-7-17.
 */

public class ItemFansListVM implements ViewModel {

    private RxBaseFragment fragment;
    private Follow follow;
    private int status;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    @DrawableRes
    public final int isFocus = R.mipmap.icon_focus_p;
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus;

    public ItemFansListVM(RxBaseFragment fragment, Follow follow, int status) {
        this.fragment = fragment;
        this.follow = follow;
        this.status = status;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean hasFocus = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        switch (status) {
            case FansMgrActivity.MY_FOCUS:
                //  我关注的
                headUri.set(follow.getTarget().getAvatar());
                name.set(follow.getTarget().getUserNick());
                summary.set(follow.getTarget().getProfile());
                viewStyle.hasFocus.set(true);
                break;
            case FansMgrActivity.MY_FANS:
                //  我的粉丝
                headUri.set(follow.getUser().getAvatar());
                name.set(follow.getUser().getUserNick());
                summary.set(follow.getUser().getProfile());
                //-TODO
//                viewStyle.hasFocus.set(fans.getEachOther() != null && fans.getEachOther() == 1);
                break;
        }
    }

    /**
     * 查看个人主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public final ReplyCommand focusOrNotClick = new ReplyCommand(() -> {
        switch (status) {
            case FansMgrActivity.MY_FOCUS:
                //  我关注的点击取消关注
                focusOrNot(follow.getTargetCode());
                break;
            case FansMgrActivity.MY_FANS:
                //  我的粉丝点，未关注的点击关注，已关注的点击取消关注
                focusOrNot(follow.getUserCode());
                break;
        }
    });

    private void focusOrNot(String userCode) {
        if (viewStyle.hasFocus.get()) {
            //  已关注的，点击取消关注
            RetrofitHelper.getUserAPI()
                    .deleteFocus(User.getInstance().getUserCode(), userCode)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_SHORT).show();
                        viewStyle.hasFocus.set(false);
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(new AddFocus(User.getInstance().getUserCode(), userCode)));
            //  未关注的，点击关注
            RetrofitHelper.getUserAPI()
                    .focus(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Toast.makeText(fragment.getActivity(), s, Toast.LENGTH_SHORT).show();
                        viewStyle.hasFocus.set(true);
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

}
