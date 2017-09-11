package com.nong.nongo2o.module.common.viewModel;

import android.Manifest;
import android.app.Activity;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.model.AspectRatio;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;

/**
 * Created by Administrator on 2017-7-1.
 */

public class ItemPicVM implements ViewModel {

    private static final String TAG = "ItemPicVM";

    private Activity context;
    private int maxSize = 9;
    private addRadioPicListener addPicListener;

    @DrawableRes
    public final int placeHolder = R.mipmap.add_picture;
    public final ObservableField<String> imgUri = new ObservableField<>();

    public interface addMultiListener {
        void addMultiPic(ImageMultipleResultEvent event);
    }

    public interface addRadioPicListener {
        void addRadioPic(MediaBean mediaBean);
    }

    public ItemPicVM(Activity context, String uri) {
        new ItemPicVM(context, uri, null);
    }

    public ItemPicVM(Activity context, String uri, addRadioPicListener addPicListener) {
        this.context = context;
        imgUri.set(uri);
        this.addPicListener = addPicListener;
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
                            addPicListener.addRadioPic(bean);
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

}
