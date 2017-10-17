package com.nong.nongo2o.module.personal.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentInviteBinding;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.personal.activity.InviteActivity;
import com.nong.nongo2o.module.personal.viewModel.InviteVM;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by Administrator on 2017-9-26.
 */

public class InviteFragment extends RxBaseFragment {

    public static final String TAG = "InviteFragment";
    public static final int SCAN_CODE = 100, REQ_PERMISSION = 101;

    private FragmentInviteBinding binding;
    private InviteVM vm;

    public static InviteFragment newInstance() {
        return new InviteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) vm = new InviteVM(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInviteBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((InviteActivity) getActivity()).setToolbarTitle("我的邀请码");

        TextPaint tp = binding.tvInviteCode.getPaint();
        tp.setFakeBoldText(true);

        if (!TextUtils.isEmpty(UserInfo.getInstance().getInviteCode())) {
            binding.ivCode.setImageBitmap(CodeUtils.createImage(UserInfo.getInstance().getInviteCode(), 300, 300, null));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SCAN_CODE:
                if (resultCode == -1 && data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) return;
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS)
                        vm.setInviteInput(bundle.getString(CodeUtils.RESULT_STRING));
                    else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED)
                        Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
