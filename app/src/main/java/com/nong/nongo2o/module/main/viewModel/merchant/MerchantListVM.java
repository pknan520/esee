package com.nong.nongo2o.module.main.viewModel.merchant;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.bean.SalerInfo;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.Activity;
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
import com.nong.nongo2o.module.common.viewModel.ItemMerchantListVM;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.fragment.merchant.MerchantFragment;
import com.nong.nongo2o.module.main.fragment.merchant.MerchantListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-23.
 */

public class MerchantListVM implements ViewModel {

    //  假数据图片uri
    private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

    private MerchantListFragment fragment;
    private int type;
    public final ObservableList<DefaultSliderView> sliderList = new ObservableArrayList<>();
    public final ObservableField<String> adText = new ObservableField<>();
    // 商家列表
    public final ObservableList<ItemMerchantListVM> itemMerchantListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMerchantListVM> itemMerchantBinding = ItemBinding.of(BR.viewModel, R.layout.item_merchant_list);

    @DrawableRes
    public final int emptyImg = R.mipmap.news_guanzhu_default;
    @DrawableRes
    public final int notLoginImg = R.mipmap.default_error;

    private int pageSize = 10;
    private int total = 0;

    public MerchantListVM(MerchantListFragment fragment, int type) {
        this.fragment = fragment;
        this.type = type;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
        public final ObservableBoolean isMyFocus = new ObservableBoolean(false);

        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        public final ObservableBoolean notLogin = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        viewStyle.isMyFocus.set(type == 1);
        viewStyle.notLogin.set(type == 1 && TextUtils.isEmpty(UserInfo.getInstance().getSessionToken()));

        getActivities(1);
        adText.set("活动期间注册账户将有机会获得奖励");

        if (!viewStyle.notLogin.get()) getMerchantList(1, true);
    }

    /**
     * 获取活动内容
     */
    private void getActivities(int page) {
        RetrofitHelper.getActivityAPI()
                .getActivities(page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (resp != null && !resp.getRows().isEmpty()) {
                        addSliderView(resp.getRows());
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * 添加轮播图
     */
    private void addSliderView(List<Activity> activities) {
//        for (String anUriArray : uriArray) {
//            DefaultSliderView view = new DefaultSliderView(fragment.getActivity());
//            view.image(anUriArray)
//                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
//            sliderList.add(view);
//        }
        for (Activity activity : activities) {
            DefaultSliderView view = new DefaultSliderView(fragment.getActivity());
            view.image(activity.getCover())
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderList.add(view);
        }
    }

    private void getMerchantList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        if (type == 1) {
            RetrofitHelper.getGoodsAPI()
                    .getFocusSalerInfos(page, pageSize)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (force) {
                            itemMerchantListVMs.clear();
                        }
                        total = resp.getTotal();
                        for (SalerInfo saller : resp.getRows()) {
                            itemMerchantListVMs.add(new ItemMerchantListVM(fragment, saller));
                        }
                        viewStyle.isEmpty.set(itemMerchantListVMs.isEmpty());
                    }, throwable -> {
                        viewStyle.isRefreshing.set(false);
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }, () -> viewStyle.isRefreshing.set(false));
        } else if (type == 2) {
            RetrofitHelper.getGoodsAPI()
                    .getAllSalerInfos(page, pageSize)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        if (force) {
                            itemMerchantListVMs.clear();
                        }
                        total = resp.getTotal();
                        for (SalerInfo saller : resp.getRows()) {
                            itemMerchantListVMs.add(new ItemMerchantListVM(fragment, saller));
                        }
                        viewStyle.isEmpty.set(itemMerchantListVMs.isEmpty());
                    }, throwable -> {
                        viewStyle.isRefreshing.set(false);
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }, () -> viewStyle.isRefreshing.set(false));
        }
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        if (!viewStyle.notLogin.get()) getMerchantList(1, true);
        else viewStyle.isRefreshing.set(false);
    }

    /**
     * NestedScrollView滑动到底部的监听
     */
    public final ReplyCommand onScrollBottomCommand = new ReplyCommand(this::onLoadMoreCommand);

    /**
     * 加载更多
     */
    private void onLoadMoreCommand() {
        if (itemMerchantListVMs.size() < total) {
            getMerchantList(itemMerchantListVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容啦^.^", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 空白或无登录按钮
     */
    public final ReplyCommand errorClick = new ReplyCommand(() -> {
        if (viewStyle.notLogin.get()) {
            fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), true));
        } else {
//            fragment.getActivity().startActivity(AddFocusActivity.newIntent(fragment.getActivity()));
            ((MerchantFragment) fragment.getParentFragment()).switchToAll();
        }
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });
}
