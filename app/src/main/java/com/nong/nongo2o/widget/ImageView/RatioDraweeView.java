package com.nong.nongo2o.widget.ImageView;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2017-7-13.
 */

public class RatioDraweeView extends SimpleDraweeView {

    private int originalWidth;
    private int originalHeight;

    public RatioDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public RatioDraweeView(Context context) {
        super(context);
    }

    public RatioDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RatioDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOriginalSize(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalWidth > 0 && originalHeight > 0) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            // TODO: 现在只支持固定宽度
            if (width > 0) {
                height = (int) ((float) width / ratio);
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
