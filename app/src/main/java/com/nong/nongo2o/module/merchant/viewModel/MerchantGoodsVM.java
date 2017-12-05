package com.nong.nongo2o.module.merchant.viewModel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.UserInfo;
import com.nong.nongo2o.entity.domain.ImgTextContent;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsSpec;
import com.nong.nongo2o.entity.request.CreateCartRequest;
import com.nong.nongo2o.module.common.viewModel.ItemImageTextVM;
import com.nong.nongo2o.module.merchant.fragment.MerchantGoodsFragment;
import com.nong.nongo2o.module.message.activity.ChatActivity;
import com.nong.nongo2o.module.personal.activity.PersonalHomeActivity;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.FocusUtils;
import com.nong.nongo2o.uils.imUtils.IMCallback;
import com.nong.nongo2o.uils.imUtils.IMUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-7-4.
 */

public class MerchantGoodsVM implements ViewModel {

    private MerchantGoodsFragment fragment;
    private Goods good;

    //  商家信息
    @DrawableRes
    public final int headPlaceHolder = R.mipmap.head_default_60;
    public final ObservableField<String> headUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> summary = new ObservableField<>();
    @DrawableRes
    public final int isFocus = R.mipmap.icon_like;
    @DrawableRes
    public final int unFocus = R.mipmap.icon_focus_white;
    //  商品轮播
    public final ObservableList<DefaultSliderView> sliderList = new ObservableArrayList<>();
    //  商品信息
    public final ObservableField<String> goodsName = new ObservableField<>();
    public final ObservableField<BigDecimal> goodsPrice = new ObservableField<>();
    public final ObservableField<BigDecimal> minPrice = new ObservableField<>();    //  最低价
    public final ObservableField<BigDecimal> maxPrice = new ObservableField<>();    //  最高价
    public final ObservableField<Integer> stockNum = new ObservableField<>();
    public final ObservableField<Integer> saleNum = new ObservableField<>();
    //  商品规格
    public final ObservableField<TagAdapter> standardAdapter = new ObservableField<>();
    private final ObservableList<GoodsSpec> standardList = new ObservableArrayList<>();
    //  图文详情
    public final ObservableList<ItemImageTextVM> itemImageTextVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemImageTextVM> itemImageTextBinding = ItemBinding.of(BR.viewModel, R.layout.item_image_text);
    //  运费
    public final ObservableField<BigDecimal> transFee = new ObservableField<>();
    //  总库存
    private int totalStock = 0;
    //  当前规格位置，没有选择时为-1
    private int currentSpecPos = -1;

    public MerchantGoodsVM(MerchantGoodsFragment fragment, Goods good) {
        this.fragment = fragment;
        this.good = good;

        initAdapter();
        if (good != null) {
            initData();
        }
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isSelf = new ObservableBoolean(false);
        public final ObservableBoolean isFocus = new ObservableBoolean(false);
        public final ObservableBoolean selectedSpec = new ObservableBoolean(false);
    }

    private void initAdapter() {
        standardAdapter.set(new TagAdapter<GoodsSpec>(standardList) {
            @Override
            public View getView(FlowLayout parent, int position, GoodsSpec goodsSpec) {
                TextView tv = (TextView) LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_standard, parent, false);
                tv.setText(goodsSpec.getTitle());
                return tv;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //  商家信息
        headUri.set(good.getSale().getAvatar());
        name.set(good.getSale().getUserNick());
        summary.set(good.getSale().getProfile());
        viewStyle.isSelf.set(good.getSale().getUserCode().equals(UserInfo.getInstance().getUserCode()));
        viewStyle.isFocus.set(FocusUtils.checkIsFocus(good.getSale().getUserCode()));
        //  轮播图
        addSliderView();
        //  商品信息
        goodsName.set(good.getTitle());
        saleNum.set(good.getTotalSale());
        //  设置规格信息
        setGoodSpecInfo();
        //  运费
        transFee.set(good.getFreight());
        //  图文详情
        if (!TextUtils.isEmpty(good.getDetail())) {
            List<ImgTextContent> contentList = new Gson().fromJson(good.getDetail(), new TypeToken<List<ImgTextContent>>() {
            }.getType());
            if (contentList != null && !contentList.isEmpty()) {
                for (ImgTextContent content : contentList) {
                    itemImageTextVMs.add(new ItemImageTextVM(content));
                }
            }
        }
    }

    /**
     * 添加轮播图
     */
    private void addSliderView() {
        if (!TextUtils.isEmpty(good.getCovers())) {
            List<String> covers = new Gson().fromJson(good.getCovers(), new TypeToken<List<String>>() {
            }.getType());
            for (String uri : covers) {
                DefaultSliderView view = new DefaultSliderView(fragment.getActivity());
                view.image(uri)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                sliderList.add(view);
            }
        }
    }

    /**
     * 设置商品规格信息
     */
    private void setGoodSpecInfo() {
        if (good.getGoodsSpecs() != null && good.getGoodsSpecs().size() > 0) {
            minPrice.set(good.getGoodsSpecs().get(0).getPrice());
            maxPrice.set(good.getGoodsSpecs().get(0).getPrice());
            //  规格列表
            for (GoodsSpec spec : good.getGoodsSpecs()) {
                standardList.add(spec);
                totalStock += spec.getQuantity();
                if (spec.getPrice().compareTo(minPrice.get()) == -1 || spec.getPrice().compareTo(minPrice.get()) == 0) {
                    minPrice.set(spec.getPrice());
                } else if (spec.getPrice().compareTo(maxPrice.get()) == 1) {
                    maxPrice.set(spec.getPrice());
                }
            }
            stockNum.set(totalStock);
        }
    }

    /**
     * 规格点击事件
     */
    public final ReplyCommand<Integer> tagClickCommand = new ReplyCommand<>(position -> {
        if (currentSpecPos != -1 && currentSpecPos == position) {
            //  当前已选择，取消选择
            currentSpecPos = -1;
            viewStyle.selectedSpec.set(false);
            goodsPrice.set(good.getPrice());
            stockNum.set(totalStock);
        } else {
            currentSpecPos = position;
            viewStyle.selectedSpec.set(true);
            goodsPrice.set(good.getGoodsSpecs().get(position).getPrice());
            stockNum.set(good.getGoodsSpecs().get(position).getQuantity());
        }
    });

    /**
     * 进入商家主页
     */
    public final ReplyCommand personalHomeClick = new ReplyCommand(() -> {
        fragment.getActivity().startActivity(PersonalHomeActivity.newIntent(fragment.getActivity(), good.getSale()));
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 咨询商家
     */
    public final ReplyCommand consultClick = new ReplyCommand(() -> {
        IMUtils.checkIMLogin(isSuccess -> {
            if (isSuccess) {
                String userName = good.getSale().getId();
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
     * 关注商家
     */
    public final ReplyCommand focusClick = new ReplyCommand(() -> {
        FocusUtils.changeFocus(fragment.getActivity(), viewStyle.isFocus.get(), good.getSale().getUserCode(), viewStyle.isFocus::set);
    });

    /**
     * 加入购物车
     */
    public final ReplyCommand addCartClick = new ReplyCommand(() -> {
        if (currentSpecPos == -1) {
            Toast.makeText(fragment.getActivity(), "请选择规格", Toast.LENGTH_SHORT).show();
            return;
        }
        postCart(createCartBody());
    });

    private RequestBody createCartBody() {
        CreateCartRequest request = new CreateCartRequest();
        request.setSaleUserCode(good.getSale().getUserCode());
        request.setGoodsCode(good.getGoodsCode());
        request.setGoodsNum(1);
        request.setSpecCode(good.getGoodsSpecs().get(currentSpecPos).getSpecCode());

        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                new Gson().toJson(request));

        return requestBody;
    }

    private void postCart(RequestBody body) {
        RetrofitHelper.getCartAPI()
                .postCart(body)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Toast.makeText(fragment.getActivity(), "添加购物车成功", Toast.LENGTH_SHORT).show();
                    //  通知购物车刷新
                    Intent intent = new Intent("updateCart");
                    LocalBroadcastManager.getInstance(fragment.getActivity()).sendBroadcast(intent);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

//    /**
//     * 立即购买
//     */
//    public final ReplyCommand buyClick = new ReplyCommand(() -> {
//        fragment.getActivity().startActivity(BuyActivity.newIntent(fragment.getActivity()));
//        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
//    });

}
