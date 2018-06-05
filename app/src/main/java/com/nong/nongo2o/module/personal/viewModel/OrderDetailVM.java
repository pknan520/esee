package com.nong.nongo2o.module.personal.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.domain.ImgTextContent;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.CreatePaymentRequest;
import com.nong.nongo2o.entity.request.UpdateOrderRequest;
import com.nong.nongo2o.module.common.buy.activity.BuyActivity;
import com.nong.nongo2o.module.common.buy.fragment.PayFragment;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.common.viewModel.ItemTransListVM;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;
import com.nong.nongo2o.module.personal.fragment.OrderEvaluateFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AddressUtils;
import com.nong.nongo2o.uils.BeanUtils;
import com.nong.nongo2o.uils.imUtils.IMUtils;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-29.
 */

public class OrderDetailVM implements ViewModel {

    private OrderDetailFragment fragment;
    private Order order;
    private UpdateOrderRequest request;
    private boolean isMerchantMode;

    //  订单状态
    public final ObservableField<String> orderNo = new ObservableField<>();
    public final ObservableField<String> orderStatus = new ObservableField<>();
    //  收货信息
    public final ObservableField<String> receiver = new ObservableField<>();
    public final ObservableField<String> receivePhone = new ObservableField<>();
    public final ObservableField<String> receiveAddr = new ObservableField<>();
    //  商家信息
    @DrawableRes
    public final int merchantHeadPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> merchantHeadUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    //  商品列表
    public final ObservableList<ItemOrderGoodsListVM> itemGoodsVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemOrderGoodsListVM> itemGoodsBinding = ItemBinding.of(BR.viewModel, R.layout.item_order_goods_list);
    //  运费
    public final ObservableField<BigDecimal> transFee = new ObservableField<>();
    // 订单费用
    public final ObservableField<String> orderInfo = new ObservableField<>();
    public final ObservableField<String> moneyInfo = new ObservableField<>();
    //  物流信息
    public final ObservableField<String> exName = new ObservableField<>();
    public final ObservableField<String> exNumber = new ObservableField<>();
    //  售后信息
    public final ObservableField<String> applyReason = new ObservableField<>();
    public final ObservableField<String> afterSaleImgUrl = new ObservableField<>();
    public final ObservableField<String> auditInfo = new ObservableField<>();
    public final ObservableField<BigDecimal> afterSaleMoney = new ObservableField<>();
    //  物流轨迹
//    public final ObservableList<ItemTransListVM> itemTransListVMs = new ObservableArrayList<>();
//    public final ItemBinding<ItemTransListVM> itemTransBinding = ItemBinding.of(BR.viewModel, R.layout.item_trans_list);
    //  按钮
    public final ObservableField<String> btnL = new ObservableField<>();
    public final ObservableField<String> btnR = new ObservableField<>();

    public OrderDetailVM(OrderDetailFragment fragment, Order order, boolean isMerchantMode) {
        this.fragment = fragment;
        this.order = order;
        this.isMerchantMode = isMerchantMode;

        iniData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean btnVisi = new ObservableBoolean(false);
        public final ObservableBoolean showLogisticsInfo = new ObservableBoolean(false);
        public final ObservableBoolean isSelf = new ObservableBoolean(false);

        public final ObservableBoolean showAfterSale = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void iniData() {
        btnL.set("");
        btnR.set("");
        if (order != null) {
            request = new UpdateOrderRequest();
            BeanUtils.Copy(request, order, false);

            itemGoodsVMs.clear();
            orderNo.set(order.getOrderCode());
            switch (order.getOrderStatus()) {
                case -1:
                    orderStatus.set("已取消");
                    break;
                case 0:
                    orderStatus.set("待支付");
                    btnL.set("取消订单");
                    if (!isMerchantMode)
                        btnR.set("付款");
                    break;
                case 1:
                    orderStatus.set("待发货");
                    if (isMerchantMode) {
                        btnL.set("退款");
                        btnR.set("确认发货");
                    } else {
                        btnL.set("取消订单");
                    }
                    break;
                case 2:
                    orderStatus.set("待收货");
                    if (!isMerchantMode)
                        btnR.set("确认收货");
                    break;
                case 3:
                    orderStatus.set("待评价");
                    if (!isMerchantMode) {
                        if (order.getPreStatus() == 5 || order.getPreStatus() == 6 || order.getPreStatus() == 7) {
                            btnL.set("");
                        } else {
                            btnL.set("售后退款");
                        }
                        btnR.set("评价订单");
                    }
                    break;
                case 4:
                    orderStatus.set("已完成");
                    break;
                case 5:
                    orderStatus.set("退款申请");
                    if (isMerchantMode) {
//                        btnL.set("取消");
                        btnR.set("退款处理");
                    }
                    break;
                case 6:
                    orderStatus.set("退款中");
                    break;
                case 7:
                    orderStatus.set("已退款");
                    break;
                case 8:
                    if (isMerchantMode) {
                        orderStatus.set("可提现");
                    } else {
                        orderStatus.set("已完成");
                    }
                    break;
                case 9:
                    if (isMerchantMode) {
                        orderStatus.set("提现中");
                    } else {
                        orderStatus.set("已完成");
                    }
                    break;
                case 10:
                    if (isMerchantMode) {
                        orderStatus.set("已提现");
                    } else {
                        orderStatus.set("已完成");
                    }
                    break;
            }

            if (order.getOrderStatus() > 1) {
                viewStyle.showLogisticsInfo.set(true);
                viewStyle.isSelf.set(order.getPickSelf() == 1);
                exName.set(order.getExName());
                exNumber.set(order.getExNumber());
            }

            receiver.set(order.getConsigneeName());
            receivePhone.set(order.getConsigneeMobile());
            if (!TextUtils.isEmpty(order.getConsigneeProvince()) && !TextUtils.isEmpty(order.getConsigneeCity())
                    && !TextUtils.isEmpty(order.getConsigneeArea())) {
                List<City> cityList = AddressUtils.getCities(new String[]{order.getConsigneeProvince(), order.getConsigneeCity(), order.getConsigneeArea()});
                receiveAddr.set(AddressUtils.getCityName(cityList) + order.getConsigneeAddress());
            } else {
                receiveAddr.set(order.getConsigneeAddress());
            }

            if (isMerchantMode) {
                //  商家看买家信息
                if (order.getUser() != null) {
                    merchantHeadUri.set(order.getUser().getAvatar());
                    name.set(order.getUser().getUserNick());
                    summary.set(order.getUser().getProfile());
                }
            } else {
                //  买家看商家信息
                if (order.getSaleUser() != null) {
                    merchantHeadUri.set(order.getSaleUser().getAvatar());
                    name.set(order.getSaleUser().getUserNick());
                    summary.set(order.getSaleUser().getProfile());
                }
            }

            BigDecimal total = new BigDecimal(0);
            BigDecimal freight = new BigDecimal(0);
            int goodsNum = 0;
            if (order.getOrderDetails() != null) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    itemGoodsVMs.add(new ItemOrderGoodsListVM(detail, order, isMerchantMode, ItemOrderGoodsListVM.FROM_ORDER_DETAIL, fragment));
                    freight = freight.add(detail.getGoods().getFreight());
                    if (detail.getGoodsSpec() != null) {
                        total = total.add(detail.getGoodsSpec().getPrice().multiply(new BigDecimal(detail.getGoodsNum())));
                    }
                    goodsNum += detail.getGoodsNum();
                }
            }
            transFee.set(freight);
            orderInfo.set("共" + goodsNum + "件，合计¥" + total.add(freight) + "（含运费¥" + freight + "）");
            moneyInfo.set("应收：¥" + total.add(freight));

            if ((order.getApplyReason() == null || TextUtils.isEmpty(order.getApplyReason())) &&
                    (order.getAuditInfo() == null || TextUtils.isEmpty(order.getAuditInfo())) &&
                    (order.getRefundAmount() == null || TextUtils.isEmpty(String.valueOf(order.getRefundAmount())))) {
                viewStyle.showAfterSale.set(false);
            } else {
                if (order.getApplyReason() != null && order.getApplyReason().startsWith("{") && order.getApplyReason().endsWith("}")) {
                    ImgTextContent content = new Gson().fromJson(order.getApplyReason(), new TypeToken<ImgTextContent>() {
                    }.getType());
                    applyReason.set("退款原因：" + content.getContent());
                    if (content.getImg() != null && !content.getImg().isEmpty()) {
                        afterSaleImgUrl.set(content.getImg().get(0));
                    }
                } else {
                    applyReason.set("退款原因：" + (order.getApplyReason() == null ? "没有填写退款原因" : order.getApplyReason()));
                }
                auditInfo.set("商家审核：" + (order.getAuditInfo() == null ? "没有填写审批内容" : order.getAuditInfo()));
                afterSaleMoney.set(order.getRefundAmount());
                viewStyle.showAfterSale.set(true);
            }
        }
    }

    public final ReplyCommand picClick = new ReplyCommand(() -> {

    });

    /**
     * 跳转地址管理
     */
    public final ReplyCommand toAddrMgrClick = new ReplyCommand(() -> {
//        fragment.getActivity().startActivity(AddressMgrActivity.newIntent(fragment.getActivity()));
//        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 跳转个人主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), isMerchantMode ? order.getUser() : order.getSaleUser()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 联系商家
     */
    public final ReplyCommand contactClick = new ReplyCommand(() -> {
        IMUtils.checkIMLogin(isSuccess -> {
            if (isSuccess) {
                String userName = isMerchantMode ? order.getUser().getId() : order.getSaleUser().getId();
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

    /**
     * 左边按钮
     */
    public final ReplyCommand lClick = new ReplyCommand(() -> {
        switch (order.getOrderStatus()) {
            case 0:
                //  both：取消订单
                fragment.showConfirmDialog("确认取消订单？", () -> {
                    changeOrderStatus(-1);
                });
                break;
            case 1:
                //  买家：申请退款；商家：直接退款
                if (isMerchantMode)
                    fragment.showConfirmDialog("确认退款至买家？", () -> {
                        refund("商家退款", order.getTotalPrice());
                    });
                else
//                    fragment.showRefundDialog(order, false, false, (reason, money) -> {
//                        applyRefund(reason);
//                    });
                    fragment.showConfirmDialog("确认取消订单并退款？", () -> {
                        refund("未发货退款", order.getTotalPrice());
                    });
                break;
            case 3:
                //  买家：售后退款
                fragment.showRefundDialog(order, isMerchantMode, false, order.getApplyReason(), (reason, money) -> {
                    applyRefund(reason);
                });
                break;
            case 5:
                //  商家：驳回退款
                fragment.showRefundDialog(order, isMerchantMode, false, order.getApplyReason(), (reason, money) -> {
                    disagreeRefund(reason);
                });
                break;
        }
    });

    /**
     * 右边按钮
     */
    public final ReplyCommand rClick = new ReplyCommand(() -> {
        switch (order.getOrderStatus()) {
            case 0:
                //  买家：付款
                fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity(), null, order, true));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                break;
            case 1:
                //  商家：确认发货
                send();
                break;
            case 2:
                //  买家：确认收货
                fragment.showConfirmDialog("确认收货后将不能取消，请确定是否确认收货？", () -> {
                    changeOrderStatus(3);
                });
                break;
            case 3:
                //  买家：评价订单
                ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                        OrderEvaluateFragment.newInstance(order), OrderEvaluateFragment.TAG);
                break;
            case 5:
                //  商家：同意退款
                fragment.showRefundDialog(order, isMerchantMode, true, order.getApplyReason(), this::refund);
                break;
        }
    });

    /**
     * 申请退款
     */
    private void applyRefund(String reason) {
        request.setApplyReason(reason);
        changeOrderStatus(5);
    }

    /**
     * 驳回退款
     */
    private void disagreeRefund(String reason) {
        request.setAuditInfo(reason);
        changeOrderStatus(order.getPreStatus());
    }

    /**
     * 退款
     */
    private void refund(String reason, BigDecimal money) {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setOrderCode(order.getOrderCode());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(req));

        RetrofitHelper.getOrderAPI()
                .refund(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    fragment.getFragmentManager().popBackStack();
                    //  通知刷新订单列表
                    Intent intent = new Intent("updateOrderList");
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * 商家发货
     */
    private void send() {
        fragment.showExDialog((type, exName, exNumber, remark) -> {
            request.setPickSelf(type);
            request.setExName(exName);
            request.setExNumber(exNumber);
            request.setRemark(remark);

            changeOrderStatus(2);
        });
    }

    /**
     * 改变订单状态
     */
    private void changeOrderStatus(int newStatus) {
        request.setOrderStatus(newStatus);

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(request));

        RetrofitHelper.getOrderAPI()
                .updateOrder(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    fragment.getFragmentManager().popBackStack();
                    //  通知刷新订单列表
                    Intent intent = new Intent("updateOrderList");
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
