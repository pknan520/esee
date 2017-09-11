package com.nong.nongo2o.module.common.viewModel;

import android.app.Fragment;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;

import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * Created by Administrator on 2017-7-4.
 */

public class ItemMerchantListVM implements ViewModel {

    private Fragment fragment;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    @DrawableRes
    public final int isFocus = R.mipmap.icon_focus_p;
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus;

    public ItemMerchantListVM(Fragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    private void initFakeData() {
        name.set("果酱妈咪09");
        summary.set("这家伙很懒，什么都没留下~");

        for (int i = 0; i < 9; i++) {
            itemPicVMs.add(new ItemMerchantGoodsListVM());
        }
    }

    /**
     * 查看详情
     */
    public final ReplyCommand detailClick = new ReplyCommand(new Action() {
        @Override
        public void run() throws Exception {
            fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        }
    });

    /**
     * 进入商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });


    /**
     * 商家的商品列表
     */
    public final ObservableList<ItemMerchantGoodsListVM> itemPicVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMerchantGoodsListVM> itemPicBinding = ItemBinding.of(BR.viewModel, R.layout.item_merchant_goods_list);

    public class ItemMerchantGoodsListVM implements ViewModel {

        //  假数据图片uri
        private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhj5228gwdj20u00u0qv5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fhb0t7ob2mj20u011itd9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fgdmpxi7erj20qy0qyjtr.jpg"};

        @DrawableRes
        public final int goodsImgPlaceHolder = R.mipmap.ic_launcher;
        public final ObservableField<String> goodsImg = new ObservableField<>();
        public final ObservableField<String> goodsName = new ObservableField<>();
        public final ObservableField<String> goodsPrice = new ObservableField<>();

        public ItemMerchantGoodsListVM() {

            initFakeData();
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            goodsImg.set(uriArray[(int) (Math.random() * 4)]);
            goodsName.set("澳洲进口奇异果新鲜美味");
            goodsPrice.set("¥48.80");
        }

        /**
         * 查看商品详情
         */
        public final ReplyCommand goodsDetailClick = new ReplyCommand(() -> {
            fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });
    }
}
