package com.nong.nongo2o.module.personal.viewModel;

import android.Manifest;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;

/**
 * Created by Administrator on 2017-6-30.
 */

public class IdentifyVM implements ViewModel {

    private IdentifyActivity activity;

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> idNo = new ObservableField<>();
    @DrawableRes
    public final int idPicPlaceHolder1 = R.mipmap.idnumber_face;
    @DrawableRes
    public final int idPicPlaceHolder2 = R.mipmap.idnumber_reverseside;
    public final ObservableField<String> idPic1 = new ObservableField<>();
    public final ObservableField<String> idPic2 = new ObservableField<>();

    public IdentifyVM(IdentifyActivity activity) {
        this.activity = activity;
    }

    /**
     * 身份证照片正面
     */
    public final ReplyCommand idFaceClick = new ReplyCommand(() -> checkCameraPermissions(idPic1));

    /**
     * 身份证照片反面
     */
    public final ReplyCommand idReverssideClick = new ReplyCommand(() -> checkCameraPermissions(idPic2));

    private void checkCameraPermissions(ObservableField<String> picUri) {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        selectPic(picUri);
                    } else {
                        Toast.makeText(activity, "拒绝权限将不能打开相册", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selectPic(ObservableField<String> picUri) {
        RxGalleryFinal
                .with(activity)
                .image()
                .radio()
//                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent event) throws Exception {
                        picUri.set("file://" + event.getResult().getOriginalPath());
                    }
                })
                .openGallery();
    }

    /**
     * 提交认证
     */
    public final ReplyCommand submitClick = new ReplyCommand(() -> {

    });
}
