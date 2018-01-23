package com.nong.nongo2o.module.personal.viewModel;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.personal.fragment.InviteFragment;
import com.nong.nongo2o.module.personal.fragment.MyInvitersFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-9-26.
 */

public class InviteVM implements ViewModel {

    private InviteFragment fragment;

    public final ObservableField<String> inviteCode = new ObservableField<>();
    public final ObservableField<String> inviteInput = new ObservableField<>();
    public final ObservableField<String> inviter = new ObservableField<>();

    public InviteVM(InviteFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean confirmClickable = new ObservableBoolean(false);
        public final ObservableBoolean hasBind = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        inviteCode.set(UserInfo.getInstance().getInviteCode());

        viewStyle.hasBind.set(!TextUtils.isEmpty(UserInfo.getInstance().getInviter()));
        if (viewStyle.hasBind.get()) inviter.set(UserInfo.getInstance().getInviter());
    }

    public void setInviteInput(String str) {
        inviteInput.set(str);
    }

    public final ReplyCommand<String> afterInputChange = new ReplyCommand<>(s -> {
        viewStyle.confirmClickable.set(!TextUtils.isEmpty(s));
    });

    /**
     * 复制
     */
    public final ReplyCommand copyClick = new ReplyCommand(() -> {
        ClipboardManager cm = (ClipboardManager) fragment.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("inviteCode", inviteCode.get());
        cm.setPrimaryClip(cd);
        Toast.makeText(fragment.getActivity(), "邀请码已复制到剪切板", Toast.LENGTH_SHORT).show();
    });

    /**
     * 开启扫描
     */
    public final ReplyCommand scanClick = new ReplyCommand(this::checkCameraPermission);

    private void checkCameraPermission() {
        new RxPermissions(fragment.getActivity())
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        fragment.startActivityForResult(new Intent(fragment.getActivity(), CaptureActivity.class), InviteFragment.SCAN_CODE);
                    } else {
                        Toast.makeText(fragment.getActivity(), "拒绝权限将不能打开二维码扫描", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 确认邀请码
     */
    public final ReplyCommand inviteClick = new ReplyCommand(() -> {
        RetrofitHelper.getUserAPI()
                .bindInviteCode(inviteInput.get())
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    viewStyle.hasBind.set(true);
                    inviter.set(inviteInput.get());
                    UserInfo.getInstance().setInviter(inviter.get());
                    Toast.makeText(fragment.getActivity(), "绑定邀请码成功", Toast.LENGTH_SHORT).show();
                }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    });

    /**
     * 查看我邀请的人
     */
    public final ReplyCommand checkMyInviters = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                MyInvitersFragment.newInstance(), MyInvitersFragment.TAG);
    });
}
