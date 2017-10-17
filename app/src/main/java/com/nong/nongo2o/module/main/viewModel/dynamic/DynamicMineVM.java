package com.nong.nongo2o.module.main.viewModel.dynamic;

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
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entities.response.DynamicContent;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.common.viewModel.ItemDynamicListVM;
import com.nong.nongo2o.module.dynamic.activity.DynamicDetailActivity;
import com.nong.nongo2o.module.dynamic.activity.DynamicPublishActivity;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.fragment.dynamic.DynamicFragment;
import com.nong.nongo2o.module.main.fragment.dynamic.DynamicMineFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-6.
 */

public class DynamicMineVM implements ViewModel {

    private DynamicMineFragment fragment;

    private int pageSize = 10;
    private int total = 0;

    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    public final ObservableField<Integer> dynamicNum = new ObservableField<>();

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    @DrawableRes
    public final int notLoginImg = R.mipmap.default_error;

    public DynamicMineVM(DynamicMineFragment fragment) {
        this.fragment = fragment;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
        public final ObservableBoolean notLogin = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        viewStyle.notLogin.set(TextUtils.isEmpty(UserInfo.getInstance().getSessionToken()));
        if (!viewStyle.notLogin.get()) {
            headUri.set(UserInfo.getInstance().getAvatar());
            name.set(UserInfo.getInstance().getUserNick());
            summary.set(UserInfo.getInstance().getProfile());

            getDynamicList(1, true);
        }
    }

    /**
     * 我的动态列表
     */
    public final ObservableList<ItemDynamicMineVM> itemDynamicMineVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemDynamicMineVM> itemDynamicMineBinding = ItemBinding.of(BR.viewModel, R.layout.item_dynamic_list_mine);

    public class ItemDynamicMineVM implements ViewModel {

        private Moment dynamic;

        @DrawableRes
        public final int imgPlaceHolder = R.mipmap.ic_launcher;
        public final ObservableField<String> date = new ObservableField<>();
        public final ObservableField<String> title = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();
        public final ObservableField<String> imgUri = new ObservableField<>();

        public ItemDynamicMineVM(Moment dynamic) {
            this.dynamic = dynamic;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            Calendar createDate = Calendar.getInstance();
            createDate.setTime(dynamic.getCreateTime());
            date.set(new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA).format(createDate.getTime()));

            List<String> headerImgList = new Gson().fromJson(dynamic.getHeaderImg(), new TypeToken<List<String>>() {
            }.getType());
            imgUri.set(headerImgList.get(0));

            title.set(dynamic.getTitle());

            List<DynamicContent> contentList = new Gson().fromJson(dynamic.getContent(), new TypeToken<List<DynamicContent>>() {
            }.getType());
            summary.set(contentList.get(0).getContent());
        }

        /**
         * 编辑按钮
         */
        public final ReplyCommand editClick = new ReplyCommand(() -> {
            fragment.getActivity().startActivity(DynamicPublishActivity.newIntent(fragment.getActivity(), dynamic));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });

        /**
         * 删除按钮
         */
        public final ReplyCommand deleteClick = new ReplyCommand(() -> {
            ((RxBaseActivity) fragment.getActivity()).showDeleteDialog(this::deleteDynamic);
        });

        private void deleteDynamic() {
            viewStyle.isRefreshing.set(true);

            RetrofitHelper.getDynamicAPI()
                    .deleteDynamic(dynamic.getMomentCode())
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        itemDynamicMineVMs.remove(this);

                        total--;
                        dynamicNum.set(total);

                        viewStyle.isEmpty.set(itemDynamicMineVMs.isEmpty());
                    }, throwable -> {
                        viewStyle.isRefreshing.set(false);
                        Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }, () -> viewStyle.isRefreshing.set(false));
        }

        /**
         * 查看详情
         */
        public final ReplyCommand detailClick = new ReplyCommand(() -> {
            fragment.getActivity().startActivity(DynamicDetailActivity.newIntent(fragment.getActivity(), dynamic));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        });
    }

    /**
     * 获取动态列表
     */
    private void getDynamicList(int page, boolean force) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getDynamicAPI()
                .getMyDynamicList(page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) itemDynamicMineVMs.clear();
                    total = resp.getTotal();
                    dynamicNum.set(total);
                    for (Moment dynamic : resp.getRows()) {
                        itemDynamicMineVMs.add(new ItemDynamicMineVM(dynamic));
                    }
                    viewStyle.isEmpty.set(itemDynamicMineVMs.isEmpty());
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
        if (!viewStyle.notLogin.get()) getDynamicList(1, true);
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        if (itemDynamicMineVMs.size() < total) {
            getDynamicList(itemDynamicMineVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容啦^.^", Toast.LENGTH_SHORT).show();
        }
    }

    public final ReplyCommand errorClick = new ReplyCommand(() -> {
        if (viewStyle.notLogin.get()) {
            fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), true));
        } else {
            fragment.getActivity().startActivity(DynamicPublishActivity.newIntent(fragment.getActivity()));
        }
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });
}
