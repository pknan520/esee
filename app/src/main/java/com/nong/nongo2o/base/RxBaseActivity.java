package com.nong.nongo2o.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nong.nongo2o.R;
import com.nong.nongo2o.uils.AppManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

/**
 * Created by Administrator on 2017-6-21.
 */

public class RxBaseActivity extends RxAppCompatActivity {

    private AlertDialog deleteDialog;
    private Dialog loadingDialog;
    private AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        initLoadingDialog();
    }

    public void replaceFragment(int containerId, Fragment fragment, String tag) {
        if (getFragmentManager().findFragmentByTag(tag) == null) {
            getFragmentManager().beginTransaction()
                    .replace(containerId, fragment, tag)
                    .commit();
        }
    }

    public void switchFragment(int containerId, Fragment from, Fragment to, String tag) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_right_out,
                R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_right_out);

        if (to.isAdded()) {
            ft.hide(from).show(to).commit();
        } else {
            ft.hide(from).add(containerId, to, tag).addToBackStack(null).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 删除确认dialog
     */
    public interface deleteDialogClickListener {
        void confirmDelete();
    }

    public void showDeleteDialog(deleteDialogClickListener listener) {
        initDeleteDialog(listener);
        if (deleteDialog != null && !deleteDialog.isShowing()) {
            deleteDialog.show();
        }
    }

    private void initDeleteDialog(deleteDialogClickListener listener) {
        deleteDialog = null;
        deleteDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认删除？")
                .setNegativeButton("取消", (dialog, which) -> deleteDialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    if (listener != null) {
                        listener.confirmDelete();
                    }
                    deleteDialog.dismiss();
                })
                .create();
    }

    /**
     * loadingDialog
     */
    private void initLoadingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.AVLoadingIndicatorView);
//        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
//        loadingText.setText(msg);
        loadingDialog = new Dialog(this, R.style.loading_dialog_style);
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        mLoadingDialog.show();
//        avLoadingIndicatorView.smoothToShow();
//        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    loadingDialog.hide();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    public void showLoading() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
            avLoadingIndicatorView.smoothToShow();
        }
    }

    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            avLoadingIndicatorView.smoothToHide();
        }
    }

}
