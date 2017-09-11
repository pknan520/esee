package com.nong.nongo2o.module.message.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.message.fragment.SysMsgListFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-19.
 */

public class SysMsgListVM implements ViewModel {

    private SysMsgListFragment fragment;
    //  系统消息
    public final ObservableList<ItemSysMsgListVM> itemSysMsgListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemSysMsgListVM> itemSysMsgListBinding = ItemBinding.of(BR.viewModel, R.layout.item_sys_msg_list);

    public SysMsgListVM(SysMsgListFragment fragment) {
        this.fragment = fragment;

        initView();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 假数据
     */
    private void initView() {
        for (int i = 0; i < 10; i++) {
            itemSysMsgListVMs.add(new ItemSysMsgListVM());
        }
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refreshData);

    private void refreshData() {
        viewStyle.isRefreshing.set(true);

        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    viewStyle.isRefreshing.set(false);
                });
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
        for (int i = 0; i < 10; i++) {
            itemSysMsgListVMs.add(new ItemSysMsgListVM());
        }
    }

    /**
     * 系统消息的item
     */
    public class ItemSysMsgListVM implements  ViewModel {

        @DrawableRes
        private final int[] bgArray = {R.drawable.shape_round_primary_button, R.drawable.shape_round_orange, R.drawable.shape_round_green};
        private final String[] typeStrArray = {"订单", "评论", "赞"};

        public final ObservableField<Integer> type = new ObservableField<>();
        public final ObservableField<String> date = new ObservableField<>();
        public final ObservableField<Drawable> bg = new ObservableField<>();
        public final ObservableField<String> typeStr = new ObservableField<>();
        public final ObservableField<String> summary = new ObservableField<>();
        public final ObservableField<String> nameTarget = new ObservableField<>();
        public final ObservableField<String> detail = new ObservableField<>();

        public ItemSysMsgListVM() {

            initFakeData();
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            type.set((int) (Math.random() * 3 - 0.01));
            date.set("2017-07-19 11:27");
            bg.set(ContextCompat.getDrawable(fragment.getActivity(), bgArray[type.get()]));
            typeStr.set(typeStrArray[type.get()]);
            summary.set("您有新的订单");
            nameTarget.set("NeilsonLo");
            detail.set("刚刚购买了你的宝贝");
        }

        /**
         * 查看详情
         */
        public final ReplyCommand detailClick = new ReplyCommand(() -> {

        });
    }
}
