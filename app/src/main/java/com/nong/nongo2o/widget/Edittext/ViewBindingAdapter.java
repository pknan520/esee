package com.nong.nongo2o.widget.Edittext;

import android.databinding.BindingAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by PANYJ7 on 2018-1-12.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"isEditable"})
    public static void setIsEditable(EditText editText, boolean editable) {
        editText.setFocusable(editable);
    }
}
