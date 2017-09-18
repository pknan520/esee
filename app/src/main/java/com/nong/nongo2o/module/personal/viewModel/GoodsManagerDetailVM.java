package com.nong.nongo2o.module.personal.viewModel;

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
import com.nong.nongo2o.entities.response.DynamicContent;
import com.nong.nongo2o.entity.domain.FileResponse;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsSpec;
import com.nong.nongo2o.entity.domain.ImgTextContent;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.common.viewModel.ItemPicVM;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.nong.nongo2o.module.dynamic.viewModel.DynamicPublishVM;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerDetailFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiConstants;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-27.
 */

public class GoodsManagerDetailVM implements ViewModel {

    private static final String TAG = "GoodsManagerDetailVM";

    private Goods goods = null;
    private GoodsManagerDetailFragment fragment;
    private ItemPicVM.addRadioPicListener addBannerPicListener;

    //  商品Banner图
    private List<String> covers = new ArrayList<>();
    public final ObservableList<ItemPicVM> itemBannerVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemPicVM> itemBannerBinding = ItemBinding.of(BR.viewModel, R.layout.item_pic);
    //  商品名称
    public final ObservableField<String> goodsName = new ObservableField<>();
    //  商品规格
    public final ObservableList<ItemStandardVM> itemStandardVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemStandardVM> itemStandardBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_manager_standard);
    //  运费
    public final ObservableField<String> transFee = new ObservableField<>();
    //  商品描述
    private List<ImgTextContent> contentList = new ArrayList<>();
    public final ObservableList<ItemDescVM> itemDescVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDescVM> itemDescBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_manager_desc);

    private List<File> bannerFileList = new ArrayList<>();

    public GoodsManagerDetailVM(GoodsManagerDetailFragment fragment, Goods goods) {
        this.fragment = fragment;
        this.goods = goods;

        initListener();

        if (goods != null) {
            //  编辑，有原始数据
            //  初始化数据
            initData();
        } else {
            //  新增，没有原始数据
            //  Banner初始有一个添加图片的按钮
            itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), null, addBannerPicListener));
            //  初始有一个空白商品规格
            itemStandardVMs.add(new ItemStandardVM());
            //  初始有一个空白的商品描述
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
                Log.d(TAG, "addRadioPic: file://" + mediaBean.getOriginalPath());
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
        if (!TextUtils.isEmpty(goods.getCovers())) {
            covers = new Gson().fromJson(goods.getCovers(), new TypeToken<List<String>>() {
            }.getType());
            for (String coverUri : covers) {
                itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), coverUri, addBannerPicListener));
            }
        }
        if (itemBannerVMs.size() < 9)
            itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), null, addBannerPicListener));

        goodsName.set(goods.getTitle());

        if (goods.getGoodsSpecs() != null && goods.getGoodsSpecs().size() > 0) {
            for (GoodsSpec spec : goods.getGoodsSpecs()) {
                itemStandardVMs.add(new ItemStandardVM(spec));
            }
        } else {
            //  初始有一个空白商品规格
            itemStandardVMs.add(new ItemStandardVM());
        }

        transFee.set(String.valueOf(goods.getFreight()));

        if (!TextUtils.isEmpty(goods.getDetail())) {
            contentList = new Gson().fromJson(goods.getDetail(), new TypeToken<List<ImgTextContent>>() {
            }.getType());
            for (ImgTextContent content : contentList) {
                itemDescVMs.add(new ItemDescVM(content));
            }
        } else {
            //  初始有一个空白的商品描述
            itemDescVMs.add(new ItemDescVM());
        }

    }

    /**
     * 发布商品
     */
    public final ReplyCommand publishClick = new ReplyCommand(() -> {
        if (itemBannerVMs.size() < 2) {
            Toast.makeText(fragment.getActivity(), "请选择商品轮播图", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(goodsName.get())) {
            Toast.makeText(fragment.getActivity(), "请输入商品名称", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemStandardVMs.size() < 1) {
            Toast.makeText(fragment.getActivity(), "请输入最少一种商品规格", Toast.LENGTH_SHORT).show();
            return;
        }

        for (ItemStandardVM item : itemStandardVMs) {
            if (TextUtils.isEmpty(item.standard.get()) || TextUtils.isEmpty(item.price.get()) || TextUtils.isEmpty(item.num.get())) {
                Toast.makeText(fragment.getActivity(), "请输入完整的商品规格", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (itemDescVMs.size() < 1 || (TextUtils.isEmpty(itemDescVMs.get(0).goodsDesc.get()) && itemDescVMs.get(0).itemDescPicVMs.size() < 2)) {
            Toast.makeText(fragment.getActivity(), "请输入商品描述", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadFile(goods == null ? new Goods() : goods, goods == null);
    });

    /**
     * 上传图片
     */
    private void uploadFile(Goods good, boolean isNew) {
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
                                postGood(createGood(good, picUriList), isNew);
                            },
                            throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            postGood(createGood(good, null), isNew);
        }
    }

    /**
     * 创建新good
     */
    private Goods createGood(Goods good, List<FileResponse> picUriList) {
        int i = 0;
        int hasAdd = 0;
        for (; i < bannerFileList.size(); i++) {
            covers.add(ApiConstants.BASE_URL + "file/image?filePath=" + picUriList.get(i).getFilePath());
            hasAdd++;
        }
        for (int j = 0; j < itemDescVMs.size(); j++) {
            ItemDescVM item = itemDescVMs.get(j);
            if (contentList == null || contentList.size() < (j + 1)) {
                contentList.add(new ImgTextContent());
            }
            ImgTextContent content = contentList.get(j);

            content.setContent(item.goodsDesc.get());
            if (content.getImg() == null) content.setImg(new ArrayList<>());
            for (; i < hasAdd + item.getNewPicList().size(); i++) {
                content.getImg().add(ApiConstants.BASE_URL + "file/image?filePath=" + picUriList.get(i).getFilePath());
            }
            hasAdd += item.getNewPicList().size();
        }
        List<GoodsSpec> specList = new ArrayList<>();
        for (ItemStandardVM itemStandardVM : itemStandardVMs) {
            GoodsSpec spec = new GoodsSpec();
            spec.setTitle(itemStandardVM.standard.get());
            spec.setPrice(new BigDecimal(itemStandardVM.price.get()));
            spec.setQuantity(Integer.parseInt(itemStandardVM.num.get()));
            specList.add(spec);
        }

        good.setPrice(specList.get(0).getPrice());
        good.setCovers(new Gson().toJson(covers));
        good.setTitle(goodsName.get());
        good.setGoodsSpecs(specList);
        good.setDetail(new Gson().toJson(contentList));
        good.setFreight(new BigDecimal(transFee.get()));
        //  默认分类
        good.setCategoryCode("code001");

        return good;
    }

    /**
     * 发布商品
     */
    private void postGood(Goods good, boolean isNew) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(good));

        if (isNew) {
            RetrofitHelper.getGoodsAPI()
                    .createUserGoods(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Toast.makeText(fragment.getActivity(), "添加商品成功", Toast.LENGTH_SHORT).show();
                        sendUpdateBroadcast();
                    }, throwable -> {
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            RetrofitHelper.getGoodsAPI()
                    .updateUserGoods(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Toast.makeText(fragment.getActivity(), "更新商品成功", Toast.LENGTH_SHORT).show();
                        sendUpdateBroadcast();
                    }, throwable -> {
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * 发送更新广播
     */
    private void sendUpdateBroadcast() {
        Intent intent = new Intent("goodsManagerUpdate");
        LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
        fragment.getActivity().getFragmentManager().popBackStack();
    }

    /**
     * 添加商品规格
     */
    public final ReplyCommand addStandardClick = new ReplyCommand(() -> itemStandardVMs.add(new ItemStandardVM()));

    public class ItemStandardVM implements ViewModel {

        private GoodsSpec spec;

        public final ObservableField<String> standard = new ObservableField<>();
        public final ObservableField<String> price = new ObservableField<>();
        public final ObservableField<String> num = new ObservableField<>();

        public ItemStandardVM() {

        }

        public ItemStandardVM(GoodsSpec spec) {
            this.spec = spec;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            standard.set(spec.getTitle());
            price.set(String.valueOf(spec.getPrice()));
            num.set(String.valueOf(spec.getQuantity()));
        }

        /**
         * 删除当前商品规格
         */
        public final ReplyCommand deleteClick = new ReplyCommand(() -> {
            View currentView = fragment.getActivity().getCurrentFocus();
            if (currentView != null) {
                currentView.clearFocus();
            }
            itemStandardVMs.remove(ItemStandardVM.this);
        });
    }

    /**
     * 添加商品描述
     */
    public final ReplyCommand addDescClick = new ReplyCommand(() -> itemDescVMs.add(new ItemDescVM()));

    public class ItemDescVM implements ViewModel {

        private ImgTextContent content;

        private ItemPicVM.addRadioPicListener addDescPicListener;
        public final ObservableField<String> goodsDesc = new ObservableField<>();
        //  每一项商品描述的图片
        public final ObservableList<ItemPicVM> itemDescPicVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemPicVM> itemDescPicBinding = ItemBinding.of(BR.viewModel, R.layout.item_pic);

        private List<File> newPicList = new ArrayList<>();

        public ItemDescVM() {
            initListener();

            //  初始有一个添加图片的按钮
            itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), null, addDescPicListener));
        }

        public ItemDescVM(ImgTextContent content) {
            this.content = content;

            initListener();
            initData();
        }

        /**
         * 初始化图文描述添加图片监听器
         */
        private void initListener() {
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
            goodsDesc.set(content.getContent());

            if (content.getImg() != null && content.getImg().size() > 0) {
                for (String uri : content.getImg()) {
                    itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), uri, addDescPicListener));
                }
            }
            if (itemDescPicVMs.size() < 9)
                itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), null, addDescPicListener));
        }

        /**
         * 删除商品描述
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

}
