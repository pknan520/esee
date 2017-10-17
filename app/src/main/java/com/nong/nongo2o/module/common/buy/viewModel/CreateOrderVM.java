package com.nong.nongo2o.module.common.buy.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.bean.SimpleUser;
import com.nong.nongo2o.entity.domain.Address;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.domain.Order;
import com.nong.nongo2o.entity.domain.OrderDetail;
import com.nong.nongo2o.entity.request.CreateOrderRequest;
import com.nong.nongo2o.module.common.buy.fragment.CreateOrderFragment;
import com.nong.nongo2o.module.common.buy.fragment.PayFragment;
import com.nong.nongo2o.module.common.buy.fragment.PayResultFragment;
import com.nong.nongo2o.module.common.viewModel.ItemOrderGoodsListVM;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AddressUtils;
import com.nong.nongo2o.uils.imUtils.IMUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-7-17.
 */

public class CreateOrderVM implements ViewModel {

    private CreateOrderFragment fragment;
    private ArrayList<OrderDetail> orderDetails;
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
    //  订单总计
    public final ObservableField<String> orderInfo = new ObservableField<>();
    public final ObservableField<String> moneyInfo = new ObservableField<>();

    private Address orderAddress;
    private BigDecimal transFeeBD, total;
    private int goodsNum;

    public CreateOrderVM(CreateOrderFragment fragment, ArrayList<OrderDetail> orderDetails) {
        this.fragment = fragment;
        this.orderDetails = orderDetails;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean hasDefaultAddr = new ObservableBoolean(false);
        public final ObservableBoolean isComplete = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (orderDetails != null && orderDetails.size() > 0) {
            getDefAddr();

            SimpleUser saler = orderDetails.get(0).getSale();
            merchantHeadUri.set(saler.getAvatar());
            name.set(saler.getUserNick());
            summary.set(saler.getProfile());

            for (OrderDetail detail : orderDetails) {
                itemGoodsVMs.add(new ItemOrderGoodsListVM(detail, null, false, ItemOrderGoodsListVM.FROM_ORDER_CREATE, fragment));
            }

            transFeeBD = calculateTransFee();
            total = calculateTotalPrice();
            goodsNum = calculateGoodsNum();
            transFee.set(transFeeBD);
            orderInfo.set("共" + goodsNum + "件，合计¥" + total.add(transFeeBD).toString() + "（含运费¥" + transFeeBD.toString() + "）");
            moneyInfo.set("应收：¥" + total.add(transFeeBD).toString());
        }
    }

    /**
     * 获取默认地址
     */
    private void getDefAddr() {
        RetrofitHelper.getAddressAPI()
                .getDefAddr()
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address -> {
                    viewStyle.hasDefaultAddr.set(true);
                    setAddrInfo(address);
                }, throwable -> {
                    viewStyle.hasDefaultAddr.set(false);
                });
    }

    /**
     * 计算运费
     */
    private BigDecimal calculateTransFee() {
        BigDecimal total = new BigDecimal(0);
        for (OrderDetail detail : orderDetails) {
            total = total.add(detail.getGoods().getFreight());
        }
        return total;
    }

    /**
     * 计算总价
     */
    private BigDecimal calculateTotalPrice() {
        BigDecimal total = new BigDecimal(0);
        for (OrderDetail detail : orderDetails) {
            total = total.add(detail.getGoodsSpec().getPrice().multiply(new BigDecimal(detail.getGoodsNum())));
        }
        return total;
    }

    /**
     * 计算货物数量
     */
    private int calculateGoodsNum() {
        int goodsNum = 0;
        for (OrderDetail detail : orderDetails) {
            goodsNum += detail.getGoodsNum();
        }
        return goodsNum;
    }

    /**
     * 选择地址
     */
    public final ReplyCommand selectAddrClick = new ReplyCommand(() -> {
        fragment.startActivityForResult(AddressMgrActivity.newIntent(fragment.getActivity(), AddressMgrActivity.ADDR_SEL), AddressMgrActivity.ADDR_SEL);
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 设置地址信息
     */
    public void setAddrInfo(Address address) {
        orderAddress = address;
        receiver.set(orderAddress.getConsigneeName());
        receivePhone.set(orderAddress.getConsigneeMobile());
        if (!TextUtils.isEmpty(address.getConsigneeProvince()) && !TextUtils.isEmpty(address.getConsigneeCity())
                && ! TextUtils.isEmpty(address.getConsigneeArea()) && !TextUtils.isEmpty(address.getConsigneeTown())) {
            List<City> cityList = AddressUtils.getCities(new String[]{address.getConsigneeProvince(), address.getConsigneeCity(), address.getConsigneeArea(), address.getConsigneeTown()});
            receiveAddr.set(AddressUtils.getCityName(cityList) + address.getConsigneeAddress());
        } else {
            receiveAddr.set(address.getConsigneeAddress());
        }

        viewStyle.isComplete.set(true);
    }

    /**
     * 查看商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), orderDetails.get(0).getSale()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 联系商家
     */
    public final ReplyCommand contactClick = new ReplyCommand(() -> {
                IMUtils.checkIMLogin(isSuccess -> {
                    if (isSuccess) {
                        String userName = orderDetails.get(0).getSale().getId();
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
     * 下单按钮
     */
    public final ReplyCommand orderClick = new ReplyCommand(this::createOrder);

    private void createOrder() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setTotalPrice(total);
        request.setConsigneeProvince(orderAddress.getConsigneeProvince());
        request.setConsigneeCity(orderAddress.getConsigneeCity());
        request.setConsigneeArea(orderAddress.getConsigneeArea());
        request.setConsigneeAddress(orderAddress.getConsigneeAddress());
        request.setConsigneeName(orderAddress.getConsigneeName());
        request.setConsigneeMobile(orderAddress.getConsigneeMobile());
        List<OrderDetail> details = new ArrayList<>();
        for (OrderDetail detail : orderDetails) {
            OrderDetail newDetail = new OrderDetail();
            newDetail.setUserCode(detail.getUserCode());
            newDetail.setSaleUserCode(detail.getSaleUserCode());
            newDetail.setGoodsCode(detail.getGoodsCode());
            newDetail.setSpecCode(detail.getSpecCode());
            newDetail.setGoodsNum(detail.getGoodsNum());
            details.add(newDetail);
        }
        request.setOrderDetails(details);

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(request));

        RetrofitHelper.getOrderAPI()
                .userOrder(requestBody)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(order -> {
                    ((RxBaseActivity) fragment.getActivity()).replaceFragment(R.id.fl, PayFragment.newInstance(order), PayFragment.TAG);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }


}
