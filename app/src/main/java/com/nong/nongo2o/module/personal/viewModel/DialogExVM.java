package com.nong.nongo2o.module.personal.viewModel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by Administrator on 2017-9-22.
 */

public class DialogExVM implements ViewModel {

    private Context context;
    private AlertDialog dialog;
    private DialogExListener listener;

    public final ObservableBoolean isSelf = new ObservableBoolean(true);
    public final ObservableBoolean isEx = new ObservableBoolean(false);

    public final ObservableField<String> exName = new ObservableField<>();
    public final ObservableField<String> exNumber = new ObservableField<>();
    public final ObservableField<String> remark = new ObservableField<>();

    public interface DialogExListener {
        void confirmClick(int type, String exName, String exNumber, String remark);
    }

    public DialogExVM(Context context, AlertDialog dialog, DialogExListener listener) {
        this.context = context;
        this.dialog = dialog;
        this.listener = listener;
    }

    /**
     * 取消按键
     */
    public final ReplyCommand cancelClick = new ReplyCommand(this::cancelDialog);

    private void cancelDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    /**
     * 确定按键
     */
    public final ReplyCommand confirmClick = new ReplyCommand(this::confirm);

    private void confirm() {
        if (!isSelf.get() && (TextUtils.isEmpty(exName.get()) || TextUtils.isEmpty(exNumber.get()))) {
            Toast.makeText(context, "请输入完整的快递信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (listener != null) {
            listener.confirmClick(isSelf.get() ? 1 : 0, exName.get(), exNumber.get(), remark.get());
        }

        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
