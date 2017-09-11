package com.nong.nongo2o.module.main.viewModel.message;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.module.main.fragment.message.MessageListFragment;
import com.nong.nongo2o.module.message.activity.SysMsgActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-19.
 */

public class MessageListVM implements ViewModel {

    private MessageListFragment fragment;
    //  消息列表
    public final ObservableList<ItemMsgListVM> itemMsgListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMsgListVM> itemMsgListBinding = ItemBinding.of(BR.viewModel, R.layout.item_msg_list);

    public MessageListVM(MessageListFragment fragment) {
        this.fragment = fragment;

        itemMsgListVMs.add(new ItemMsgListVM(true));
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
            itemMsgListVMs.add(new ItemMsgListVM(false));
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
            itemMsgListVMs.add(new ItemMsgListVM(false));
        }
    }

    /**
     * 消息Item
     */
    public class ItemMsgListVM implements ViewModel {

        public final ObservableField<String> badgeText = new ObservableField<>();
        @DrawableRes
        public final int sysMsgPlaceHolder = R.mipmap.xiaoxi_news_default;
        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_xiaoxi_80;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> lastMsg = new ObservableField<>();
        public final ObservableField<String> date = new ObservableField<>();

        public ItemMsgListVM(boolean isSysMsg) {
            viewStyle.isSysMsg.set(isSysMsg);

            initFakeData();
        }

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean isSysMsg = new ObservableBoolean(false);
        }

        /**
         * 假数据
         */
        private void initFakeData() {
            badgeText.set(String.valueOf((int) (Math.random() * 100)));
            name.set(viewStyle.isSysMsg.get() ? "系统消息" : "宝宝不哭");
            lastMsg.set("宝宝心里难受，宝宝不说~");
            date.set("一个月前");
        }

        /**
         * 查看消息详情
         */
        public final ReplyCommand itemClick = new ReplyCommand(this::checkMsgDetail);

        private void checkMsgDetail() {
            if (viewStyle.isSysMsg.get()) {
                //  系统消息
                fragment.getActivity().startActivity(SysMsgActivity.newIntent(fragment.getActivity()));
            } else {
                //  聊天消息
            }
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        }
    }
}
