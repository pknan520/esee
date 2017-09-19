package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entity.domain.Follow;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListVM implements ViewModel {

    private OrderListFragment fragment;
    public final ObservableField<String> status = new ObservableField<>();
    public final ObservableField<Boolean> isMerchantMode = new ObservableField<>();

    private int page = 1;
    private int pageSize = 10;
    private int total = 0;



    public OrderListVM(OrderListFragment fragment, int status,boolean isMerchantMode) {
        this.fragment = fragment;
        this.status.set(String.valueOf(status));
        this.isMerchantMode.set(isMerchantMode);
        initData();
    }


    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 初始数据
     */
    private void initData() {
        searchDate(true);
    }
    private void searchDate(boolean force){
        if(force){
            viewStyle.isRefreshing.set(true);
            page = 1;
        }
        RetrofitHelper.getOrderAPI()
                .userOrderSearch(("-99".equals(status.get()) ? null :Integer.parseInt(status.get())),isMerchantMode.get() ? 1 : 0,page,pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if(force){
                        itemOrderVMs.clear();
                    }
                    total = resp.getTotal();
                    for (Order order : resp.getRows()) {
                        itemOrderVMs.add(new ItemOrderVM(order));
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> {
                    page ++;
                    viewStyle.isRefreshing.set(false);
                });
    }
    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        viewStyle.isRefreshing.set(true);
        searchDate(true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if(total > (page-1) * pageSize){
            searchDate(false);
        }
    }

    /**
     * 订单列表
     */
    public final ObservableList<ItemOrderVM> itemOrderVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemOrderVM> itemOrderBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_list);

    public class ItemOrderVM implements ViewModel {

        private Order order;
        //  商家信息
        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_default_60;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();
        public final ObservableField<String> status = new ObservableField<>();
        //  商品列表
        public final ObservableList<ItemOrderGoodsListVM> itemOrderGoodsListVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemOrderGoodsListVM> itemOrderGoodsListBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_goods_list);
        //  订单信息
        public final ObservableField<String> orderInfo = new ObservableField<>();
        //  操作按钮
        public final ObservableField<String> btnStr = new ObservableField<>();

        public ItemOrderVM(Order order) {
            this.order = order;
            initFakeData();
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            name.set(order.getUser().getUserNick());
            summary.set(order.getUser().getProfile());
            switch (order.getOrderStatus()){
                case -1:
                    status.set("已取消");
                    break;
                case 0:
                    status.set("待支付");
                    break;
                case 1:
                    status.set("待发货");
                    break;
                case 2:
                    status.set("待收货");
                    break;
                case 3:
                    status.set("待评价");
                    break;
                case 4:
                    status.set("已完成");
                    break;
            }

            int goodNum = 0;
            for(OrderDetail orderDetail : order.getOrderDetails()){
                goodNum += orderDetail.getGoodsNum();

                itemOrderGoodsListVMs.add(new ItemOrderGoodsListVM(orderDetail,ItemOrderGoodsListVM.FROM_ORDER_LIST, fragment));
            }

            orderInfo.set("共"+goodNum+"件，合计¥ "+order.getTotalPrice());

            btnStr.set("操作按钮");
        }

        /**
         * 查看订单详情
         */
        public final ReplyCommand orderDetailClick = new ReplyCommand(() -> {
            ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                    OrderDetailFragment.newInstance(), OrderDetailFragment.TAG);
        });

        /**
         * 查看商家主页
         */
        public final ReplyCommand personalHomeClick = new ReplyCommand(new Action() {
            @Override
            public void run() throws Exception {
                fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
            }
        });

        /**
         * 订单操作
         */
        public final ReplyCommand operateClick = new ReplyCommand(() -> {
            Toast.makeText(fragment.getActivity(), "你点击了" + btnStr.get(), Toast.LENGTH_SHORT).show();
        });
    }
}
