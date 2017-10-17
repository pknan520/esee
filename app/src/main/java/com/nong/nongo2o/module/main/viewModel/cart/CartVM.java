package com.nong.nongo2o.module.main.viewModel.cart;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.Cart;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsSpec;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.IdRequest;
import com.nong.nongo2o.entity.request.UpdateCartBatchRequest;
import com.nong.nongo2o.module.common.buy.activity.BuyActivity;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.fragment.cart.CartFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.imUtils.IMUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    @DrawableRes
    public final int notLoginImg = R.mipmap.default_error;

    public CartVM(CartFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
        public final ObservableBoolean isEdit = new ObservableBoolean(false);

        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        public final ObservableBoolean notLogin = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        viewStyle.notLogin.set(TextUtils.isEmpty(UserInfo.getInstance().getSessionToken()));
        if (!viewStyle.notLogin.get()) getCartList();

        totalPrice.set("¥0");
        transFee.set("运费：¥0");
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

                    viewStyle.isEmpty.set(itemCartMerchantVMs.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(() -> {
        if (!viewStyle.notLogin.get()) getCartList();
    });

    /**
     * 批量更新购物车
     */
    public void updateCartList() {
        List<Cart> cartList = new ArrayList<>();
        if (itemCartMerchantVMs.size() > 0) {
            for (ItemCartMerchantVM itemM : itemCartMerchantVMs) {
                if (itemM.itemCartMerchantGoodsVMs.size() > 0) {
                    for (ItemCartMerchantVM.ItemCartMerchantGoodsVM itemG : itemM.itemCartMerchantGoodsVMs) {
                        Cart cart = new Cart();
                        cart.setCartCode(itemG.cart.getCartCode());
                        cart.setSpecCode(itemG.cart.getSpecCode());
                        cart.setGoodsNum(itemG.cart.getGoodsNum());
                        cartList.add(cart);
                    }
                }
            }
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(new UpdateCartBatchRequest(cartList)));

        RetrofitHelper.getCartAPI()
                .updateCartList(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> Toast.makeText(fragment.getActivity(), "购物车修改成功", Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * 提交订单
     */
    public final ReplyCommand submitOrderClick = new ReplyCommand(() -> {
        ArrayList<OrderDetail> orderDetails = createOrderDetails();
        if (orderDetails != null && !orderDetails.isEmpty()) {
            fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity(), createOrderDetails()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        } else {
            Toast.makeText(fragment.getActivity(), "请选择商品", Toast.LENGTH_SHORT).show();
        }
    });

    /**
     * 生成订单信息
     */
    private ArrayList<OrderDetail> createOrderDetails() {
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();

        for (ItemCartMerchantVM itemMerchant : itemCartMerchantVMs) {
            for (ItemCartMerchantVM.ItemCartMerchantGoodsVM itemGoods : itemMerchant.itemCartMerchantGoodsVMs) {
                if (itemGoods.mViewStyle.isSelect.get()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setUserCode(UserInfo.getInstance().getUserCode());
                    detail.setSaleUserCode(itemGoods.getCart().getSaleUserCode());
                    detail.setSale(itemGoods.getCart().getSaleUser());
                    detail.setGoodsCode(itemGoods.getCart().getGoodsCode());
                    detail.setGoods(itemGoods.getCart().getGoods());
                    detail.setSpecCode(itemGoods.getCart().getSpecCode());
                    detail.setGoodsSpec(itemGoods.getCart().getGoodsSpec());
                    detail.setGoodsNum(itemGoods.getCart().getGoodsNum());
                    detail.setUnitPrice(itemGoods.getCart().getGoodsSpec().getPrice());
                    detail.setTotalPrice(itemGoods.getCart().getGoodsSpec().getPrice().multiply(new BigDecimal(itemGoods.getCart().getGoodsNum())));
                    orderDetails.add(detail);
                }
            }
        }

        return orderDetails;
    }

    /**
     * 跳转登录
     */
    public final ReplyCommand toLogin = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), true));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public class ItemCartMerchantVM implements ViewModel {

        private List<Cart> cartList;

        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_default_60;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();
        //  商品列表
        public final ObservableList<ItemCartMerchantGoodsVM> itemCartMerchantGoodsVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemCartMerchantGoodsVM> itemCartMerchantGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_cart_merchant_goods);

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
            fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), cartList.get(0).getSaleUser()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });

        /**
         * 联系商家
         */
        public final ReplyCommand contactClick = new ReplyCommand(() -> {
            IMUtils.checkIMLogin(isSuccess -> {
                if (isSuccess) {
                    String userName = cartList.get(0).getSaleUser().getId();
                    if (userName.equals(EMClient.getInstance().getCurrentUser())) {
                        Toast.makeText(fragment.getActivity(), "您不能自言自语了啦^.^", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
                    intent.putExtra("userId", userName);
                    fragment.getActivity().startActivity(intent);
                    fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                } else {
                    Toast.makeText(fragment.getActivity(), "聊天可能有点问题，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            });
        });

        public class ItemCartMerchantGoodsVM implements ViewModel {

            private ItemCartMerchantVM merchantVM;
            private Cart cart;
            private GoodsSpec currentSpec;

            @DrawableRes
            public final int goodsImgPlaceHolder = R.mipmap.ic_launcher;
            public final ObservableField<String> goodsImgUri = new ObservableField<>();
            public final ObservableField<String> goodsName = new ObservableField<>();
            public final ObservableField<String> goodsSummary = new ObservableField<>();
            public final ObservableField<BigDecimal> goodsPrice = new ObservableField<>();
            public final ObservableField<Integer> goodsNum = new ObservableField<>();
            public final ObservableField<String> standardStr = new ObservableField<>();
            @DrawableRes
            public final int isSelect = R.mipmap.icon_select;
            @DrawableRes
            public final int notSelect = R.mipmap.icon_select_none;

            public ItemCartMerchantGoodsVM(ItemCartMerchantVM merchantVM, Cart cart) {
                this.merchantVM = merchantVM;
                this.cart = cart;

                initData();
            }

            public final ViewStyle mViewStyle = new ViewStyle();

            public class ViewStyle {
                public final ObservableBoolean isSelect = new ObservableBoolean(false);
                public final ObservableBoolean isEdit = new ObservableBoolean(false);
            }

            /**
             * 初始化数据
             */
            private void initData() {
                refreshData();
            }

            private void refreshData() {
                if (cart != null && cart.getGoods() != null) {
                    Goods good = cart.getGoods();
                    currentSpec = cart.getGoodsSpec();
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

            /**
             * 勾选事件
             */
            public final ReplyCommand checkClick = new ReplyCommand(new Action() {
                @Override
                public void run() throws Exception {
                    mViewStyle.isSelect.set(!mViewStyle.isSelect.get());
                    if (mViewStyle.isSelect.get()) {
                        for (ItemCartMerchantVM itemMerchantVM : itemCartMerchantVMs) {
                            if (itemMerchantVM != merchantVM) {
                                for (ItemCartMerchantGoodsVM itemGoodsVM : itemMerchantVM.itemCartMerchantGoodsVMs) {
                                    itemGoodsVM.mViewStyle.isSelect.set(false);
                                }
                            }
                        }
                    }

                    totalPrice.set(calculatePrice());
                    transFee.set(calculateTransFee());
                }
            });

            /**
             * 计算总价
             */
            private String calculatePrice() {
                BigDecimal total = new BigDecimal(0);
                for (ItemCartMerchantVM itemMerchantVM : itemCartMerchantVMs) {
                    for (ItemCartMerchantGoodsVM itemGoodsVM : itemMerchantVM.itemCartMerchantGoodsVMs) {
                        if (itemGoodsVM.mViewStyle.isSelect.get()) {
                            total = total.add(itemGoodsVM.goodsPrice.get().multiply(new BigDecimal(itemGoodsVM.goodsNum.get())));
                        }
                    }
                }

                return "¥" + total.toString();
            }

            public Cart getCart() {
                return cart;
            }

            /**
             * 计算运费
             */
            private String calculateTransFee() {
                BigDecimal total = new BigDecimal(0);
                for (ItemCartMerchantVM itemMerchantVM : itemCartMerchantVMs) {
                    for (ItemCartMerchantGoodsVM itemGoodsVM : itemMerchantVM.itemCartMerchantGoodsVMs) {
                        if (itemGoodsVM.mViewStyle.isSelect.get()) {
                            total = total.add(itemGoodsVM.getCart().getGoods().getFreight());
                        }
                    }
                }

                return "¥" + total.toString();
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
                cart.setGoodsNum(goodsNum.get());
            });

            /**
             * 减1
             */
            public final ReplyCommand subtractOneClick = new ReplyCommand(() -> {
                if (goodsNum.get() > 1) {
                    goodsNum.set(goodsNum.get() - 1);
                    cart.setGoodsNum(goodsNum.get());
                }
            });

            /**
             * 选择规格
             */
            public ReplyCommand standardClick = new ReplyCommand(() -> {
                fragment.showPopupStandard(cart, spec -> {
                    cart.setSpecCode(spec.getSpecCode());
                    cart.setGoodsSpec(spec);
                    refreshData();
                });
            });

            /**
             * 删除
             */
            public final ReplyCommand deleteClick = new ReplyCommand(() -> {
                ((RxBaseActivity) fragment.getActivity()).showDeleteDialog(ItemCartMerchantGoodsVM.this::deleteGoods);
            });

            private void deleteGoods() {
                RetrofitHelper.getCartAPI()
                        .deleteCart(cart.getCartCode())
                        .subscribeOn(Schedulers.io())
                        .map(new ApiResponseFunc<>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            itemCartMerchantGoodsVMs.remove(this);
                            if (itemCartMerchantGoodsVMs.size() == 0) {
                                itemCartMerchantVMs.remove(merchantVM);
                            }
                            viewStyle.isEmpty.set(itemCartMerchantVMs.isEmpty());
                        }, throwable -> {
                            Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

        }
    }
}
