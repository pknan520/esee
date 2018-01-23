package com.nong.nongo2o.module.main.viewModel.message;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.EaseUserInfo;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.User;
import com.nong.nongo2o.greenDaoGen.EaseUserInfoDao;
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
import com.nong.nongo2o.module.login.LoginActivity;
import com.nong.nongo2o.module.main.MainActivity;
import com.nong.nongo2o.module.main.fragment.message.MessageListFragment;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.MyTimeUtils;
import com.nong.nongo2o.uils.dbUtils.GreenDaoManager;
import com.nong.nongo2o.uils.imUtils.IMCallback;
import com.nong.nongo2o.uils.imUtils.IMUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-19.
 */

public class MessageListVM implements ViewModel {

    public static final int MSG_REFRESH = 0;

    private MessageListFragment fragment;
    //  消息列表
    public final ObservableList<ItemMsgListVM> itemMsgListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemMsgListVM> itemMsgListBinding = ItemBinding.of(BR.viewModel, R.layout.item_msg_list);

    private EaseUserInfoDao easeDao;

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;
    @DrawableRes
    public final int notLoginImg = R.mipmap.default_error;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    loadConversationList(true);

                    viewStyle.isRefreshing.set(false);
                    break;
                default:
                    break;
            }
        }
    };

    public MessageListVM(MessageListFragment fragment) {
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
        easeDao = GreenDaoManager.getInstance().getSession().getEaseUserInfoDao();

        viewStyle.notLogin.set(TextUtils.isEmpty(UserInfo.getInstance().getSessionToken()));
        if (!viewStyle.notLogin.get()) loadConversationList(true);

        EMClient.getInstance().chatManager().addMessageListener(emListener);
    }

    private EMMessageListener emListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //  收到消息
            refresh();
            ((MainActivity) fragment.getActivity()).showMessagePoint();
            for (EMMessage msg : messages) {
                EaseUserInfo info = new EaseUserInfo();
                info.setUserId(msg.getStringAttribute("userCode", ""));
                info.setUserNick(msg.getStringAttribute("userNick", ""));
                info.setAvatar(msg.getStringAttribute("avatar", ""));
                easeDao.insertOrReplace(info);
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //  收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //  收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            //  收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //  消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //  消息状态变动
        }
    };

    public EMMessageListener getEmListener() {
        return emListener;
    }

    /**
     * 读取聊天信息
     */
    private void loadConversationList(boolean force) {
        IMUtils.checkIMLogin(isSuccess -> {
            // get all conversations
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            viewStyle.isEmpty.set(conversations.isEmpty());

            if (!viewStyle.isEmpty.get()) {
                List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
                /**
                 * lastMsgTime will change if there is new message during sorting
                 * so use synchronized to make sure timestamp of last message won't change.
                 */
                synchronized (conversations) {
                    for (EMConversation conversation : conversations.values()) {
                        if (conversation.getAllMessages().size() != 0) {
                            sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                        }
                    }
                }
                try {
                    // Internal is TimSort algorithm, has bug
                    sortConversationByLastChatTime(sortList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (force) itemMsgListVMs.clear();
                for (Pair<Long, EMConversation> sortItem : sortList) {
                    itemMsgListVMs.add(new ItemMsgListVM(sortItem.second));
                }
            }
        });
    }

    /**
     * 聊天信息排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, (con1, con2) -> {
            if (con1.first.equals(con2.first)) {
                return 0;
            } else if (con2.first > con1.first) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    /**
     * 下拉刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::refresh);

    public void refresh() {
        viewStyle.isRefreshing.set(true);

        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * 上拉加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> loadMoreData());

    private void loadMoreData() {
//        for (int i = 0; i < 10; i++) {
//            itemMsgListVMs.add(new ItemMsgListVM(false));
//        }
    }

    /**
     * 无登录按钮
     */
    public final ReplyCommand errorClick = new ReplyCommand(() -> {
        if (viewStyle.notLogin.get()) {
            fragment.getActivity().startActivity(LoginActivity.newIntent(fragment.getActivity(), true));
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        }
    });

    /**
     * 检查未读消息
     */
    public void checkUnreadMessage() {
        IMUtils.checkIMLogin(isSuccess -> {
            if (isSuccess) {
                int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
                if (unreadMessageCount > 0) ((MainActivity)fragment.getActivity()).showMessagePoint();
                else ((MainActivity)fragment.getActivity()).hideMessagePoint();
            } else {
                ((MainActivity)fragment.getActivity()).hideMessagePoint();
            }
        });
    }

    /**
     * 消息Item
     */
    public class ItemMsgListVM implements ViewModel {

        private EMConversation conversation;
        private EMMessage lastMessage;

        public final ObservableField<Integer> badgeText = new ObservableField<>();
        @DrawableRes
        public final int sysMsgPlaceHolder = R.mipmap.xiaoxi_news_default;
        @DrawableRes
        public final int headPlaceHolder = R.mipmap.head_xiaoxi_80;
        public final ObservableField<String> headUri = new ObservableField<>();
        public final ObservableField<String> name = new ObservableField<>();
        public final ObservableField<String> lastMsg = new ObservableField<>();
        public final ObservableField<String> date = new ObservableField<>();

        public ItemMsgListVM(EMConversation conversation) {
            this.conversation = conversation;

            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            lastMessage = conversation.getLastMessage();
            switch (lastMessage.getType()) {
                case TXT:
                    lastMsg.set(((EMTextMessageBody) lastMessage.getBody()).getMessage());
                    break;
                case IMAGE:
                    lastMsg.set("[图片]");
                    break;
                case VIDEO:
                    lastMsg.set("[视频]");
                    break;
                case LOCATION:
                    lastMsg.set("[地点]");
                    break;
                case VOICE:
                    lastMsg.set("[音频]");
                    break;
                case FILE:
                    lastMsg.set("[文件]");
                    break;
                case CMD:
                    lastMsg.set("[指令]");
                    break;
            }

            badgeText.set(conversation.getUnreadMsgCount());

            setUserInfo(lastMessage.getFrom().equals(EMClient.getInstance().getCurrentUser()) ? lastMessage.getTo() : lastMessage.getFrom());

            date.set(MyTimeUtils.formatTime(lastMessage.getMsgTime()));
        }

        private void setUserInfo(String userName) {
            EaseUserInfo info = easeDao.load(userName);

            if (info != null) {
                headUri.set(info.getAvatar());
                name.set(info.getUserNick());
            } else if (conversation.getLatestMessageFromOthers() != null) {
                headUri.set(conversation.getLatestMessageFromOthers().getStringAttribute("avatar", ""));
                name.set(conversation.getLatestMessageFromOthers().getStringAttribute("userNick", ""));

                info = new EaseUserInfo();
                info.setUserId(userName);
                info.setAvatar(headUri.get());
                info.setUserNick(name.get());
                easeDao.insertOrReplace(info);
            } else {
                RetrofitHelper.getUserAPI()
                        .profile(userName)
                        .subscribeOn(Schedulers.io())
                        .map(new ApiResponseFunc<>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            headUri.set(user.getAvatar());
                            name.set(user.getUserNick());

                            EaseUserInfo newInfo = new EaseUserInfo();
                            newInfo.setUserId(userName);
                            newInfo.setAvatar(headUri.get());
                            newInfo.setUserNick(name.get());
                            easeDao.insertOrReplace(newInfo);
                        });
            }

        }

        /**
         * 查看消息详情
         */
        public final ReplyCommand itemClick = new ReplyCommand(this::checkMsgDetail);

        private void checkMsgDetail() {
//            if (viewStyle.isSysMsg.get()) {
//                //  系统消息
//                fragment.getActivity().startActivity(SysMsgActivity.newIntent(fragment.getActivity()));
//            } else {
//                //  聊天消息
//            }
            String userName = conversation.conversationId();
            if (userName.equals(EMClient.getInstance().getCurrentUser())) {
                Toast.makeText(fragment.getActivity(), "您不能自言自语了啦^.^", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(fragment.getActivity(), ChatActivity.class);
            intent.putExtra("userId", userName);
            fragment.getActivity().startActivity(intent);
            fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
        }
    }
}
