package com.nong.nongo2o.uils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by Administrator on 2017-7-18.
 */

public interface AbsImageLoader {
    void displayImage(Context context,
                      String path,
                      FixImageView imageView,
                      Drawable defaultDrawable,
                      Bitmap.Config config,
                      boolean resize,
                      boolean isGif,
                      int width,
                      int height,
                      int rotate);
}
