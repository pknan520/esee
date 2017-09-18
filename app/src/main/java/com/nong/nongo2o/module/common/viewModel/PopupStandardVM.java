package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.Cart;
import com.nong.nongo2o.entity.domain.GoodsSpec;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-7-21.
 */

public class PopupStandardVM implements ViewModel {

    private RxBaseActivity activity;
    private PopupWindow popup;
    private Cart cart;
    private SelectListener listener;

    //  商品信息
    @DrawableRes
    public final int imgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> imgUri = new ObservableField<>();
    public final ObservableField<BigDecimal> price = new ObservableField<>();
    public final ObservableField<BigDecimal> minPrice = new ObservableField<>();    //  最低价
    public final ObservableField<BigDecimal> maxPrice = new ObservableField<>();    //  最高价
    public final ObservableField<Integer> stockNum = new ObservableField<>();
    public final ObservableField<String> standard = new ObservableField<>();
    //  商品规格
    private TagAdapter<GoodsSpec> tagAdapter;
    public final ObservableField<TagAdapter> standardAdapter = new ObservableField<>();
    public final ObservableList<GoodsSpec> standardList = new ObservableArrayList<>();

    //  总库存
    private int totalStock = 0;
    //  当前规格位置，没有选择时为-1
    private int currentSpecPos = -1;
    private GoodsSpec currentSpec;

    public interface SelectListener {
        void onSelected(GoodsSpec spec);
    }

    public PopupStandardVM(RxBaseActivity activity, PopupWindow popup, Cart cart, SelectListener listener) {
        this.activity = activity;
        this.popup = popup;
        this.cart = cart;
        this.listener = listener;

        initAdapter();
        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean selectedSpec = new ObservableBoolean(true);
    }

    private void initAdapter() {
        tagAdapter = new TagAdapter<GoodsSpec>(standardList) {
            @Override
            public View getView(FlowLayout parent, int position, GoodsSpec goodsSpec) {
                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.item_standard, parent, false);
                tv.setText(goodsSpec.getTitle());
                return tv;
            }
        };
        standardAdapter.set(tagAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (cart != null && cart.getGoods() != null) {
            if (!TextUtils.isEmpty(cart.getGoods().getCovers())) {
                List<String> covers = new Gson().fromJson(cart.getGoods().getCovers(), new TypeToken<List<String>>() {
                }.getType());
                imgUri.set(covers.get(0));
            }

            if (cart.getGoods().getGoodsSpecs() != null && cart.getGoods().getGoodsSpecs().size() > 0) {
                updateSpecInfo(getCurrentSpec());

                minPrice.set(cart.getGoods().getGoodsSpecs().get(0).getPrice());
                maxPrice.set(cart.getGoods().getGoodsSpecs().get(0).getPrice());

                for (GoodsSpec spec : cart.getGoods().getGoodsSpecs()) {
                    standardList.add(spec);
                    totalStock += spec.getQuantity();
                    if (spec.getPrice().compareTo(minPrice.get()) == -1 || spec.getPrice().compareTo(minPrice.get()) == 0) {
                        minPrice.set(spec.getPrice());
                    } else if (spec.getPrice().compareTo(maxPrice.get()) == 1) {
                        maxPrice.set(spec.getPrice());
                    }
                }
                Observable.timer(200, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> tagAdapter.setSelectedList(currentSpecPos));
            }
        }
    }

    /**
     * 获取当前规格
     */
    private GoodsSpec getCurrentSpec() {
        for (int i = 0; i < cart.getGoods().getGoodsSpecs().size(); i++) {
            if (cart.getSpecCode().equals(cart.getGoods().getGoodsSpecs().get(i).getSpecCode())) {
                currentSpecPos = i;
                return cart.getGoods().getGoodsSpecs().get(i);
            }
        }
        return null;
    }

    /**
     * 更新规格信息
     */
    private void updateSpecInfo(GoodsSpec spec) {
        if (spec != null) {
            price.set(spec.getPrice());
            stockNum.set(spec.getQuantity());
        }
    }

    /**
     * 取消按钮
     */
    public final ReplyCommand cancelClick = new ReplyCommand(this::popupDismiss);

    /**
     * 规格点击事件
     */
    public final ReplyCommand<Integer> tagClickCommand = new ReplyCommand<>(position -> {
        if (currentSpecPos != -1 && currentSpecPos == position) {
            //  当前已选择，取消选择
            currentSpecPos = -1;
            viewStyle.selectedSpec.set(false);
            price.set(cart.getGoods().getPrice());
            stockNum.set(totalStock);
        } else {
            currentSpecPos = position;
            viewStyle.selectedSpec.set(true);
            price.set(cart.getGoods().getGoodsSpecs().get(position).getPrice());
            stockNum.set(cart.getGoods().getGoodsSpecs().get(position).getQuantity());
        }
    });

    /**
     * 确定按钮
     */
    public final ReplyCommand confirmClick = new ReplyCommand(() -> {
        if (currentSpecPos == -1) {
            Toast.makeText(activity, "请选择一种规格", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (listener != null) {
            listener.onSelected(cart.getGoods().getGoodsSpecs().get(currentSpecPos));
            popupDismiss();
        }
    });

    private void popupDismiss() {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }
}
