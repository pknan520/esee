package com.nong.nongo2o.widget.ScrollView;

import android.databinding.BindingAdapter;
import android.support.v4.widget.NestedScrollView;

import com.kelin.mvvmlight.command.ReplyCommand;

/**
 * Created by Administrator on 2017-9-13.
 */

public class ViewBindingAdapter {

    @BindingAdapter({"onScrollBottomCommand"})
    public static void onScrollBottomCommand(NestedScrollView view, ReplyCommand onScrollBottomCommand) {
        if (view != null) {
            view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        try {
                            onScrollBottomCommand.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
}
