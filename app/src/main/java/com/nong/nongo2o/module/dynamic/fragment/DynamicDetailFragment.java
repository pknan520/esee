package com.nong.nongo2o.module.dynamic.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;

import com.daimajia.slider.library.SliderLayout;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentDynamicDetailBinding;
import com.nong.nongo2o.databinding.PopupCommentEditBinding;
import com.nong.nongo2o.entities.response.DynamicDetail;
import com.nong.nongo2o.entity.domain.Moment;
import com.nong.nongo2o.module.dynamic.viewModel.DynamicDetailVM;
import com.nong.nongo2o.uils.CommonUtils;

/**
 * Created by Administrator on 2017-6-30.
 */

public class DynamicDetailFragment extends RxBaseFragment {

    public static final String TAG = "DynamicDetailFragment";

    private FragmentDynamicDetailBinding binding;
    private DynamicDetailVM vm;
    private Moment dynamic;
    private PopupWindow commentPopup, commentEditPopup;
    private PopupCommentEditBinding popupCommentEditBinding;

    public static DynamicDetailFragment newInstance(Moment dynamic) {
        Bundle args = new Bundle();
        args.putParcelable("dynamic", dynamic);
        DynamicDetailFragment fragment = new DynamicDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamic = getArguments().getParcelable("dynamic");
        if (vm == null) {
            vm = new DynamicDetailVM(this, dynamic);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDynamicDetailBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        binding.collapsingToolbar.setTitle(dynamic.getTitle());
        binding.collapsingToolbar.setExpandedTitleColor(Color.WHITE);
        binding.collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        binding.slider.setCustomIndicator(binding.customIndicator);

        binding.sv.setOnTouchListener((v, event) -> {
            CommonUtils.hideSoftInput(getActivity(), binding.et);
            binding.et.clearFocus();
            vm.hintComment.set("评论");
            return false;
        });

//        binding.sv.getViewTreeObserver().addOnScrollChangedListener(() -> binding.refreshLayout.setEnabled(binding.sv.getScaleY() == 0));
        binding.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (!binding.refreshLayout.isRefreshing()) {
                binding.refreshLayout.setEnabled(binding.sv.getScaleY() == verticalOffset);
            }
        });


        TextPaint tp = binding.tvTitle.getPaint();
        tp.setFakeBoldText(true);

        binding.rvDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvDetail.setNestedScrollingEnabled(false);
        binding.rvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvComment.setNestedScrollingEnabled(false);

        binding.et.clearFocus();
//        initPopup();
    }

    public void editCommentRequestFocus() {
        binding.et.requestFocus();
        CommonUtils.showSoftInput(getActivity(), binding.et);
    }

//    private void initPopup() {
//        PopupCommentBinding popupCommentBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.popup_comment, null, false);
//        commentPopup = new PopupWindow(popupCommentBinding.getRoot(), WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        commentPopup.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        commentPopup.setAnimationStyle(R.style.popwin_anim_style);
//        commentPopup.setTouchable(true);
//        commentPopup.setOutsideTouchable(true);
//        commentPopup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        popupCommentBinding.setViewModel(vm.new CommentPopupVM());
//
//        popupCommentEditBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.popup_comment_edit, null, false);
//        commentEditPopup = new PopupWindow(popupCommentEditBinding.getRoot(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        commentEditPopup.setFocusable(true);
//        commentEditPopup.setTouchable(true);
//        commentEditPopup.setOutsideTouchable(true);
//        commentEditPopup.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
//        commentEditPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
//        commentEditPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        popupCommentEditBinding.setViewModel(vm.new CommentEditPopupVM());
//    }

//    public void showCommentPopup() {
//        if (!commentPopup.isShowing()) {
//            int[] location = new int[2];
//            binding.ivCommentPopup.getLocationOnScreen(location);
//            commentPopup.showAtLocation(binding.ivCommentPopup, Gravity.NO_GRAVITY, location[0] - commentPopup.getContentView().getMeasuredWidth(), location[1]);
//        }
//    }
//
//    public void dismissCommentPopup() {
//        if (commentPopup.isShowing()) commentPopup.dismiss();
//    }
//
//    public void showCommentEditPopup(int position) {
//        if (!commentEditPopup.isShowing()) {
//            commentEditPopup.showAtLocation(binding.sv, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            popupCommentEditBinding.et.requestFocus();
//            CommonUtils.showSoftInput(getActivity(), popupCommentEditBinding.et);
//            if (position >= 0) {
//                scrollUnderItem(binding.rvComment.getChildAt(position));
//            } else {
//                scrollUnderItem(binding.ivCommentPopup);
//            }
//        }
//    }
//
//    private void scrollUnderItem(View view) {
//        Observable.timer(300, TimeUnit.MILLISECONDS)
//                .subscribe(aLong -> {
//                    int[] position1 = new int[2];
//                    view.getLocationOnScreen(position1);
//                    int[] position2 = new int[2];
//                    popupCommentEditBinding.et.getLocationOnScreen(position2);
//                    int dx = Math.abs(position1[1] - position2[1]);
//                    binding.sv.smoothScrollBy(0, Math.abs(position1[1] - position2[1]));
//                });
//    }

}

