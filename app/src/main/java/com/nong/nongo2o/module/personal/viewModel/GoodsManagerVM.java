package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.entity.domain.Follow;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.personal.activity.FansMgrActivity;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerDetailFragment;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerFragment;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerTotalFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-26.
 */

public class GoodsManagerVM implements ViewModel {

    private int page = 1;
    private final int pageSize = 10;
    private int total;

    private GoodsManagerFragment fragment;
    public final ObservableField<Integer> status = new ObservableField<>();
    //  商品列表
    public final ObservableList<ItemGoodsVM> itemGoodsVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemGoodsVM> itemGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_manager);

    public GoodsManagerVM(GoodsManagerFragment fragment, int status) {
        this.fragment = fragment;
        this.status.set(status);

        initFakeData(true);
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData(boolean force) {
        if(force){
            viewStyle.isRefreshing.set(true);
            page = 1;
            total = 0;
            itemGoodsVMs.clear();
        }

        RetrofitHelper.getGoodsAPI()
                .userGoodsSearch(status.get(),page,pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    total = resp.getTotal();
                    for (Goods goods : resp.getRows()) {
                        itemGoodsVMs.add(new ItemGoodsVM( goods, status.get()));
                    }
                    if(total > page * pageSize){
                        page ++ ;
                    }else{
                        Toast.makeText(fragment.getActivity(), "数据已加载完毕", Toast.LENGTH_SHORT).show();
                    }
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
        initFakeData(true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        initFakeData(false);
    }

    /**
     * 添加商品
     */
    public final ReplyCommand addGoods = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                GoodsManagerDetailFragment.newInstance(), GoodsManagerDetailFragment.TAG);
    });

    /**
     * 商品列表的Item
     */
    public class ItemGoodsVM implements ViewModel {
        private Goods goods;
        private int status;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //  假数据图片uri
        private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

        @DrawableRes
        public final int imgPlaceHolder = R.mipmap.picture_default;
        public final ObservableField<String> imgUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<BigDecimal> price = new ObservableField<>();
        public final ObservableField<Integer> saleNum = new ObservableField<>();
        public final ObservableField<Integer> stockNum = new ObservableField<>();
        public final ObservableField<String> date = new ObservableField<>();

        public ItemGoodsVM(Goods goods,int status) {
            this.goods = goods;
            this.status = status;
            initFakeDate();
        }

        /**
         * 假数据
         */
        private void initFakeDate() {
            imgUri.set(uriArray[(int) (Math.random() * 4)]);
            name.set(goods.getTitle());
            price.set(goods.getPrice());
            saleNum.set(goods.getTotalSale());
            stockNum.set(0);
            date.set(simpleDateFormat.format(goods.getCreateTime()));
        }

        /**
         * 列表Item点击事件
         */
        public ReplyCommand itemDetailClick = new ReplyCommand(() -> {

        });

        /**
         * 弹出操作菜单
         */
        public final ReplyCommand operateClick = new ReplyCommand(this::showPopupMenu);

        private void showPopupMenu() {
            fragment.showPopupMenu();
        }
    }

    /**
     * 操作菜单的VM
     */
    public class PopupVM implements ViewModel {

        private final String[] putawayArray = {"编辑", "上架/下架", "移至顶部", "删除"};

        private PopupWindow popup;

        //操作菜单选项列表
        public final ObservableList<ItemPopupVM> itemPopupVMs = new ObservableArrayList<>();
        public final ItemBinding<ItemPopupVM> itemPopupBinding = ItemBinding.of(BR.viewModel, R.layout.item_popup_menu);

        public PopupVM(PopupWindow popup) {
            this.popup = popup;

            for (String putawayStr : putawayArray) {
                itemPopupVMs.add(new ItemPopupVM(putawayStr));
            }
        }

        /**
         * 取消点击事件
         */
        public final ReplyCommand cancelClick = new ReplyCommand(() -> popup.dismiss());

        /**
         * 操作菜单的Item
         */
        public class ItemPopupVM implements ViewModel {

            public final ObservableField<String> operateStr = new ObservableField<>();

            public ItemPopupVM(String operateStr) {
                this.operateStr.set(operateStr);
            }

            public final ReplyCommand itemPopupClick = new ReplyCommand(() -> {
                switch (operateStr.get()) {
                    case "编辑":
                        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                                GoodsManagerDetailFragment.newInstance(), GoodsManagerDetailFragment.TAG);
                        break;
                }
                popup.dismiss();
            });
        }
    }
}
