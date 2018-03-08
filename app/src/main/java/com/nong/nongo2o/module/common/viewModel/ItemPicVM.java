package com.nong.nongo2o.module.common.viewModel;

import android.Manifest;
import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
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
    private int maxSize = 1;
    private ClickListener clickListener;
    private String uri;
    private ObservableList<ItemPicVM> list;

    @DrawableRes
    public final int placeHolder = R.mipmap.add_picture;
    public final ObservableField<String> imgUri = new ObservableField<>();

    public interface ClickListener {
//        void addRadioPic(MediaBean mediaBean);
        void addMultiPic(List<MediaBean> mediaBeanList);
        void removePic(ItemPicVM itemPicVM);
    }

    public ItemPicVM(Activity context, String uri, ClickListener clickListener, ObservableList<ItemPicVM> list) {
        this.context = context;
        this.uri = uri;
        this.clickListener = clickListener;
        this.list = list;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean canDelete = new ObservableBoolean(false);
    }

    private void initData() {
        imgUri.set(uri);
        viewStyle.canDelete.set(!TextUtils.isEmpty(uri));
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
        maxSize = 1 - (TextUtils.isEmpty(list.get(list.size() - 1).uri) ? list.size() - 1 : list.size());
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
                        clickListener.addMultiPic(event.getResult());
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
