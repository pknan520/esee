package com.nong.nongo2o.widget.checkbox;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by Administrator on 2017-8-22.
 */

public class ViewBindingAdapter {

    @BindingAdapter(value = {"onCheckChange"}, requireAll = false)
    public static void checkChangeListener(CheckBox checkBox, ReplyCommand<OnCheckChange> onCheckChangCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CheckBox", "onCheckedChanged: " + isChecked);
                if (onCheckChangCommand != null) {
                    try {
                        onCheckChangCommand.execute(new OnCheckChange(checkBox, isChecked));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static class OnCheckChange {

        public CheckBox cb;
        public boolean isChecked;

        public OnCheckChange(CheckBox cb, boolean isChecked) {
            this.cb = cb;
            this.isChecked = isChecked;
        }
    }
}
