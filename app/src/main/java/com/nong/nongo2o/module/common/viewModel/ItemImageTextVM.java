package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.ImgTextContent;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-8.
 */

public class ItemImageTextVM implements ViewModel {

    private ImgTextContent content;

    public final ObservableField<String> text = new ObservableField<>();

    public ItemImageTextVM(ImgTextContent content) {
        this.content = content;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (content != null) {
            text.set(content.getContent());
            if (content.getImg() != null && !content.getImg().isEmpty()) {
                for (String uri : content.getImg()) {
                    itemImageListVMs.add(new ItemImageListVM(uri));
                }
            }
        }
    }

    /**
     * 图片列表
     */
    public final ObservableList<ItemImageListVM> itemImageListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemImageListVM> itemImageListBinding = ItemBinding.of(BR.viewModel, R.layout.item_image_list);

    public class ItemImageListVM implements ViewModel {

        @DrawableRes
        public final int imgPlaceHolder = R.mipmap.ic_launcher;
        public final ObservableField<String> imgUri = new ObservableField<>();

        public ItemImageListVM(String uri) {
            imgUri.set(uri);
        }
    }
}
