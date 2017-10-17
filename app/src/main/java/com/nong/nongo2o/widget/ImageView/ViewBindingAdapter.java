package com.nong.nongo2o.widget.ImageView;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.uils.AppManager;
import com.nong.nongo2o.uils.MyFrescoImageLoader;

/**
 * Created by Administrator on 2017-7-18.
 */

public class ViewBindingAdapter {

    @BindingAdapter(value = {"glideUri", "placeholderImageRes"}, requireAll = false)
    public static void glideLoadImage(final ImageView iv, String uri, @DrawableRes int placeholderImageRes) {
        Glide.with(AppManager.getAppManager().currentActivity())
                .load(uri)
                .placeholder(placeholderImageRes)
                .into(iv);
    }

    @BindingAdapter(value = {"uri", "placeholderImageRes", "request_width", "request_height", "onSuccessCommand", "onFailureCommand"}, requireAll = false)
    public static void loadImage(final SimpleDraweeView imageView, String uri,
                                 @DrawableRes int placeholderImageRes,
                                 int width, int height,
                                 final ReplyCommand<Bitmap> onSuccessCommand,
                                 final ReplyCommand<DataSource<CloseableReference<CloseableImage>>> onFailureCommand) {
        imageView.setImageResource(placeholderImageRes);
        if (!TextUtils.isEmpty(uri)) {
            MyFrescoImageLoader.setImageSmall(uri, imageView, width, height);
//            ImagePipeline imagePipeline = Fresco.getImagePipeline();
//            ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri));
//            if (width > 0 && height > 0) {
//                builder.setResizeOptions(new ResizeOptions(width, height));
//            }
//            ImageRequest request = builder.build();
//            DataSource<CloseableReference<CloseableImage>>
//                    dataSource = imagePipeline.fetchDecodedImage(request, imageView.getContext());
//            dataSource.subscribe(new BaseBitmapDataSubscriber() {
//                @Override
//                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
//                    if (onFailureCommand != null) {
//                        try {
//                            onFailureCommand.execute(dataSource);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                protected void onNewResultImpl(Bitmap bitmap) {
//                    imageView.setImageBitmap(bitmap);
//                    if (onSuccessCommand != null) {
//                        try {
//                            onSuccessCommand.execute(bitmap);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }, UiThreadImmediateExecutorService.getInstance());
        }
    }
}
