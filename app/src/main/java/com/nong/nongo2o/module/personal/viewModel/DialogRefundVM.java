package com.nong.nongo2o.module.personal.viewModel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.entity.domain.Order;

import java.math.BigDecimal;

/**
 * Created by PANYJ7 on 2018-1-10.
 */

public class DialogRefundVM implements ViewModel {

    private Context context;
    private AlertDialog dialog;
    private DialogRefundListener listener;

    public final ObservableField<BigDecimal> total = new ObservableField<>();
    public final ObservableField<String> refund = new ObservableField<>();
    public final ObservableField<String> applyReason = new ObservableField<>();
    public final ObservableField<String> reason = new ObservableField<>();

    public interface DialogRefundListener {
        void confirmClick(String reason, BigDecimal money);
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isSaler = new ObservableBoolean(false);
        public final ObservableBoolean isAgree = new ObservableBoolean(false);
    }

    public DialogRefundVM(Context context, AlertDialog dialog, DialogRefundListener listener, Order order, boolean isSaler, boolean isAgree, String applyReason) {
        this.context = context;
        this.dialog = dialog;
        this.listener = listener;

        total.set(order.getTotalPrice());
        viewStyle.isSaler.set(isSaler);
        viewStyle.isAgree.set(isAgree);
        this.applyReason.set(applyReason);
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
        if (TextUtils.isEmpty(reason.get())) {
            Toast.makeText(context, "请输入申请退款的原因", Toast.LENGTH_SHORT).show();
            return;
        }

        if (viewStyle.isSaler.get() && viewStyle.isAgree.get()) {
            if (TextUtils.isEmpty(refund.get()) || Float.parseFloat(refund.get()) <= 0) {
                Toast.makeText(context, "请输入退款金额", Toast.LENGTH_SHORT).show();
                return;
            }

            if (new BigDecimal(refund.get()).compareTo(total.get()) == 1) {
                Toast.makeText(context, "退款金额大于订单金额了", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (listener != null) {
            if (viewStyle.isSaler.get() && viewStyle.isAgree.get()) {
                listener.confirmClick(reason.get(), new BigDecimal(refund.get()));
            } else {
                listener.confirmClick(reason.get(), new BigDecimal(0));
            }
        }

        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}

