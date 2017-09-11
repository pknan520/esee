package com.nong.nongo2o.module.main.fragment.merchant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.nong.nongo2o.databinding.FragmentMerchantListBinding;
import com.nong.nongo2o.module.main.viewModel.merchant.MerchantListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;
import com.trello.rxlifecycle2.components.RxFragment;

/**
 * Created by Administrator on 2017-6-23.
 */

public class MerchantListFragment extends RxFragment {

    private FragmentMerchantListBinding binding;
    private MerchantListVM vm;

    public static MerchantListFragment newInstance() {
        return new MerchantListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new MerchantListVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMerchantListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        binding.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        binding.slider.setPresetTransformer(SliderLayout.Transformer.Default);

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.setNestedScrollingEnabled(false);
    }
}
