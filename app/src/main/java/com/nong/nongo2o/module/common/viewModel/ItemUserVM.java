package com.nong.nongo2o.module.common.viewModel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.uils.FocusUtils;
import com.nong.nongo2o.uils.imUtils.IMUtils;

/**
 * Created by PANYJ7 on 2018-3-5.
 */

public class ItemUserVM implements ViewModel {

    private RxBaseFragment fragment;
    private SimpleUser user;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    @DrawableRes
    public final int isFocus = R.mipmap.icon_focus_p;
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus;

    public ItemUserVM(RxBaseFragment fragment, SimpleUser user) {
        this.fragment = fragment;
        this.user = user;

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
        headUri.set(user.getAvatar());
        name.set(user.getUserNick());
        summary.set(user.getProfile());

        viewStyle.hasFocus.set(FocusUtils.checkIsFocus(user.getUserCode()));
    }

    /**
     * 查看个人主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), user));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public final ReplyCommand contactClick = new ReplyCommand(() -> {
        IMUtils.checkIMLogin(isSuccess -> {
            if (isSuccess) {
                String userName = user.getId();
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
        FocusUtils.changeFocus(fragment.getActivity(), viewStyle.hasFocus.get(), user.getUserCode(), viewStyle.hasFocus::set);
    });
}
