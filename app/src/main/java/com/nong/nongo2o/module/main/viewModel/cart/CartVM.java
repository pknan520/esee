package com.nong.nongo2o.module.main.viewModel.cart;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.domain.Cart;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsSpec;
import com.nong.nongo2o.module.common.activity.BuyActivity;
import com.nong.nongo2o.module.main.fragment.cart.CartFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
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

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
        public final ObservableBoolean isEdit = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getCartList();
    }

    private void getCartList() {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getCartAPI()
                .getCartList()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    itemCartMerchantVMs.clear();

                    Map<String, List<Cart>> cartMap = new HashMap<>();
                    for (Cart cart : resp) {
                        if (cartMap.containsKey(cart.getSaleUserCode())) {
                            cartMap.get(cart.getSaleUserCode()).add(cart);
                        } else {
                            cartMap.put(cart.getSaleUserCode(), new ArrayList<>());
                            cartMap.get(cart.getSaleUserCode()).add(cart);
                        }
                    }

                    for (String key : cartMap.keySet()) {
                        itemCartMerchantVMs.add(new ItemCartMerchantVM(cartMap.get(key)));
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::getCartList);

    /**
     * 提交订单
     */
    public final ReplyCommand submitOrderClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public class ItemCartMerchantVM implements ViewModel {

        private List<Cart> cartList;

        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_default_60;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();

        public ItemCartMerchantVM(List<Cart> cartList) {
            this.cartList = cartList;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            if (cartList != null && cartList.size() > 0) {
                //  商家信息
                SimpleUser saler = cartList.get(0).getSaleUser();
                headUri.set(saler.getAvatar());
                name.set(saler.getUserNick());
                summary.set(saler.getProfile());
                //  添加商品列表
                for (Cart cart : cartList) {
                    itemCartMerchantGoodsVMs.add(new ItemCartMerchantGoodsVM(this, cart));
                }
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

            private ItemCartMerchantVM merchantVM;
            private Cart cart;
            private List<GoodsSpec> specList;
            private GoodsSpec currentSpec;

            @DrawableRes
            public final int goodsImgPlaceHolder = R.mipmap.ic_launcher;
            public final ObservableField<String> goodsImgUri = new ObservableField<>();
            public final ObservableField<String> goodsName = new ObservableField<>();
            public final ObservableField<String> goodsSummary = new ObservableField<>();
            public final ObservableField<BigDecimal> goodsPrice = new ObservableField<>();
            public final ObservableField<Integer> goodsNum = new ObservableField<>();
            public final ObservableField<String> standardStr = new ObservableField<>();

            public ItemCartMerchantGoodsVM(ItemCartMerchantVM merchantVM, Cart cart) {
                this.merchantVM = merchantVM;
                this.cart = cart;

                initData();
            }

            public final ViewStyle viewStyle = new ViewStyle();

            public class ViewStyle {
                public final ObservableBoolean isSelect = new ObservableBoolean(false);
                public final ObservableBoolean isEdit = new ObservableBoolean(false);
            }

            /**
             * 初始化数据
             */
            private void initData() {
                if (cart != null && cart.getGoods() != null) {
                    Goods good = cart.getGoods();
                    if (good.getGoodsSpecs() != null && good.getGoodsSpecs().size() > 0) {
                        specList = good.getGoodsSpecs();
                        currentSpec = getCurrentSpec();
                    }
                    if (!TextUtils.isEmpty(good.getCovers())) {
                        List<String> covers = new Gson().fromJson(good.getCovers(), new TypeToken<List<String>>() {
                        }.getType());
                        goodsImgUri.set(covers.get(0));
                    }
                    goodsName.set(good.getTitle());
                    goodsNum.set(cart.getGoodsNum());
                    if (currentSpec != null) {
                        goodsSummary.set(currentSpec.getTitle());
                        goodsPrice.set(currentSpec.getPrice());
                        standardStr.set(currentSpec.getTitle());
                    }
                }
            }

            private GoodsSpec getCurrentSpec() {
                for (GoodsSpec spec : specList) {
                    if (spec.getSpecCode().equals(cart.getSpecCode())) {
                        return spec;
                    }
                }
                return null;
            }

            /**
             * 查看商品详情
             */
            public final ReplyCommand detailClick = new ReplyCommand(() -> {
                fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity(), cart.getGoods()));
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
                fragment.showPopupStandard(cart);
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
