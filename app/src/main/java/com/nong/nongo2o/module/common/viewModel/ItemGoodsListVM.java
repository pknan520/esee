package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsSpec;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-7-15.
 */

public class ItemGoodsListVM implements ViewModel {

    //  假数据图片uri
    private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

    private RxBaseFragment fragment;
    private Goods goods;

    @DrawableRes
    public final int goodsImgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> goodsImgUri = new ObservableField<>();
    public final ObservableField<String> goodsName = new ObservableField<>();
    public final ObservableField<BigDecimal> goodsPrice = new ObservableField<>();
    public final ObservableField<Integer> saleNum = new ObservableField<>();
    public final ObservableField<Integer> stockNum = new ObservableField<>();

    public ItemGoodsListVM(RxBaseFragment fragment, Goods goods) {
        this.fragment = fragment;
        this.goods = goods;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (goods != null) {
            if (!TextUtils.isEmpty(goods.getCovers())) {
                List<String> covers = new Gson().fromJson(goods.getCovers(), new TypeToken<List<String>>(){}.getType());
                goodsImgUri.set(covers.get(0));
            }

            goodsName.set(goods.getTitle());
            goodsPrice.set(goods.getPrice());
            saleNum.set(goods.getTotalSale());

            int totalStock = 0;
            if (goods.getGoodsSpecs() != null && goods.getGoodsSpecs().size() > 0) {
                for (GoodsSpec spec : goods.getGoodsSpecs()) {
                    totalStock += spec.getQuantity();
                }
                stockNum.set(totalStock);
            }
        }
    }

    /**
     * 查看商品详情
     */
    public final ReplyCommand detailClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity(), goods));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

}
