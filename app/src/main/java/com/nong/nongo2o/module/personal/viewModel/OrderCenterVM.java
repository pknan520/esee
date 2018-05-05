package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.module.personal.fragment.OrderListTotalFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017-7-14.
 */

public class OrderCenterVM implements ViewModel {

    private OrderListTotalFragment fragment;
    private boolean isMerchantMode;
    private int tabLength;

    public final ObservableList<ItemTabVM> itemTabVMs = new ObservableArrayList<>();

    public OrderCenterVM(OrderListTotalFragment fragment, boolean isMerchantMode, int tabLength) {
        this.fragment = fragment;
        this.isMerchantMode = isMerchantMode;
        this.tabLength = tabLength;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < tabLength; i++) {
            itemTabVMs.add(new ItemTabVM());
        }

        getOrderCount();
    }

    /**
     * 获取订单统计
     */
    public void getOrderCount() {
        reset();
        String type = "";
        Map<String, String> paramMap = new HashMap<>();
        type = isMerchantMode ? "saler_order_status" : "buyer_order_status";
        paramMap.put(isMerchantMode ? "salerCode" : "buyerCode", UserInfo.getInstance().getUserCode());

        try {
            RetrofitHelper.getUserAPI()
                    .userDbWrapper(type, URLEncoder.encode(new Gson().toJson(paramMap), "utf-8"))
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        for(Map<String,Object> countMap : resp){
                            switch ((int)Double.parseDouble(countMap.get("order_status").toString()) ){
                                case 0: //  待付款
                                    if (!isMerchantMode) {
                                        itemTabVMs.get(0).tabBadgeText.set((int)Double.parseDouble(countMap.get("count").toString()));
                                    }
                                    break;
                                case 1: //  待发货
                                    itemTabVMs.get(isMerchantMode ? 0 : 1).tabBadgeText.set((int)Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 2: //  待收货
                                    itemTabVMs.get(isMerchantMode ? 1 : 2).tabBadgeText.set((int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 3: //  待评价
                                    itemTabVMs.get(isMerchantMode ? 2 : 3).tabBadgeText.set((int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 5: //  退款申请
                                    itemTabVMs.get(isMerchantMode ? 3 : 4).tabBadgeText.set(itemTabVMs.get(isMerchantMode ? 3 : 4).tabBadgeText.get() + (int) Double.parseDouble(countMap.get("count").toString()));
                                    break;
                                case 6: //  退款中
                                    itemTabVMs.get(isMerchantMode ? 3 : 4).tabBadgeText.set(itemTabVMs.get(isMerchantMode ? 3 : 4).tabBadgeText.get() + (int)Double.parseDouble(countMap.get("count").toString()));
                                    break;
                            }
                        }
//                        int total = 0;
//                        for (ItemTabVM item : itemTabVMs) {
//                            total += item.tabBadgeText.get();
//                        }
//                        itemTabVMs.get(0).tabBadgeText.set(total);

                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空统计显示
     */
    private void reset() {
        for (ItemTabVM item : itemTabVMs) {
            item.tabBadgeText.set(0);
        }
    }

    public class ItemTabVM implements ViewModel {

        public final ObservableField<Integer> tabBadgeText = new ObservableField<>();


    }

}
