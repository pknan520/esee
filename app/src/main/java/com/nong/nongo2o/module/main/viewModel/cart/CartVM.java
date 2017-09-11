package com.nong.nongo2o.module.main.viewModel.cart;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.activity.BuyActivity;
import com.nong.nongo2o.module.main.fragment.cart.CartFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;

import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-22.
 */

public class CartVM implements ViewModel {

    private CartFragment fragment;

    public final ObservableField<String> totalPrice = new ObservableField<>();
    public final ObservableField<String> transFee = new ObservableField<>();
    //  购物车商家列表
    public final ObservableList<ItemCartMerchantVM> itemCartMerchantVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemCartMerchantVM> itemCartMerchantBinding = ItemBinding.of(BR.viewModel, R.layout.item_cart_merchant);

    public CartVM(CartFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isEdit = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        totalPrice.set("¥117.60");
        transFee.set("含运费：¥20.00");

        //  添加商家列表
        for (int i = 0; i < 3; i++) {
            itemCartMerchantVMs.add(new ItemCartMerchantVM());
        }
    }

    /**
     * 提交订单
     */
    public final ReplyCommand submitOrderClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public class ItemCartMerchantVM implements ViewModel {

        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_default_60;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();

        public ItemCartMerchantVM() {

            initFakeData();
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            name.set("果酱妈咪09");
            summary.set("这家伙很懒，什么都没留下");
            //  添加商品列表
            for (int i = 0; i < 2; i++) {
                itemCartMerchantGoodsVMs.add(new ItemCartMerchantGoodsVM(this));
            }
        }

        /**
         * 打开商家主页
         */
        public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
            fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });

        /**
         * 商品列表
         */
        public final ObservableList<ItemCartMerchantGoodsVM> itemCartMerchantGoodsVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemCartMerchantGoodsVM> itemCartMerchantGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_cart_merchant_goods);

        public class ItemCartMerchantGoodsVM implements ViewModel {

            //  假数据图片uri
            private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
                    "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

            private ItemCartMerchantVM merchantVM;

            @DrawableRes
            public final int goodsImgPlaceHolder = R.mipmap.ic_launcher;
            public final ObservableField<String> goodsImgUri = new ObservableField<>();
            public final ObservableField<String> goodsName = new ObservableField<>();
            public final ObservableField<String> goodsSummary = new ObservableField<>();
            public final ObservableField<String> goodsPrice = new ObservableField<>();
            public final ObservableField<Integer> goodsNum = new ObservableField<>();
            public final ObservableField<String> standardStr = new ObservableField<>();

            public ItemCartMerchantGoodsVM(ItemCartMerchantVM merchantVM) {
                this.merchantVM = merchantVM;

                initFakeData();
            }

            public final ViewStyle viewStyle = new ViewStyle();

            public class ViewStyle {
                public final ObservableBoolean isEdit = new ObservableBoolean(false);
            }

            /**
             * 假数据
             */
            private void initFakeData() {
                goodsImgUri.set(uriArray[(int) (Math.random() * 4)]);
                goodsName.set("墨西哥进口牛油果");
                goodsSummary.set("精装4只/盒");
                goodsPrice.set("¥48.80");
                goodsNum.set(4);
                standardStr.set("进口上等品");
            }

            /**
             * 查看商品详情
             */
            public final ReplyCommand detailClick = new ReplyCommand(() -> {
                fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            });

            /**
             * 增1
             */
            public final ReplyCommand addOneClick = new ReplyCommand(() -> {
                goodsNum.set(goodsNum.get() + 1);
            });

            /**
             * 减1
             */
            public final ReplyCommand subtractOneClick = new ReplyCommand(() -> {
                if (goodsNum.get() > 1) {
                    goodsNum.set(goodsNum.get() - 1);
                }
            });

            /**
             * 选择规格
             */
            public ReplyCommand standardClick = new ReplyCommand(() -> {
                fragment.showPopupStandard();
            });

            /**
             * 删除
             */
            public final ReplyCommand deleteClick = new ReplyCommand(this::deleteGoods);

            private void deleteGoods() {
                itemCartMerchantGoodsVMs.remove(this);
                if (itemCartMerchantGoodsVMs.size() == 0) {
                    itemCartMerchantVMs.remove(merchantVM);
                }
            }

        }
    }
}
