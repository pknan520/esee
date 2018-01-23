package com.nong.nongo2o.module.personal.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiResponse;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.CreateGoodsCommentRequest;
import com.nong.nongo2o.entity.request.UpdateOrderRequest;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.fragment.OrderEvaluateFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import org.reactivestreams.Publisher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-9-23.
 */

public class OrderEvaluateVM implements ViewModel {

    private OrderEvaluateFragment fragment;
    private Order order;

    //  商品列表
    public final ObservableList<ItemGoodsVM> itemVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemGoodsVM> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_evaluate_goods);

    public OrderEvaluateVM(OrderEvaluateFragment fragment, Order order) {
        this.fragment = fragment;
        this.order = order;

        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (order != null && order.getOrderDetails() != null && order.getOrderDetails().size() > 0) {
            for (OrderDetail detail : order.getOrderDetails()) {
                itemVMs.add(new ItemGoodsVM(detail));
            }
        }
    }

    /**
     * 提交按钮
     */
    public final ReplyCommand submitClick = new ReplyCommand(() -> {
        for (ItemGoodsVM item : itemVMs) {
            if (item.rating.get() == 0.0f) {
                Toast.makeText(fragment.getActivity(), "最少一颗星哟", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Observable.just(itemVMs)
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .map(itemGoodsVM -> {
                    CreateGoodsCommentRequest request = new CreateGoodsCommentRequest();
                    request.setGoodsCode(itemGoodsVM.getGoodsCode());
                    request.setStar(Math.round(itemGoodsVM.rating.get()));
                    request.setContent(itemGoodsVM.content.get());
                    return request;
                })
                .map(request -> RequestBody.create(MediaType.parse("Content-Type, application/json"), new Gson().toJson(request)))
                .flatMap(body -> RetrofitHelper.getGoodsAPI().postGoodsComment(body))
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Log.d("submitComment", ": success");
                }, throwable -> {
                    Log.d("submitComment", ":fail ");
                }, ()-> changeOrderStatus(new UpdateOrderRequest(), 4));
    });

    /**
     * 改变订单状态
     */
    private void changeOrderStatus(UpdateOrderRequest request, int newStatus) {
        if (request != null) {
            request.setOrderCode(order.getOrderCode());
            request.setUserCode(order.getUserCode());
            request.setOrderStatus(newStatus);
            request.setPickSelf(order.getPickSelf());

            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(request));

            RetrofitHelper.getOrderAPI()
                    .updateOrder(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        fragment.getFragmentManager().popBackStack();
                        //  通知刷新订单列表
                        Intent intent = new Intent("updateOrderList");
                        LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                    }, throwable -> {
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * 商品列表的VM
     */
    public class ItemGoodsVM implements ViewModel {

        private OrderDetail detail;

        public final ObservableField<String> goodsImg = new ObservableField<>();
        public final ObservableField<Float> rating = new ObservableField<>();
        public final ObservableField<String> content = new ObservableField<>();

        public ItemGoodsVM(OrderDetail detail) {
            this.detail = detail;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            if (detail != null) {
                if (detail.getGoods() != null) {
                    if (!TextUtils.isEmpty(detail.getGoods().getCovers())) {
                        List<String> covers = new Gson().fromJson(detail.getGoods().getCovers(), new TypeToken<List<String>>() {
                        }.getType());
                        if (covers != null && covers.size() > 0) goodsImg.set(covers.get(0));
                    }
                }
            }

            rating.set(3f);
        }

        public String getGoodsCode() {
            return detail.getGoodsCode();
        }
    }
}
