package com.nong.nongo2o.module.share.viewModel;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.share.fragment.ShareDetailFragment;

/**
 * Created by Administrator on 2017-7-3.
 */

public class ShareDetailVM implements ViewModel {

    private ShareDetailFragment fragment;

    public ShareDetailVM(ShareDetailFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * 查看作者主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 查看所有评论
     */
    public final ReplyCommand commentDetailClick = new ReplyCommand(() -> {
//        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
//                DynamicCommentFragment.newInstance(), DynamicCommentFragment.TAG);
    });
}
