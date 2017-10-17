package com.nong.nongo2o.module.dynamic.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entities.response.DynamicContent;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.domain.FileResponse;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;
import com.nong.nongo2o.module.common.viewModel.ItemPicVM;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.nong.nongo2o.module.dynamic.fragment.DynamicPublishFragment;
import com.nong.nongo2o.module.dynamic.fragment.DynamicSelectGoodsFragment;
import com.nong.nongo2o.module.personal.fragment.AddressEditFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiConstants;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AddressUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-7-1.
 */

public class DynamicPublishVM implements ViewModel {

    private DynamicPublishFragment fragment;
    private Moment dynamic = null;
    private ItemPicVM.addRadioPicListener addBannerPicListener;

    private List<String> headerImgList = new ArrayList<>();
    private List<DynamicContent> contentList = new ArrayList<>();

    private List<File> bannerFileList = new ArrayList<>();

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> areaName = new ObservableField<>();
    //  动态Banner
    public final ObservableList<ItemPicVM> itemBannerVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemPicVM> itemBannerBinding = ItemBinding.of(BR.viewModel, R.layout.item_pic);
    //  动态图文
    public final ObservableList<ItemDescVM> itemDescVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDescVM> itemDescBinding = ItemBinding.of(BR.viewModel, R.layout.item_dynamic_publish_desc);

    private String selectedGoodsCode = "";

    private City cityP, cityC, cityA;

    public DynamicPublishVM(DynamicPublishFragment fragment, Moment dynamic) {
        this.fragment = fragment;
        this.dynamic = dynamic;

        initListener();

        if (dynamic != null) {
            //  编辑，有原始数据
            //  初始化数据
            initData();
        } else {
            //  新增，没有原始数据
            //  初始有一个添加Banner的按钮
            itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), null, addBannerPicListener));
            //  初始有一项空白图文
            itemDescVMs.add(new ItemDescVM());
        }
    }

    /**
     * 初始化回调监听
     */
    private void initListener() {
        //  初始化添加Banner图的回调监听
        addBannerPicListener = new ItemPicVM.addRadioPicListener() {

            @Override
            public void addRadioPic(MediaBean mediaBean) {
                itemBannerVMs.add(itemBannerVMs.size() - 1, new ItemPicVM(fragment.getActivity(), "file://" + mediaBean.getOriginalPath(), this));
                bannerFileList.add(new File(mediaBean.getOriginalPath()));
                if (itemBannerVMs.size() > 9) {
                    itemBannerVMs.remove(itemBannerVMs.size() - 1);
                }
            }
        };
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (!TextUtils.isEmpty(dynamic.getHeaderImg())) {
            headerImgList = new Gson().fromJson(dynamic.getHeaderImg(), new TypeToken<List<String>>() {
            }.getType());
            for (String bannerUri : headerImgList) {
                itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), bannerUri, addBannerPicListener));
            }
            if (itemBannerVMs.size() < 9)
                itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), null, addBannerPicListener));
        }

        title.set(dynamic.getTitle());

        if (!TextUtils.isEmpty(dynamic.getContent())) {
            contentList = new Gson().fromJson(dynamic.getContent(), new TypeToken<List<DynamicContent>>() {
            }.getType());
            for (DynamicContent content : contentList) {
                itemDescVMs.add(new ItemDescVM(content));
            }
        }

        if (!TextUtils.isEmpty(dynamic.getProvinceCode()) && !TextUtils.isEmpty(dynamic.getCityCode()) && !TextUtils.isEmpty(dynamic.getAreaCode())) {
            List<City> cityList = AddressUtils.getCities(new String[]{dynamic.getProvinceCode(), dynamic.getCityCode(), dynamic.getAreaCode()});
            if (cityList != null && cityList.size() > 2) {
                cityP = cityList.get(0);
                cityC = cityList.get(1);
                cityA = cityList.get(2);
            }
            areaName.set(AddressUtils.getCityNameWithSpace(cityList));
        }
    }

    /**
     * 地区选择
     */
    public final ReplyCommand areaSelectClick = new ReplyCommand(() -> {
        fragment.startActivityForResult(SelectAreaActivity.newIntent(fragment.getActivity()), DynamicPublishFragment.GET_AREA);
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 设置城市
     */
    public void setCities(City cityP, City cityC, City cityA) {
        this.cityP = cityP;
        this.cityC = cityC;
        this.cityA = cityA;
        areaName.set(cityP.getCity_name() + " " + cityC.getCity_name() + " " + cityA.getCity_name());
    }

    /**
     * 添加动态图文
     */
    public final ReplyCommand addDescClick = new ReplyCommand(() -> itemDescVMs.add(new ItemDescVM()));

    public class ItemDescVM implements ViewModel {

        private DynamicContent content;
        private ItemPicVM.addRadioPicListener addDescPicListener;
        public final ObservableField<String> desc = new ObservableField<>();
        public final ObservableList<ItemPicVM> itemDescPicVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemPicVM> itemDescPicBinding = ItemBinding.of(BR.viewModel, R.layout.item_pic);

        private List<File> newPicList = new ArrayList<>();

        public ItemDescVM() {
            initListener();

            //  初始有一个添加图片的按钮
            itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), null, addDescPicListener));
        }

        public ItemDescVM(DynamicContent content) {
            this.content = content;

            initListener();
            initData();
        }

        private void initListener() {
            //  初始化图文描述添加图片监听器
            addDescPicListener = new ItemPicVM.addRadioPicListener() {
                @Override
                public void addRadioPic(MediaBean mediaBean) {
                    itemDescPicVMs.add(itemDescPicVMs.size() - 1, new ItemPicVM(fragment.getActivity(), "file://" + mediaBean.getOriginalPath(), this));
                    newPicList.add(new File(mediaBean.getOriginalPath()));
                    if (itemDescPicVMs.size() > 9) {
                        itemDescPicVMs.remove(itemDescPicVMs.size() - 1);
                    }
                }
            };
        }

        /**
         * 初始化数据
         */
        private void initData() {
            desc.set(content.getContent());

            if (content.getImg() != null && !content.getImg().isEmpty()) {
                for (String uri : content.getImg()) {
                    itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), uri, addDescPicListener));
                }
            }
            if (itemDescPicVMs.size() < 9) {
                //  添加一个加号图片
                itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), null, addDescPicListener));
            }
        }

        /**
         * 删除图文描述
         */
        public final ReplyCommand deleteDescClick = new ReplyCommand(() -> {
            View currentView = fragment.getActivity().getCurrentFocus();
            if (currentView != null) {
                currentView.clearFocus();
            }
            itemDescVMs.remove(ItemDescVM.this);
        });

        public List<File> getNewPicList() {
            return newPicList;
        }
    }

    /**
     * 添加商品链接
     */
    public final ReplyCommand addGoodsLinkClick = new ReplyCommand(() -> {
        // TODO: 2017-9-18 界面如何展示
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                DynamicSelectGoodsFragment.newInstance(), DynamicSelectGoodsFragment.TAG);
    });

    public void setSelectedGoods(Goods goods) {
        this.selectedGoodsCode = goods.getGoodsCode();
    }

    /**
     * 发布按钮
     */
    public final ReplyCommand publishClick = new ReplyCommand(() -> {
        if (itemBannerVMs.size() < 1) {
            Toast.makeText(fragment.getActivity(), "请选择最少一张主图", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(title.get())) {
            Toast.makeText(fragment.getActivity(), "请输入文章标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemDescVMs.size() < 1 || allContentEmpty()) {
            Toast.makeText(fragment.getActivity(), "请输入文章内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cityP == null || cityC == null || cityA == null) {
            Toast.makeText(fragment.getActivity(), "请选择您所在地区", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadFile(dynamic == null ? new Moment() : dynamic, dynamic == null);
    });

    private boolean allContentEmpty() {
        for (ItemDescVM item : itemDescVMs) {
            if (!TextUtils.isEmpty(item.desc.get()) || item.itemDescPicVMs.size() > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 上传图片
     */
    private void uploadFile(Moment dynamic, boolean isNew) {
        ((RxBaseActivity) fragment.getActivity()).showLoading();

        Map<String, RequestBody> picMap = new LinkedHashMap<>();
        for (File file : bannerFileList) {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            picMap.put(file.getName(), body);
        }
        for (ItemDescVM item : itemDescVMs) {
            for (File file : item.getNewPicList()) {
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                picMap.put(file.getName(), body);
            }
        }

        if (picMap.size() > 0) {
            RetrofitHelper.getFileAPI()
                    .uploadFile(picMap)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                                List<FileResponse> picUriList = new Gson().fromJson(s, new TypeToken<List<FileResponse>>() {
                                }.getType());
                                postDynamic(createDynamic(dynamic, picUriList), isNew);
                            },
                            throwable -> {
                                Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                            });
        } else {
            postDynamic(createDynamic(dynamic, null), isNew);
        }
    }

    /**
     * 创建新dynamic
     */
    private Moment createDynamic(Moment dynamic, List<FileResponse> picUriList) {
        if (picUriList != null && picUriList.size() > 0) {
            int i = 0;
            int hasAdd = 0;
            for (; i < bannerFileList.size(); i++) {
                headerImgList.add(ApiConstants.BASE_URL + "file/image?filePath=" + picUriList.get(i).getFilePath());
                hasAdd++;
            }
            for (int j = 0; j < itemDescVMs.size(); j++) {
                ItemDescVM item = itemDescVMs.get(j);
                if (contentList == null || contentList.size() < (j + 1)) {
                    contentList.add(new DynamicContent());
                }
                DynamicContent content = contentList.get(j);

                content.setContent(item.desc.get());
                if (content.getImg() == null) content.setImg(new ArrayList<>());
                for (; i < hasAdd + item.getNewPicList().size(); i++) {
                    content.getImg().add(ApiConstants.BASE_URL + "file/image?filePath=" + picUriList.get(i).getFilePath());
                }
                hasAdd += item.getNewPicList().size();
            }
        }

        dynamic.setHeaderImg(new Gson().toJson(headerImgList));
        dynamic.setTitle(title.get());
        dynamic.setContent(new Gson().toJson(contentList));
        dynamic.setGoodsCode(TextUtils.isEmpty(selectedGoodsCode) ? "" : selectedGoodsCode);

        dynamic.setProvinceCode(cityP.getCity_code());
        dynamic.setCityCode(cityC.getCity_code());
        dynamic.setAreaCode(cityA.getCity_code());

        return dynamic;
    }

    /**
     * 发表动态
     */
    private void postDynamic(Moment dynamic, boolean isNew) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(dynamic));

        if (isNew) {
            RetrofitHelper.getDynamicAPI()
                    .postMoment(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Intent intent = new Intent("refreshMyDynamic");
                        LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                        fragment.getActivity().finish();
                    }, throwable -> {
                        ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }, () -> ((RxBaseActivity) fragment.getActivity()).dismissLoading());
        } else {
            RetrofitHelper.getDynamicAPI()
                    .updateDynamic(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Intent intent = new Intent("refreshMyDynamic");
                        LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                        fragment.getActivity().finish();
                    }, throwable -> {
                        ((RxBaseActivity) fragment.getActivity()).dismissLoading();
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }, () -> ((RxBaseActivity) fragment.getActivity()).dismissLoading());
        }
    }
}
