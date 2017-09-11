package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableField;
import android.support.annotation.DrawableRes;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.module.merchant.activity.MerchantGoodsActivity;
import com.nong.nongo2o.module.personal.fragment.OrderDetailFragment;

/**
 * Created by Administrator on 2017-7-14.
 */

public class ItemOrderGoodsListVM implements ViewModel {

    public static final int FROM_ORDER_LIST = 0, FROM_ORDER_DETAIL = 1;

    private int fromTag;
    private RxBaseFragment fragment;

    //  假数据图片uri
    private String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};

    @DrawableRes
    public final int imgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> imgUri = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> standard = new ObservableField<>();
    public final ObservableField<Double> price = new ObservableField<>();
    public final ObservableField<Integer> num = new ObservableField<>();

    public ItemOrderGoodsListVM(int fromTag, RxBaseFragment fragment) {
        this.fromTag = fromTag;
        this.fragment = fragment;

        initFakeData();
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        imgUri.set(uriArray[(int) (Math.random() * 4)]);
        name.set("墨西哥进口牛油果");
        standard.set("精装4只/每盒");
        price.set(48.80);
        num.set(4);
    }

    /**
     * 点击事件（跳转至订单详情或商品详情)
     */
    public final ReplyCommand itemClickCommand = new ReplyCommand(this::itemClick);

    private void itemClick() {
        switch (fromTag) {
            case FROM_ORDER_LIST:
                ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment.getParentFragment(),
                        OrderDetailFragment.newInstance(), OrderDetailFragment.TAG);
                break;
            case FROM_ORDER_DETAIL:
                fragment.getActivity().startActivity(MerchantGoodsActivity.newIntent(fragment.getActivity()));
                fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
                break;
        }
    }
}
