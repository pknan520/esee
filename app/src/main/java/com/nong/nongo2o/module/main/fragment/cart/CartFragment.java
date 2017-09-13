package com.nong.nongo2o.module.main.fragment.cart;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.databinding.FragmentCartBinding;
import com.nong.nongo2o.databinding.PopupStandardBinding;
import com.nong.nongo2o.module.common.viewModel.PopupStandardVM;
import com.nong.nongo2o.module.main.viewModel.cart.CartVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-22.
 */

public class CartFragment extends RxFragment {

    private FragmentCartBinding binding;
    private CartVM vm;
    private PopupWindow popupStandard;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new CartVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
//        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.inflateMenu(R.menu.menu_cart);
        binding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.edit_cart:
                    editClick(item);
                    break;
            }
            return true;
        });
        setHasOptionsMenu(true);

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider_15px));

        PopupStandardBinding popupBinding = PopupStandardBinding.inflate(getActivity().getLayoutInflater(), null, false);
        popupStandard = new PopupWindow(popupBinding.getRoot(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupStandard.setFocusable(true);
        popupStandard.setAnimationStyle(R.style.PopupAnimBottom);
        popupStandard.setBackgroundDrawable(new ColorDrawable(0x80000000));
        //  popupStandard添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        popupBinding.getRoot().setOnTouchListener((v, event) -> {
            int height = popupBinding.llContainer.getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    popupStandard.dismiss();
                }
            }
            return true;
        });
        popupBinding.setViewModel(new PopupStandardVM((RxBaseActivity) getActivity(), popupStandard));
        popupStandard.update();
    }

    public void showPopupStandard() {
        if (popupStandard != null && !popupStandard.isShowing()) {
            popupStandard.showAtLocation(binding.llContainer, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.menu_cart, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.edit_cart:
//                editClick(item);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void editClick(MenuItem item) {
        vm.viewStyle.isEdit.set(!vm.viewStyle.isEdit.get());

        for (CartVM.ItemCartMerchantVM itemMerchant : vm.itemCartMerchantVMs) {
            for (CartVM.ItemCartMerchantVM.ItemCartMerchantGoodsVM itemGoods : itemMerchant.itemCartMerchantGoodsVMs) {
                itemGoods.viewStyle.isEdit.set(vm.viewStyle.isEdit.get());
            }
        }

        if (vm.viewStyle.isEdit.get()) {
            item.setTitle("完成");
        } else {
            item.setTitle("编辑");
        }
    }
}
