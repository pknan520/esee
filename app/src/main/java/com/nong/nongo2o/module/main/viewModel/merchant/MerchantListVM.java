package com.nong.nongo2o.module.main.viewModel.merchant;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.common.viewModel.ItemMerchantListVM;
import com.nong.nongo2o.module.main.fragment.merchant.MerchantListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-23.
 */

public class MerchantListVM implements ViewModel {

    //  假数据图片uri
    private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

    private MerchantListFragment fragment;
    public final ObservableList<DefaultSliderView> sliderList = new ObservableArrayList<>();
    public final ObservableField<String> adText = new ObservableField<>();

    public MerchantListVM(MerchantListFragment fragment) {
        this.fragment = fragment;

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
        addSliderView();

        adText.set("活动期间注册账户将有机会获得奖励");

        for (int i = 0; i < 20; i++) {
            itemMerchantListVMs.add(new ItemMerchantListVM(fragment));
        }
    }

    /**
     * 添加轮播图
     */
    private void addSliderView() {
        for (String anUriArray : uriArray) {
            DefaultSliderView view = new DefaultSliderView(fragment.getActivity());
            view.image(anUriArray)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderList.add(view);
        }
    }

    /**
     * 商家列表
     */
    public final ObservableList<ItemMerchantListVM> itemMerchantListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMerchantListVM> itemMerchantBinding = ItemBinding.of(BR.viewModel, R.layout.item_merchant_list);

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        viewStyle.isRefreshing.set(true);

        Observable.just(0)
                .delay(3000, TimeUnit.MILLISECONDS)
                .subscribe(integer -> {
                    viewStyle.isRefreshing.set(false);
                });
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> {
        for (int i = 0; i < 10; i++) {
            itemMerchantListVMs.add(new ItemMerchantListVM(fragment));
        }
    });

}
