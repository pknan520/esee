package com.nong.nongo2o.module.merchant.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.databinding.FragmentMerchantGoodsBinding;
import com.nong.nongo2o.entity.domain.Goods;
import com.nong.nongo2o.module.common.adapter.MyFragmentPagerAdapter;
import com.nong.nongo2o.module.merchant.viewModel.MerchantGoodsVM;
import com.nong.nongo2o.widget.ScrollView.ObservableScrollView;
import com.nong.nongo2o.widget.ScrollView.ScrollViewListener;
import com.trello.rxlifecycle2.components.RxFragment;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017-7-4.
 */

public class MerchantGoodsFragment extends RxFragment {

    public static final String TAG = "MerchantGoodsFragment";

    public static final int EVA_ALL = 0, EVA_GOOD = 1, EVA_NORMAL = 2, EVA_BAD = 3;
    private static final String[] tabArray = {"商品", "详情", "评价"};
    private static final String[] evaArray = {"全部", "好评", "中评", "差评"};

    private FragmentMerchantGoodsBinding binding;
    private MerchantGoodsVM vm;

    public static MerchantGoodsFragment newInstance(Goods good) {
        Bundle args = new Bundle();
        args.putSerializable("good", good);
        MerchantGoodsFragment fragment = new MerchantGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new MerchantGoodsVM(this, (Goods) getArguments().getSerializable("good"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMerchantGoodsBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        getActivity().setTitle("");
        binding.tvName.getPaint().setFakeBoldText(true);

        for (String aTabArray : tabArray) {
            TabLayout.Tab tab = binding.tab.newTab().setCustomView(R.layout.item_goods_tap);
            if (tab.getCustomView() != null) {
                TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                tv.setText(aTabArray);
                binding.tab.addTab(tab);
            }
        }

        binding.tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        binding.sv.smoothScrollTo(0, (int) binding.slider.getY());
                        break;
                    case 1:
                        binding.sv.smoothScrollTo(0, (int) binding.rvDetail.getY());
                        break;
                    case 2:
                        binding.sv.smoothScrollTo(0, (int) binding.tabEvaluate.getY());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.sv.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView view, int x, int y, int oldX, int oldY) {
                if (y < binding.rvDetail.getY()) {
                    binding.tab.setScrollPosition(0, 0, true);
                } else if (y >= binding.rvDetail.getY() && y < binding.tabEvaluate.getY()) {
                    binding.tab.setScrollPosition(1, 0, true);
                } else {
                    binding.tab.setScrollPosition(2, 0, true);
                }
            }

            @Override
            public void onScrollBottom() {
//                Toast.makeText(getActivity(), "滑动到底部了", Toast.LENGTH_SHORT).show();
            }
        });

        binding.rvDetail.setLayoutManager(new LinearLayoutManager(getActivity()));

        //  评价
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MerchantGoodsEvaluateFragment.newInstance((Goods) getArguments().getSerializable("good"), EVA_ALL));
        fragmentList.add(MerchantGoodsEvaluateFragment.newInstance((Goods) getArguments().getSerializable("good"), EVA_GOOD));
        fragmentList.add(MerchantGoodsEvaluateFragment.newInstance((Goods) getArguments().getSerializable("good"), EVA_NORMAL));
        fragmentList.add(MerchantGoodsEvaluateFragment.newInstance((Goods) getArguments().getSerializable("good"), EVA_BAD));

        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList);
        binding.vpEvaluate.setAdapter(pagerAdapter);
        binding.vpEvaluate.setOffscreenPageLimit(1);
        binding.tabEvaluate.setupWithViewPager(binding.vpEvaluate);

        for (int i = 0; i < evaArray.length; i++) {
            TabLayout.Tab tab = binding.tabEvaluate.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.item_evaluate_tap);
                if (tab.getCustomView() != null) {
                    TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tv);
                    tv.setText(evaArray[i]);
                }
            }
        }
    }

}
