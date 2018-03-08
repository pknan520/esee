package com.nong.nongo2o.module.personal.viewModel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entities.request.AddFocus;
import com.nong.nongo2o.entities.response.Fans;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.entity.domain.Follow;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.FocusUtils;
import com.nong.nongo2o.uils.imUtils.IMUtils;

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

                viewStyle.hasFocus.set(FocusUtils.checkIsFocus(follow.getUserCode()));
                break;
        }
    }

    /**
     * 查看个人主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), status == FansMgrActivity.MY_FOCUS ? follow.getTarget() : follow.getUser()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public final ReplyCommand contactClick = new ReplyCommand(() -> {
        IMUtils.checkIMLogin(isSuccess -> {
            if (isSuccess) {
                String userName = status == FansMgrActivity.MY_FOCUS ? follow.getTarget().getId() : follow.getUser().getId();
                if (userName.equals(EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(fragment.getActivity(), "您不能自言自语了啦^.^", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
                intent.putExtra("userId", userName);
                fragment.getActivity().startActivity(intent);
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            } else {
                Toast.makeText(fragment.getActivity(), "聊天可能有点问题，请稍候再试", Toast.LENGTH_SHORT).show();
            }
        });
    });

    public final ReplyCommand focusOrNotClick = new ReplyCommand(() -> {
        switch (status) {
            case FansMgrActivity.MY_FOCUS:
                FocusUtils.changeFocus(fragment.getActivity(), viewStyle.hasFocus.get(), follow.getTargetCode(), viewStyle.hasFocus::set);
                break;
            case FansMgrActivity.MY_FANS:
                FocusUtils.changeFocus(fragment.getActivity(), viewStyle.hasFocus.get(), follow.getUserCode(), viewStyle.hasFocus::set);
                break;
        }
    });

}
