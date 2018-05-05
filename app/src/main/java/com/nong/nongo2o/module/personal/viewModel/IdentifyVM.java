package com.nong.nongo2o.module.personal.viewModel;

import android.Manifest;
import android.databinding.ObservableBoolean;
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
import com.nong.nongo2o.entity.domain.User;
import com.nong.nongo2o.entity.request.UpdateUserRequest;
import com.nong.nongo2o.module.personal.activity.IdentifyActivity;
import com.nong.nongo2o.module.personal.fragment.AgreementFragment;
import com.nong.nongo2o.module.personal.fragment.IdentifyFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiConstants;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
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
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

import static android.R.attr.fragment;

/**
 * Created by Administrator on 2017-6-30.
 */

public class IdentifyVM implements ViewModel {

    private IdentifyFragment fragment;

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> idNo = new ObservableField<>();
    @DrawableRes
    public final int idPicPlaceHolder1 = R.mipmap.idnumber_face;
    @DrawableRes
    public final int idPicPlaceHolder2 = R.mipmap.idnumber_reverseside;
    public final ObservableField<String> idPic1 = new ObservableField<>();
    public final ObservableField<String> idPic2 = new ObservableField<>();
    public final ObservableBoolean isAgree = new ObservableBoolean(false);

    private List<String> idPicList;

    public IdentifyVM(IdentifyFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isAuthing = new ObservableBoolean(false);
    }

    private void initData() {
        idPicList = new ArrayList<>();
        viewStyle.isAuthing.set(UserInfo.getInstance().getUserType() == 10);

        if (UserInfo.getInstance().getUserType() == 10) {
            RetrofitHelper.getUserAPI()
                    .userProfile()
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> {
                        name.set(user.getUserName());
                        idNo.set(user.getIdNumber());
                        idPic1.set(ApiConstants.PIC_URL.substring(0, ApiConstants.PIC_URL.length() - 1) + user.getIdFront());
                        idPic2.set(ApiConstants.PIC_URL.substring(0, ApiConstants.PIC_URL.length() - 1) + user.getIdBack());
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    /**
     * 用户协议
     */
    public final ReplyCommand agreementClick = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                AgreementFragment.newInstance(), AgreementFragment.TAG);
    });

    /**
     * 身份证照片正面
     */
    public final ReplyCommand idFaceClick = new ReplyCommand(() -> {
        if (UserInfo.getInstance().getUserType() == 10) {
            Toast.makeText(fragment.getActivity(), "正在审核中，请不要修改", Toast.LENGTH_SHORT).show();
            return;
        }

        checkCameraPermissions(idPic1, 1);
    });

    /**
     * 身份证照片反面
     */
    public final ReplyCommand idReverssideClick = new ReplyCommand(() -> {
        if (UserInfo.getInstance().getUserType() == 10) {
            Toast.makeText(fragment.getActivity(), "正在审核中，请不要修改", Toast.LENGTH_SHORT).show();
            return;
        }

        checkCameraPermissions(idPic2, 2);
    });

    private void checkCameraPermissions(ObservableField<String> picUri, int pos) {
        new RxPermissions(fragment.getActivity())
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        selectPic(picUri, pos);
                    } else {
                        Toast.makeText(fragment.getActivity(), "拒绝权限将不能打开相册", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selectPic(ObservableField<String> picUri, int pos) {
        RxGalleryFinal
                .with(fragment.getActivity())
                .image()
                .radio()
//                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent event) throws Exception {
                        picUri.set("file://" + event.getResult().getOriginalPath());
//                        idPicMap.put(pos, new File(event.getResult().getOriginalPath()));
                        idPicList.add(event.getResult().getOriginalPath());
                    }
                })
                .openGallery();
    }

    /**
     * 提交认证
     */
    public final ReplyCommand submitClick = new ReplyCommand(() -> {
        if (!isAgree.get()) {
            Toast.makeText(fragment.getActivity(), "如果需要提交实名验证，请勾选同意规则", Toast.LENGTH_SHORT).show();
            return;
        }

        if (UserInfo.getInstance().getUserType() == 10) {
            Toast.makeText(fragment.getActivity(), "正在审核中，请不要修改", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name.get()) || TextUtils.isEmpty(idNo.get())) {
            Toast.makeText(fragment.getActivity(), "请填写完整的身份资料", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idPicList.size() < 2) {
            Toast.makeText(fragment.getActivity(), "请上传身份证照片", Toast.LENGTH_SHORT).show();
            return;
        }

        ((RxBaseActivity) fragment.getActivity()).showLoading();

        Observable.just(idPicList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(files -> Luban.with(fragment.getActivity()).load(files).get())
                .map(files -> {
                    Map<String, RequestBody> picMap = new LinkedHashMap<>();
                    for (File file : files) {
                        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                        picMap.put(file.getName(), body);
                    }
                    return picMap;
                })
                .flatMap(picMap -> RetrofitHelper.getFileAPI().uploadFile(picMap))
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
                    Toast.makeText(fragment.getActivity(), "资料提交成功，请等待审核", Toast.LENGTH_SHORT).show();
                    fragment.getActivity().finish();
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                }, () -> ((RxBaseActivity) fragment.getActivity()).dismissLoading());
    });
}
