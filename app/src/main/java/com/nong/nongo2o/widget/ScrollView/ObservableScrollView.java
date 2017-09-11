package com.nong.nongo2o.widget.ScrollView;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017-7-4.
 */

public class ObservableScrollView extends NestedScrollView {

    private ScrollViewListener mListener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChanged(this, l, t, oldl, oldt);
        }

        if (t + getHeight() >= getChildAt(0).getMeasuredHeight()) {
            if (mListener != null) {
                mListener.onScrollBottom();
            }
        }
    }
}
