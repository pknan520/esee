package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-7-15.
 */

public class ItemGoodsListVM implements ViewModel {

    //  假数据图片uri
    private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

    private RxBaseFragment fragment;

    @DrawableRes
    public final int goodsImgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> goodsImgUri = new ObservableField<>();
    public final ObservableField<String> goodsName = new ObservableField<>();
    public final ObservableField<Double> goodsPrice = new ObservableField<>();
    public final ObservableField<Integer> saleNum = new ObservableField<>();
    public final ObservableField<Integer> stockNum = new ObservableField<>();

    public ItemGoodsListVM(RxBaseFragment fragment) {
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        goodsImgUri.set(uriArray[(int) (Math.random() * 4)]);
        goodsName.set("墨西哥进口牛油果");
        goodsPrice.set(48.80);
        saleNum.set(28);
        stockNum.set(129);
    }

    /**
     * 查看商品详情
     */
    public final ReplyCommand detailClick = new ReplyCommand(() -> {
//        fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity()));
//        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

}
