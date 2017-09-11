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
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderListVM implements ViewModel {

    private OrderListFragment fragment;
    public final ObservableField<String> status = new ObservableField<>();

    public OrderListVM(OrderListFragment fragment, int status) {
        this.fragment = fragment;
        this.status.set(String.valueOf(status));

        for (int i = 0; i < 20; i++) {
            itemOrderVMs.add(new ItemOrderVM());
        }
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        viewStyle.isRefreshing.set(true);

        Observable.just(0)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        for (int i = 0; i < 10; i++) {
            itemOrderVMs.add(new ItemOrderVM());
        }
    }

    /**
     * 订单列表
     */
    public final ObservableList<ItemOrderVM> itemOrderVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemOrderVM> itemOrderBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_list);

    public class ItemOrderVM implements ViewModel {

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

        public ItemOrderVM() {

            initFakeData();
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            name.set("果酱妈咪09");
            summary.set("这家伙很懒，什么都没留下~");
            status.set("已发货");

            for (int i = 0; i < (int) (Math.random() * 2) + 1; i++) {
                itemOrderGoodsListVMs.add(new ItemOrderGoodsListVM(ItemOrderGoodsListVM.FROM_ORDER_LIST, fragment));
            }

            orderInfo.set("共n件，合计¥58.80（含运费¥10.00）");

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
