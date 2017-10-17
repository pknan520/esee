package com.nong.nongo2o.module.personal.viewModel;

import android.Manifest;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.FileResponse;
import com.nong.nongo2o.entity.request.UpdateUserRequest;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.R.attr.fragment;

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

    private Map<Integer, File> idPicMap;

    public IdentifyVM(IdentifyActivity activity) {
        this.activity = activity;

        idPicMap = new HashMap<>();
    }

    /**
     * 身份证照片正面
     */
    public final ReplyCommand idFaceClick = new ReplyCommand(() -> checkCameraPermissions(idPic1, 1));

    /**
     * 身份证照片反面
     */
    public final ReplyCommand idReverssideClick = new ReplyCommand(() -> checkCameraPermissions(idPic2, 2));

    private void checkCameraPermissions(ObservableField<String> picUri, int pos) {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        selectPic(picUri, pos);
                    } else {
                        Toast.makeText(activity, "拒绝权限将不能打开相册", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selectPic(ObservableField<String> picUri, int pos) {
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
                        idPicMap.put(pos, new File(event.getResult().getOriginalPath()));
                    }
                })
                .openGallery();
    }

    /**
     * 提交认证
     */
    public final ReplyCommand submitClick = new ReplyCommand(() -> {
        if (TextUtils.isEmpty(name.get()) || TextUtils.isEmpty(idNo.get())) {
            Toast.makeText(activity, "请填写完整的身份资料", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idPicMap.size() < 2) {
            Toast.makeText(activity, "请上传身份证照片", Toast.LENGTH_SHORT).show();
            return;
        }

        activity.showLoading();

        Map<String, RequestBody> picMap = new LinkedHashMap<>();
        for (Integer key : idPicMap.keySet()) {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), idPicMap.get(key));
            picMap.put(idPicMap.get(key).getName(), body);
        }
        if (picMap.size() > 1) {
            RetrofitHelper.getFileAPI()
                    .uploadFile(picMap)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .flatMap(s -> {
                        List<FileResponse> picUriList = new Gson().fromJson(s, new TypeToken<List<FileResponse>>() {
                        }.getType());

                        UpdateUserRequest req = new UpdateUserRequest();
                        req.setUserCode(UserInfo.getInstance().getUserCode());
                        req.setUserName(name.get());
                        req.setIdNumber(idNo.get());
                        req.setIdFront(picUriList.get(0).getFilePath());
                        req.setIdBack(picUriList.get(1).getFilePath());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                                new Gson().toJson(req));

                        return RetrofitHelper.getAccountAPI().updateSaler(requestBody);
                    })
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Toast.makeText(activity, "资料提交成功，请等待审核", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }, throwable -> {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        activity.dismissLoading();
                    }, () -> activity.dismissLoading());
        }
    });
}
