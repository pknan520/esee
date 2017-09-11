package com.nong.nongo2o.module.merchant.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.activity.BuyActivity;
import com.nong.nongo2o.module.common.viewModel.ItemImageTextVM;
import com.nong.nongo2o.module.merchant.fragment.MerchantGoodsFragment;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-4.
 */

public class MerchantGoodsVM implements ViewModel {

    //  假数据图片uri
    private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhj5228gwdj20u00u0qv5.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhb0t7ob2mj20u011itd9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fgdmpxi7erj20qy0qyjtr.jpg"};

    private MerchantGoodsFragment fragment;

    //  商家信息
    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    @DrawableRes
    public final int isFocus = R.mipmap.icon_like;
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus_white;
    //  商品轮播
    public final ObservableList<DefaultSliderView> sliderList = new ObservableArrayList<>();
    //  商品信息
    public final ObservableField<String> goodsName = new ObservableField<>();
    public final ObservableField<String> goodsPrice = new ObservableField<>();
    public final ObservableField<Integer> stockNum = new ObservableField<>();
    public final ObservableField<Integer> saleNum = new ObservableField<>();
    //  商品规格
    public final ObservableField<TagAdapter> standardAdapter = new ObservableField<>();
    public final ObservableList<String> standardList = new ObservableArrayList<>();
    //  运费
    public final ObservableField<String> transFee = new ObservableField<>();

    public MerchantGoodsVM(MerchantGoodsFragment fragment) {
        this.fragment = fragment;

        initAdapter();
        initFakeData();
    }

    private void initAdapter() {
        standardAdapter.set(new TagAdapter<String>(standardList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_standard, parent, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        //  商家信息
        name.set("NeilsonLo");
        summary.set("这家伙很懒，还没填写任何东西~");
        //  轮播图
        addSliderView();
        //  商品信息
        goodsName.set("墨西哥进口牛油果");
        goodsPrice.set("¥48.80");
        stockNum.set(725);
        saleNum.set(99);
        //  规格列表
        String[] standardArray = {"规格", "规格+1", "规格不同长度", "就想看看长点会怎样", "规格+2"};
        for (int i = 0; i < 20; i++) {
            standardList.add(standardArray[(int) (Math.random() * 5 - 0.01)]);
        }
        //  运费
        transFee.set("¥10.00");
        //  图文详情
        for (int i = 0; i < 2; i++) {
            itemImageTextVMs.add(new ItemImageTextVM());
        }
    }

    /**
     * 添加轮播图
     */
    private void addSliderView() {
        for (int i = 0; i < uriArray.length; i++) {
            DefaultSliderView view = new DefaultSliderView(fragment.getActivity());
            view.image(uriArray[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderList.add(view);
        }
    }

    /**
     * 进入商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 图文详情
     */
    public final ObservableList<ItemImageTextVM> itemImageTextVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemImageTextVM> itemImageTextBinding = ItemBinding.of(BR.viewModel, R.layout.item_image_text);

    /**
     * 咨询商家
     */
    public final ReplyCommand consultClick = new ReplyCommand(() -> {

    });

    /**
     * 加入购物车
     */
    public final ReplyCommand addCartClick = new ReplyCommand(() -> {

    });

    /**
     * 立即购买
     */
    public final ReplyCommand buyClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

}
