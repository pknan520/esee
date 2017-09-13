package com.nong.nongo2o.module.common.viewModel;

import android.app.Fragment;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.SalerInfo;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.uils.FocusUtils;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

/**
 * Created by Administrator on 2017-7-4.
 */

public class ItemMerchantListVM implements ViewModel {

    private Fragment fragment;
    private SalerInfo saler;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    @DrawableRes
    public final int isFocus = R.mipmap.icon_focus_p;
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus;
    //  商家的商品列表
    public final ObservableList<ItemMerchantGoodsListVM> itemPicVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMerchantGoodsListVM> itemPicBinding = ItemBinding.of(BR.viewModel, R.layout.item_merchant_goods_list);

    // TODO: 2017-9-13 容错的构造方法，以后删除
    public ItemMerchantListVM(Fragment fragment) {
        this.fragment = fragment;
    }

    public ItemMerchantListVM(Fragment fragment, SalerInfo saler) {
        this.fragment = fragment;
        this.saler = saler;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isFocus = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        headUri.set(saler.getAvatar());
        name.set(saler.getUserNick());
        summary.set(saler.getProfile());
        viewStyle.isFocus.set(FocusUtils.checkIsFocus(saler.getUserCode()));

        if (saler.getGoods() != null && saler.getGoods().size() > 0) {
            for (Goods good : saler.getGoods()) {
                itemPicVMs.add(new ItemMerchantGoodsListVM(good));
            }
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
     * 关注按钮
     */
    public final ReplyCommand focusClick = new ReplyCommand(() -> {
        FocusUtils.changeFocus(fragment.getActivity(), viewStyle.isFocus.get(), saler.getUserCode(), viewStyle.isFocus::set);
    });

    public class ItemMerchantGoodsListVM implements ViewModel {

        private Goods good;

        @DrawableRes
        public final int goodsImgPlaceHolder = R.mipmap.ic_launcher;
        public final ObservableField<String> goodsImg = new ObservableField<>();
        public final ObservableField<String> goodsName = new ObservableField<>();
        public final ObservableField<BigDecimal> goodsPrice = new ObservableField<>();

        public ItemMerchantGoodsListVM(Goods good) {
            this.good = good;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            if (!TextUtils.isEmpty(good.getCovers())) {
                List<String> covers = new Gson().fromJson(good.getCovers(), new TypeToken<List<String>>() {
                }.getType());
                goodsImg.set(covers.get(0));
            }
            goodsName.set(good.getTitle());
            goodsPrice.set(good.getPrice());
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
