package com.nong.nongo2o.module.personal.viewModel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import com.nong.nongo2o.entity.domain.FileResponse;
import com.nong.nongo2o.entity.domain.ImgTextContent;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiConstants;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

/**
 * Created by PANYJ7 on 2018-1-10.
 */

public class DialogRefundVM implements ViewModel {

    private Activity activity;
    private AlertDialog dialog;
    private DialogRefundListener listener;

    public final ObservableField<BigDecimal> total = new ObservableField<>();
    public final ObservableField<String> refund = new ObservableField<>();
    public final ObservableField<String> applyReason = new ObservableField<>();
    public final ObservableField<String> reason = new ObservableField<>();

    @DrawableRes
    public final int placeHolder = R.mipmap.add_picture;
    public final ObservableField<String> imgUri = new ObservableField<>();
    private File picFile = null;

    public interface DialogRefundListener {
        void confirmClick(String reason, BigDecimal money);
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isSaler = new ObservableBoolean(false);
        public final ObservableBoolean isAgree = new ObservableBoolean(false);

        public final ObservableBoolean showPic = new ObservableBoolean(true);
        public final ObservableBoolean canDelete = new ObservableBoolean(false);
    }

    public DialogRefundVM(Activity activity, AlertDialog dialog, DialogRefundListener listener, Order order, boolean isSaler, boolean isAgree, String applyReason) {
        this.activity = activity;
        this.dialog = dialog;
        this.listener = listener;

        total.set(order.getTotalPrice());

        if (applyReason != null & !TextUtils.isEmpty(applyReason) && applyReason.startsWith("{") && applyReason.endsWith("}")) {
            ImgTextContent content = new Gson().fromJson(applyReason, new TypeToken<ImgTextContent>() {
            }.getType());
            this.applyReason.set(content.getContent());
            if (content.getImg() != null && !content.getImg().isEmpty()) {
                this.imgUri.set(content.getImg().get(0));
            }
        } else {
            this.applyReason.set(applyReason);
        }

        viewStyle.isSaler.set(isSaler);
        viewStyle.isAgree.set(isAgree);
        viewStyle.showPic.set(!(isSaler && TextUtils.isEmpty(imgUri.get())));
        viewStyle.canDelete.set(!isSaler && !TextUtils.isEmpty(imgUri.get()));
    }

    public final ReplyCommand picClick = new ReplyCommand(() -> {
        if (!viewStyle.isSaler.get()) {
            new RxPermissions(activity)
                    .request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            addPicFromGallery();
                        } else {
                            Toast.makeText(activity, "拒绝权限将不能打开相册", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    });

    private void addPicFromGallery() {
        RxGalleryFinal
                .with(activity)
                .image()
                .multiple()
                .maxSize(1)
                .cropAspectRatioOptions(0, new AspectRatio("3.3", 30, 10))
                .crop()
                .imageLoader(ImageLoaderType.FRESCO)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {
                    @Override
                    protected void onEvent(ImageMultipleResultEvent event) throws Exception {
                        if (event.getResult() != null && !event.getResult().isEmpty()) {
                            imgUri.set("file://" + event.getResult().get(0).getOriginalPath());
                            picFile = new File(event.getResult().get(0).getOriginalPath());
                            viewStyle.canDelete.set(true);
                        }
                    }
                })
                .openGallery();
    }

    public final ReplyCommand deleteClick = new ReplyCommand(() -> {
        imgUri.set("");
        picFile = null;
        viewStyle.canDelete.set(false);
    });

    /**
     * 取消按键
     */
    public final ReplyCommand cancelClick = new ReplyCommand(this::cancelDialog);

    private void cancelDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    /**
     * 确定按键
     */
    public final ReplyCommand confirmClick = new ReplyCommand(this::confirm);

    private void confirm() {
        if (TextUtils.isEmpty(reason.get())) {
            Toast.makeText(activity, "请输入申请退款的原因", Toast.LENGTH_SHORT).show();
            return;
        }

        if (viewStyle.isSaler.get() && viewStyle.isAgree.get()) {
            if (TextUtils.isEmpty(refund.get()) || Float.parseFloat(refund.get()) <= 0) {
                Toast.makeText(activity, "请输入退款金额", Toast.LENGTH_SHORT).show();
                return;
            }

            if (new BigDecimal(refund.get()).compareTo(total.get()) == 1) {
                Toast.makeText(activity, "退款金额大于订单金额了", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (listener != null) {
            if (viewStyle.isSaler.get() && viewStyle.isAgree.get()) {
                //  卖家同意退款
                listener.confirmClick(reason.get(), new BigDecimal(refund.get()));
            } else {
                //  买家申请退款
                ImgTextContent content = new ImgTextContent();
                content.setContent(reason.get());
                content.setImg(new ArrayList<>());

                if (picFile != null) {
                    Observable.just(picFile)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .map(file -> Luban.with(activity).load(file).get())
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
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(s -> {
                                List<FileResponse> picUriList = new Gson().fromJson(s, new TypeToken<List<FileResponse>>() {
                                }.getType());
                                if (picUriList != null && !picUriList.isEmpty()) {
                                    content.getImg().add(ApiConstants.BASE_URL + picUriList.get(0).getFilePath());
                                }
                                listener.confirmClick(new Gson().toJson(content), new BigDecimal(0));
                            }, throwable -> {
                                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    listener.confirmClick(new Gson().toJson(content), new BigDecimal(0));
                }
            }
        }

        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}

