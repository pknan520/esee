package com.nong.nongo2o.module.merchant.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.module.merchant.fragment.MerchantSaleRecordFragment;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.FocusUtils;
import com.nong.nongo2o.uils.imUtils.IMUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by PANYJ7 on 2018-3-6.
 */

public class MerchantSaleRecordVM implements ViewModel {

    private MerchantSaleRecordFragment fragment;
    private Goods good;

    //  销售列表
    public final ObservableList<ItemSaleRecordVM> itemRecordVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemSaleRecordVM> itemRecordBinding = ItemBinding.of(BR.viewModel, R.layout.item_sale_record);

    private int total;
    private int pageSize = 20;
    private String starStr;

    public MerchantSaleRecordVM(MerchantSaleRecordFragment fragment, Goods good) {
        this.fragment = fragment;
        this.good = good;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getRecordList(1);
    }

    private void getRecordList(int page) {
        RetrofitHelper.getGoodsAPI()
                .getSaleRecordList(page, pageSize, good.getGoodsCode())
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    total = resp.getTotal();
                    for (Order order : resp.getRows()) {
                        itemRecordVMs.add(new ItemSaleRecordVM(order));
                    }
                    viewStyle.isEmpty.set(itemRecordVMs.isEmpty());
                }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * 上拉加载
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(integer -> {
        loadMoreData();
    });

    private void loadMoreData() {
        if (itemRecordVMs.size() < total) {
            getRecordList(itemRecordVMs.size() / pageSize + 1);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    public class ItemSaleRecordVM implements ViewModel {

        private Order order;
        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_default_60;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();
        @DrawableRes
        public final int isFocus = R.mipmap.icon_focus_p;
        @DrawableRes
        public final int unFocus = R.mipmap.icon_focus;

        public ItemSaleRecordVM(Order order) {
            this.order = order;

            initData();
        }

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean hasFocus = new ObservableBoolean(false);
        }

        /**
         * 初始化数据
         */
        private void initData() {
            headUri.set(order.getUser().getAvatar());
            name.set(order.getUser().getUserNick());
            summary.set("在" + new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(order.getCreateTime()) + "购买了此商品");

            viewStyle.hasFocus.set(FocusUtils.checkIsFocus(order.getUser().getUserCode()));
        }

        /**
         * 查看个人主页
         */
        public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
            fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), order.getUser()));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });

        public final ReplyCommand contactClick = new ReplyCommand(() -> {
            IMUtils.checkIMLogin(isSuccess -> {
                if (isSuccess) {
                    String userName = order.getUser().getId();
                    if (userName.equals(EMClient.getInstance().getCurrentUser())) {
                        Toast.makeText(fragment.getActivity(), "您不能自言自语了啦^.^", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
                    intent.putExtra("userId", userName);
                    fragment.getActivity().startActivity(intent);
                    fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                } else {
                    Toast.makeText(fragment.getActivity(), "聊天可能有点问题，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            });
        });

        public final ReplyCommand focusOrNotClick = new ReplyCommand(() -> {
            FocusUtils.changeFocus(fragment.getActivity(), viewStyle.hasFocus.get(), order.getUser().getUserCode(), viewStyle.hasFocus::set);
        });
    }
}
