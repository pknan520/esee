package com.nong.nongo2o.module.message.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.exceptions.HyphenateException;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.EaseUserInfo;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.greenDaoGen.EaseUserInfoDao;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AppManager;
import com.nong.nongo2o.uils.dbUtils.GreenDaoManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-9-26.
 */

public class ChartFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private EaseUserInfoDao easeDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        easeDao = GreenDaoManager.getInstance().getSession().getEaseUserInfoDao();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        setChatFragmentHelper(this);
        titleBar.setLeftImageResource(R.mipmap.back_white);
        titleBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        String userId = getArguments().getString("userId");
        if (!TextUtils.isEmpty(userId)) {
            EaseUserInfo info = easeDao.load(userId);
            if (info != null) {
                titleBar.setTitle(info.getUserNick());
            } else {
                RetrofitHelper.getUserAPI()
                        .profile(userId)
                        .subscribeOn(Schedulers.io())
                        .map(new ApiResponseFunc<>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            EaseUserInfo newInfo = new EaseUserInfo();
                            newInfo.setUserId(userId);
                            newInfo.setUserNick(user.getUserNick());
                            newInfo.setAvatar(user.getAvatar());
                            easeDao.insertOrReplace(newInfo);

                            titleBar.setTitle(user.getUserNick());
                        }, throwable -> Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }

        //  电话信息
        RetrofitHelper.getUserAPI()
                .profile(userId)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (user.getUserType() == 1 && !TextUtils.isEmpty(user.getPhone())) {
                        titleBar.setRightLayoutVisibility(View.VISIBLE);
                        titleBar.setRightImageResource(R.mipmap.bar_call);
                        titleBar.setRightLayoutClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhone()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
                    } else {
                        titleBar.setRightLayoutVisibility(View.GONE);
                    }
                }, throwable -> Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage msg : messages) {
            EaseUserInfo info = new EaseUserInfo();
            info.setUserId(getArguments().getString("userId"));
            info.setUserNick(msg.getStringAttribute("userNick", ""));
            info.setAvatar(msg.getStringAttribute("avatar", ""));
            easeDao.insertOrReplace(info);
        }
        super.onMessageReceived(messages);
    }

    @Override
    protected void registerExtendMenuItem() {
        super.registerExtendMenuItem();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        //设置要发送扩展消息用户编号
        message.setAttribute("userCode", UserInfo.getInstance().getUserCode());
        //设置要发送扩展消息用户昵称
        message.setAttribute("userNick", UserInfo.getInstance().getUserNick());
        //设置要发送扩展消息用户头像
        message.setAttribute("avatar", UserInfo.getInstance().getAvatar());
    }

    @Override
    public void onEnterToChatDetails() {

    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }
}
