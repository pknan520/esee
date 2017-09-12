package com.nong.nongo2o.module.dynamic.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
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
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;
import com.nong.nongo2o.module.common.viewModel.ItemPicVM;
import com.nong.nongo2o.module.dynamic.fragment.DynamicPublishFragment;
import com.nong.nongo2o.module.dynamic.fragment.DynamicSelectGoodsFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DynamicDetail dynamic = null;
    private ItemPicVM.addRadioPicListener addBannerPicListener;

    public final ObservableField<String> title = new ObservableField<>();

    public DynamicPublishVM(DynamicPublishFragment fragment, DynamicDetail dynamic) {
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
        List<String> headerImgList = new Gson().fromJson(dynamic.getHeaderImg(), new TypeToken<List<String>>() {
        }.getType());
        for (String bannerUri : headerImgList) {
            itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), bannerUri, addBannerPicListener));
        }
        if (itemBannerVMs.size() < 9)
            itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), null, addBannerPicListener));
        title.set(dynamic.getTitle());
        List<DynamicContent> contentList = new Gson().fromJson(dynamic.getContent(), new TypeToken<List<DynamicContent>>() {
        }.getType());
        for (DynamicContent content : contentList) {
            itemDescVMs.add(new ItemDescVM(content));
        }
    }

    /**
     * 地区选择
     */
    public final ReplyCommand areaSelectClick = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                SelectAreaFragment.newInstance(), SelectAreaFragment.TAG);
    });

    /**
     * 动态Banner
     */
    public final ObservableList<ItemPicVM> itemBannerVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemPicVM> itemBannerBinding = ItemBinding.of(BR.viewModel, R.layout.item_pic);

    /**
     * 动态图文
     */
    public final ObservableList<ItemDescVM> itemDescVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDescVM> itemDescBinding = ItemBinding.of(BR.viewModel, R.layout.item_dynamic_publish_desc);

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

        private List<File> newPicFiles = new ArrayList<>();

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
                    newPicFiles.add(new File("file://" + mediaBean.getOriginalPath()));
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

        public List<File> getNewPicFiles() {
            return newPicFiles;
        }
    }

    /**
     * 添加商品链接
     */
    public final ReplyCommand addGoodsLinkClick = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                DynamicSelectGoodsFragment.newInstance(), DynamicSelectGoodsFragment.TAG);
    });

    /**
     * 发布按钮
     */
    public final ReplyCommand publishClick = new ReplyCommand(() -> {
        if (dynamic != null) {
            //  编辑，有原始数据
            Toast.makeText(fragment.getActivity(), "发布旧动态", Toast.LENGTH_SHORT).show();
        } else {
            //  新增，没有原始数据
            Toast.makeText(fragment.getActivity(), "发布新动态", Toast.LENGTH_SHORT).show();
        }
        uploadFile();
    });

    private void uploadFile() {
        Map<String, RequestBody> picMap = new HashMap<>();
        for (ItemDescVM item : itemDescVMs) {
            for (File file : item.getNewPicFiles()) {
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                picMap.put("file\":fileName=\"" + file.getName() + "\"", body);
            }
        }

        RetrofitHelper.getFileAPI()
                .uploadFile(picMap)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d("DynamicPublish", "accept: String" + s);
                    }
                }, throwable -> {

                }, ()-> {});
    }

    /**
     * 创建新dynamic
     * @param detail
     */
    private void createDynamic(DynamicDetail detail) {

    }
}
