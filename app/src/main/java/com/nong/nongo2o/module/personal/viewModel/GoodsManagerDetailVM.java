package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemPicVM;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerDetailFragment;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-27.
 */

public class GoodsManagerDetailVM implements ViewModel {

    private static final String TAG = "GoodsManagerDetailVM";

    private GoodsManagerDetailFragment fragment;
    private ItemPicVM.addRadioPicListener addBannerPicListener;

    //  商品Banner图
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
    public final ObservableList<ItemDescVM> itemDescVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDescVM> itemDescBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_manager_desc);

    public GoodsManagerDetailVM(GoodsManagerDetailFragment fragment) {
        this.fragment = fragment;

        initListener();

        //  Banner初始有一个添加图片的按钮
        itemBannerVMs.add(new ItemPicVM(fragment.getActivity(), null, addBannerPicListener));
        //  初始有一个空白商品规格
        itemStandardVMs.add(new ItemStandardVM());
        //  初始有一个空白的商品描述
        itemDescVMs.add(new ItemDescVM());
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
                if (itemBannerVMs.size() > 9) {
                    itemBannerVMs.remove(itemBannerVMs.size() - 1);
                }
            }
        };
    }

    /**
     * 发布商品
     */
    public final ReplyCommand publishClick = new ReplyCommand(() -> Toast.makeText(fragment.getActivity(), "你点击了发布按钮", Toast.LENGTH_SHORT).show());

    /**
     * 添加商品规格
     */
    public final ReplyCommand addStandardClick = new ReplyCommand(() -> itemStandardVMs.add(new ItemStandardVM()));

    public class ItemStandardVM implements ViewModel {

        public final ObservableField<String> standard = new ObservableField<>();
        public final ObservableField<String> price = new ObservableField<>();
        public final ObservableField<String> num = new ObservableField<>();

        public ItemStandardVM() {

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

        private ItemPicVM.addRadioPicListener addDescPicListener;
        public final ObservableField<String> goodsDesc = new ObservableField<>();

        public ItemDescVM() {

            initListener();

            //  初始有一个添加图片的按钮
            itemDescPicVMs.add(new ItemPicVM(fragment.getActivity(), null, addDescPicListener));
        }

        private void initListener() {
            //  初始化图文描述添加图片监听器
            addDescPicListener = new ItemPicVM.addRadioPicListener() {
                @Override
                public void addRadioPic(MediaBean mediaBean) {
                    itemDescPicVMs.add(itemDescPicVMs.size() - 1, new ItemPicVM(fragment.getActivity(), "file://" + mediaBean.getOriginalPath(), this));
                    if (itemDescPicVMs.size() > 9) {
                        itemDescPicVMs.remove(itemDescPicVMs.size() - 1);
                    }
                }
            };
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

        /**
         * 每一项商品描述的图片
         */
        public final ObservableList<ItemPicVM> itemDescPicVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemPicVM> itemDescPicBinding = ItemBinding.of(BR.viewModel, R.layout.item_pic);

    }

}
