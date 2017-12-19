package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017-7-14.
 */

public class ItemOrderGoodsListVM implements ViewModel {

    public static final int FROM_ORDER_LIST = 0, FROM_ORDER_DETAIL = 1, FROM_ORDER_CREATE = 2;

    private Gson gson;
    private OrderDetail orderDetail;
    private Order order;
    private boolean isMerchantMode;

    private int fromTag;
    private RxBaseFragment fragment;

    @DrawableRes
    public final int imgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> imgUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> standard = new ObservableField<>();
    public final ObservableField<BigDecimal> price = new ObservableField<>();
    public final ObservableField<Integer> num = new ObservableField<>();

    public ItemOrderGoodsListVM(OrderDetail orderDetail, @Nullable Order order, boolean isMerchantMode, int fromTag, RxBaseFragment fragment) {
        gson = new Gson();
        this.orderDetail = orderDetail;
        this.order = order;
        this.isMerchantMode = isMerchantMode;
        this.fromTag = fromTag;
        this.fragment = fragment;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (orderDetail != null) {
            if (null != orderDetail.getGoods() && null != orderDetail.getGoods().getCovers()) {
                List<String> list = gson.fromJson(orderDetail.getGoods().getCovers(), new TypeToken<List<String>>() {
                }.getType());
                imgUri.set(list.get(0));
            }
            name.set(orderDetail.getGoods().getTitle());
            if (orderDetail.getGoodsSpec() != null) {
                standard.set(orderDetail.getGoodsSpec().getTitle());
            }
            price.set(orderDetail.getUnitPrice());
            num.set(orderDetail.getGoodsNum());
        }
    }

    /**
     * 点击事件（跳转至订单详情或商品详情)
     */
    public final ReplyCommand itemClickCommand = new ReplyCommand(this::itemClick);

    private void itemClick() {
        switch (fromTag) {
            case FROM_ORDER_LIST:
                ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                        OrderDetailFragment.newInstance(order, isMerchantMode), OrderDetailFragment.TAG);
                break;
            case FROM_ORDER_DETAIL:
                fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity(), orderDetail.getGoods()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                break;
        }
    }
}
