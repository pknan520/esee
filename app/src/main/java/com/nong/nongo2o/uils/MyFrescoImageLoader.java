package com.nong.nongo2o.uils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import cn.finalteam.rxgalleryfinal.imageloader.AbsImageLoader;
import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by Administrator on 2017-7-18.
 */

public class MyFrescoImageLoader implements AbsImageLoader {

    private DraweeHolder<GenericDraweeHierarchy> draweeHolder;

    public static void setImageSmall(String url, SimpleDraweeView simpleDraweeView, int width, int height) {

        Uri uri = Uri.parse(url);

        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        if (width > 0 && height > 0) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.setRotationOptions(RotationOptions.autoRotate())
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();

        DraweeController controller;
        if (simpleDraweeView.getScaleType().equals(ImageView.ScaleType.FIT_XY)) {
            ControllerListener listener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                    updateViewSize(simpleDraweeView, imageInfo);
                }

                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    updateViewSize(simpleDraweeView, imageInfo);
                }
            };
            controller = Fresco.newDraweeControllerBuilder()
                    .setTapToRetryEnabled(true)
                    .setImageRequest(request)
                    .setControllerListener(listener)
                    .build();
        } else {
            controller = Fresco.newDraweeControllerBuilder()
                    .setTapToRetryEnabled(true)
                    .setImageRequest(request)
                    .setOldController(simpleDraweeView.getController())
                    .build();
        }

        simpleDraweeView.setController(controller);
    }

    private static void updateViewSize(SimpleDraweeView view, @Nullable ImageInfo imageInfo) {
        if (imageInfo != null) {
            view.setAspectRatio((float) imageInfo.getWidth() / imageInfo.getHeight());
        }
    }

    private void init(Context ctx, Drawable defaultDrawable) {
        if (draweeHolder == null) {
            Resources resources = ctx.getResources();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                    .setPlaceholderImage(defaultDrawable)
                    .setFailureImage(defaultDrawable)
                    .build();
            draweeHolder = DraweeHolder.create(hierarchy, ctx);
        }
    }

    @Override
    public void displayImage(Context context,
                             String path,
                             FixImageView imageView,
                             Drawable defaultDrawable,
                             Bitmap.Config config,
                             boolean resize, boolean isGif,
                             int width,
                             int height,
                             int rotate) {
        init(context, defaultDrawable);

        imageView.setOnImageViewListener(new FixImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                return dr == draweeHolder.getHierarchy().getTopLevelDrawable();
            }

            @Override
            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageDrawable(defaultDrawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return draweeHolder.onTouchEvent(event);
            }
        });
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true);
        if (resize) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeHolder.getController())
                .setImageRequest(request)
                .build();
        draweeHolder.setController(controller);
    }
}