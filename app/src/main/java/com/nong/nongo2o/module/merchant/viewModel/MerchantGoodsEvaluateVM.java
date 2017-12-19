package com.nong.nongo2o.module.merchant.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.entity.bean.ApiListResponse;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.entity.domain.GoodsComment;
import com.nong.nongo2o.module.common.viewModel.ItemEvaluateVM;
import com.nong.nongo2o.module.merchant.fragment.MerchantGoodsEvaluateFragment;
import com.nong.nongo2o.module.merchant.fragment.MerchantGoodsFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-7-12.
 */

public class MerchantGoodsEvaluateVM implements ViewModel {

    private MerchantGoodsEvaluateFragment fragment;
    private Goods good;
    private int status;

    //  评价列表
    public final ObservableList<ItemEvaluateVM> itemEvaluateVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemEvaluateVM> itemEvaluateBinding = ItemBinding.of(BR.viewModel, R.layout.item_evaluate_list);

    private int total;
    private int pageSize = 20;
    private String starStr;

    public MerchantGoodsEvaluateVM(MerchantGoodsEvaluateFragment fragment, Goods good, int status) {
        this.fragment = fragment;
        this.good = good;
        this.status = status;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        switch (status) {
            case MerchantGoodsFragment.EVA_ALL:
                starStr = "1,2,3,4,5";
                break;
            case MerchantGoodsFragment.EVA_BAD:
                starStr = "1,2";
                break;
            case MerchantGoodsFragment.EVA_NORMAL:
                starStr = "3,4";
                break;
            case MerchantGoodsFragment.EVA_GOOD:
                starStr = "5";
                break;
            default:
                starStr = "1,2,3,4,5";
                break;
        }

        getEvaluation(1);
    }

    private void getEvaluation(int page) {
        RetrofitHelper.getGoodsAPI()
                .getGoodsComment(good.getGoodsCode(), starStr, page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    total = resp.getTotal();
                    for (GoodsComment comment : resp.getRows()) {
                        itemEvaluateVMs.add(new ItemEvaluateVM(comment, itemEvaluateVMs.isEmpty()));
                    }
                    viewStyle.isEmpty.set(itemEvaluateVMs.size() == 0);
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * 上拉加载
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<Integer>(integer -> {
        loadMoreData();
    });

    private void loadMoreData() {
        if (itemEvaluateVMs.size() < total) {
            getEvaluation(itemEvaluateVMs.size() / pageSize + 1);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
        }
    }
}
