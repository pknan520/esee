package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.dynamic.fragment.DynamicGoodsListFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-7-7.
 */

public class ItemSelectGoodsVM implements ViewModel {

    private DynamicGoodsListFragment fragment;
    private SelectClickListener listener;
    private Goods good;

    public final ObservableBoolean isSelected = new ObservableBoolean(false);
    @DrawableRes
    public final int imgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> imgUrl = new ObservableField<>();
    public final ObservableField<String> goodsName = new ObservableField<>();
    public final ObservableField<String> goodsSummary = new ObservableField<>();
    public final ObservableField<String> goodsPrice = new ObservableField<>();

    public interface SelectClickListener {
        void selectClick(Goods goods);
    }

    public ItemSelectGoodsVM(DynamicGoodsListFragment fragment, SelectClickListener listener, Goods good) {
        this.fragment = fragment;
        this.listener = listener;
        this.good = good;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (good != null) {
            if (!TextUtils.isEmpty(good.getCovers())) {
                List<String> imgs = new Gson().fromJson(good.getCovers(), new TypeToken<List<String>>() {
                }.getType());
                imgUrl.set(imgs.get(0));
            }
            goodsName.set(good.getTitle());
            // TODO: 2017-9-18 界面显示有异议
            goodsSummary.set(good.getGoodsSpecs().get(0).getTitle());
            goodsPrice.set("¥" + good.getGoodsSpecs().get(0).getPrice());
        }
    }

    public final ReplyCommand itemClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity(), good));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    public final ReplyCommand selectClick = new ReplyCommand(() -> {
        listener.selectClick(good);
        isSelected.set(!isSelected.get());
    });
}