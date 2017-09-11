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
import com.nong.nongo2o.module.personal.fragment.GoodsManagerDetailFragment;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerFragment;
import com.nong.nongo2o.module.personal.fragment.GoodsManagerTotalFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-26.
 */

public class GoodsManagerVM implements ViewModel {

    private GoodsManagerFragment fragment;
    public final ObservableField<Integer> status = new ObservableField<>();
    //  商品列表
    public final ObservableList<ItemGoodsVM> itemGoodsVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemGoodsVM> itemGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_goods_manager);

    public GoodsManagerVM(GoodsManagerFragment fragment, int status) {
        this.fragment = fragment;
        this.status.set(status);

        initFakeData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initFakeData() {

        for (int i = 0; i < 10; i++) {
            itemGoodsVMs.add(new ItemGoodsVM());
        }
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
            itemGoodsVMs.add(new ItemGoodsVM());
        }
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

        //  假数据图片uri
        private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
                "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

        @DrawableRes
        public final int imgPlaceHolder = R.mipmap.picture_default;
        public final ObservableField<String> imgUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<Double> price = new ObservableField<>();
        public final ObservableField<Integer> saleNum = new ObservableField<>();
        public final ObservableField<Integer> stockNum = new ObservableField<>();
        public final ObservableField<String> date = new ObservableField<>();

        public ItemGoodsVM() {

            initFakeDate();
        }

        /**
         * 假数据
         */
        private void initFakeDate() {
            imgUri.set(uriArray[(int) (Math.random() * 4)]);
            name.set("墨西哥进口牛油果");
            price.set(48.80);
            saleNum.set(128);
            stockNum.set(293);
            date.set("2017-07-05");
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
