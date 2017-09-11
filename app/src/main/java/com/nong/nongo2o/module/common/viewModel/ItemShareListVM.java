package com.nong.nongo2o.module.common.viewModel;

import android.app.Fragment;
import android.databinding.ObservableField;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.share.activity.ShareDetailActivity;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-7-3.
 */

public class ItemShareListVM implements ViewModel {

    private Fragment fragment;

    public final ObservableField<String> str = new ObservableField<>();

    public ItemShareListVM(Fragment fragment, String str) {
        this.fragment = fragment;
        this.str.set(str);
    }

    /**
     * 查看详情
     */
    public final ReplyCommand detailClick = new ReplyCommand(new Action() {
        @Override
        public void run() throws Exception {
            fragment.getActivity().startActivity(ShareDetailActivity.newIntent(fragment.getActivity()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        }
    });
}
