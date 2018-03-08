package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.module.common.viewModel.ItemSelectGoodsVM;
import com.nong.nongo2o.module.dynamic.fragment.DynamicGoodsListFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.fragment.PersonalBuyFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by PANYJ7 on 2018-3-5.
 */

public class PersonalBuyVM implements ViewModel {

    private PersonalBuyFragment fragment;
    private SimpleUser user;
    //  动态列表
    public final ObservableList<ItemPersonalBuyVM> itemBuyList = new ObservableArrayList<>();
    public final ItemBinding<ItemPersonalBuyVM> itemBuyBinding = ItemBinding.of(BR.viewModel, R.layout.item_personal_buy);

    private int total = 0;
    private int pageSize = 10;
    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;

    public PersonalBuyVM(PersonalBuyFragment fragment, SimpleUser user) {
        this.fragment = fragment;
        this.user = user;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getBuyList(1, true);
    }

    /**
     * 获取动态列表
     */
    private void getBuyList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getOrderAPI()
                .getBuyRecord(page, pageSize, user.getUserCode())
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) itemBuyList.clear();
                    total = resp.getTotal();
                    for (Order order: resp.getRows()) {
                        for (OrderDetail detail : order.getOrderDetails()) {
                            itemBuyList.add(new ItemPersonalBuyVM(detail.getGoods()));
                        }
                    }
                    viewStyle.isEmpty.set(itemBuyList.isEmpty());
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        getBuyList(1, true);
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemBuyList.size() < total) {
            getBuyList(itemBuyList.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }

    public class ItemPersonalBuyVM implements ViewModel {

        private Goods good;

        @DrawableRes
        public final int imgPlaceHolder = R.mipmap.picture_default;
        public final ObservableField<String> imgUrl = new ObservableField<>();
        public final ObservableField<String> goodsName = new ObservableField<>();
        public final ObservableField<String> goodsSummary = new ObservableField<>();
        public final ObservableField<BigDecimal> goodsPrice = new ObservableField<>();

        public ItemPersonalBuyVM(Goods good) {
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
                goodsSummary.set(good.getGoodsSpecs().get(0).getTitle());
                goodsPrice.set(good.getGoodsSpecs().get(0).getPrice());
            }
        }

        public final ReplyCommand itemClick = new ReplyCommand(() -> {
            fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity(), good));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });
    }
}
