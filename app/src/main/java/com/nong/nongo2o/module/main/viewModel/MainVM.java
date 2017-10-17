package com.nong.nongo2o.module.main.viewModel;

import android.text.TextUtils;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.service.VersionUpdateHelper;

/**
 * Created by Administrator on 2017-9-13.
 */

public class MainVM implements ViewModel {

    private MainActivity activity;

    public MainVM(MainActivity activity) {
        //初始数据
//        activity.startService(new Intent(activity, InitDataService.class));

        if (!TextUtils.isEmpty(UserInfo.getInstance().getId()) && !TextUtils.isEmpty(UserInfo.getInstance().getUserCode()) && !EMClient.getInstance().isLoggedInBefore()) {
            EMClient.getInstance().login(String.valueOf(UserInfo.getInstance().getId()), UserInfo.getInstance().getUserCode(), new EMCallBack() {
                @Override
                public void onSuccess() {
                    EMClient.getInstance().chatManager().loadAllConversations();
                }

                @Override
                public void onError(int code, String error) {
                    Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(int progress, String status) {

                }
            });
        }

        new VersionUpdateHelper(activity).startUpdateVersion();
    }
}
