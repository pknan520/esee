package com.nong.nongo2o.module.common.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017-7-21.
 */

public class PopupStandardVM implements ViewModel {

    private RxBaseActivity activity;
    private PopupWindow popup;

    //  假数据图片uri
    private static String[] uriArray = {"https://ws1.sinaimg.cn/large/610dc034ly1fhb0t7ob2mj20u011itd9.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg",
            "https://ws1.sinaimg.cn/large/610dc034ly1fhovjwwphfj20u00u04qp.jpg", "https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"};
    //  商品信息
    @DrawableRes
    public final int imgPlaceHolder = R.mipmap.picture_default;
    public final ObservableField<String> imgUri = new ObservableField<>();
    public final ObservableField<Double> price = new ObservableField<>();
    public final ObservableField<Integer> stockNum = new ObservableField<>();
    public final ObservableField<String> standard = new ObservableField<>();
    //  商品规格
    public final ObservableField<TagAdapter> standardAdapter = new ObservableField<>();
    public final ObservableList<String> standardList = new ObservableArrayList<>();

    public PopupStandardVM(RxBaseActivity activity, PopupWindow popup) {
        this.activity = activity;
        this.popup = popup;

        initAdapter();
        initFakeData();
    }

    private void initAdapter() {
        standardAdapter.set(new TagAdapter<String>(standardList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.item_standard, parent, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    /**
     * 假数据
     */
    private void initFakeData() {
        imgUri.set(uriArray[(int) (Math.random() * 4 - 0.01)]);
        price.set(48.80);
        stockNum.set(621);

        //  规格列表
        String[] standardArray = {"规格", "规格+1", "规格不同长度", "就想看看长点会怎样", "规格+2"};
        for (int i = 0; i < 20; i++) {
            standardList.add(standardArray[(int) (Math.random() * 5 - 0.01)]);
        }
    }

    /**
     * 取消按钮
     */
    public final ReplyCommand cancelClick = new ReplyCommand(() -> {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    });

    /**
     * 确定按钮
     */
    public final ReplyCommand confirmClick = new ReplyCommand(() -> {

    });
}
