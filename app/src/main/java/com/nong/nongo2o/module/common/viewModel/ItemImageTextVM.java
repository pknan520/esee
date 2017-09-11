package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entities.response.DynamicContent;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-8.
 */

public class ItemImageTextVM implements ViewModel {

    private DynamicContent content;

    public final ObservableField<String> text = new ObservableField<>();

    public ItemImageTextVM() {

        initData();
    }

    public ItemImageTextVM(DynamicContent content) {
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
        } else {
            //  假数据
            text.set("黑白灰这类型的色系，在很多业主的心中太过冰冷、单调，其实黑白灰的装饰可以是极俭，却不失品质;极简，却不失品位，室内门选择黑白灰色调，" +
                    "虽然褪去了缤纷的色彩，但它属于四季皆宜的颜色，同时还将简约与高级化身为一种低调的奢华，平静但不失深刻，让室内门看起来历久弥新。");

            for (int i = 0; i < 2; i++) {
                itemImageListVMs.add(new ItemImageListVM());
            }
        }
    }

    /**
     * 图片列表
     */
    public final ObservableList<ItemImageListVM> itemImageListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemImageListVM> itemImageListBinding = ItemBinding.of(BR.viewModel, R.layout.item_image_list);

    public class ItemImageListVM implements ViewModel {

        //  假数据图片uri
        private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fhovjwwphfj20u00u04qp.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

        @DrawableRes
        public final int imgPlaceHolder = R.mipmap.ic_launcher;
        public final ObservableField<String> imgUri = new ObservableField<>();

        public ItemImageListVM() {
            imgUri.set(uriArray[(int) (Math.random() * 4)]);
        }

        public ItemImageListVM(String uri) {
            imgUri.set(uri);
        }
    }
}
