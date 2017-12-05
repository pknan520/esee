package com.nong.nongo2o.module.common.viewModel;

import android.Manifest;
import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.model.AspectRatio;

import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-7-1.
 */

public class ItemPicVM implements ViewModel {

    private static final String TAG = "ItemPicVM";

    private Activity context;
    private int maxSize = 9;
    private ClickListener clickListener;

    @DrawableRes
    public final int placeHolder = R.mipmap.add_picture;
    public final ObservableField<String> imgUri = new ObservableField<>();

    public interface addMultiListener {
        void addMultiPic(ImageMultipleResultEvent event);
    }

    public interface ClickListener {
        void addRadioPic(MediaBean mediaBean);
        void removePic(ItemPicVM itemPicVM);
    }

    public ItemPicVM(Activity context, String uri, ClickListener clickListener) {
        this.context = context;
        imgUri.set(uri);
        this.clickListener = clickListener;

        viewStyle.canDelete.set(!TextUtils.isEmpty(uri));
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean canDelete = new ObservableBoolean(false);
    }

    /**
     * 添加图片
     */
    public final ReplyCommand addPicClick = new ReplyCommand(() -> {
        if (TextUtils.isEmpty(imgUri.get())) {
            new RxPermissions(context)
                    .request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            addPicFromGallery();
                        } else {
                            Toast.makeText(context, "拒绝权限将不能打开相册", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    });

    /**
     * 从相册获取
     */
    private void addPicFromGallery() {
        RxGalleryFinal
                .with(context)
                .image()
                .multiple()
                .maxSize(maxSize)
                .cropAspectRatioOptions(0, new AspectRatio("3.3", 30, 10))
                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent event) throws Exception {
                        maxSize -= event.getResult().size();

                        for (int i = 0; i < event.getResult().size(); i++) {
                            MediaBean bean = event.getResult().get(i);
                            clickListener.addRadioPic(bean);
                        }
                    }
                })
                .openGallery();

//        RxGalleryFinalApi
//                .getInstance(context)
//                .setType(RxGalleryFinalApi.SelectRXType.TYPE_IMAGE, RxGalleryFinalApi.SelectRXType.TYPE_SELECT_MULTI)
//                .onCrop(true)
//                .openGalleryRadioImgDefault(new RxBusResultSubscriber() {
//                    @Override
//                    protected void onEvent(Object o) throws Exception {
//
//                    }
//                })
//                .onCropImageResult(new IRadioImageCheckedListener() {
//                    @Override
//                    public void cropAfter(Object t) {
//                        Log.d(TAG, "cropAfter: ");
//                    }
//
//                    @Override
//                    public boolean isActivityFinish() {
//                        Log.d(TAG, "isActivityFinish: ");
//                        return true;
//                    }
//                })
//                .open();
    }

    /**
     * 删除图片
     */
    public final ReplyCommand deleteClick = new ReplyCommand(() -> {
        if (clickListener != null) clickListener.removePic(this);
    });

}
