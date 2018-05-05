package com.nong.nongo2o.module.common.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentAddFocusListBinding;
import com.nong.nongo2o.module.common.activity.AddFocusActivity;
import com.nong.nongo2o.module.common.viewModel.AddFocusListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-19.
 */

public class AddFocusListFragment extends RxBaseFragment {

    public static final String TAG = "AddFocusListFragment";

    private FragmentAddFocusListBinding binding;
    private AddFocusListVM vm;

    public static AddFocusListFragment newInstance() {
        return new AddFocusListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new AddFocusListVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddFocusListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
//        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setHasOptionsMenu(true);
        getActivity().setTitle("");

        binding.toolbar.inflateMenu(R.menu.menu_search);
        binding.toolbar.setNavigationIcon(R.mipmap.back_white);
        binding.toolbar.setNavigationOnClickListener(v -> {
            getActivity().finish();
        });
        MenuItem item = binding.toolbar.getMenu().findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        //设置搜索栏的默认提示
        searchView.setQueryHint("搜索用户");
        //默认刚进去就打开搜索栏
        searchView.setIconified(false);
        //设置输入文本的EditText
        SearchView.SearchAutoComplete et = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        //设置提示文本的颜色
        et.setHintTextColor(Color.WHITE);
        //设置输入文本的颜色
        et.setTextColor(Color.WHITE);
        //设置提交按钮是否可见
//        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (vm != null) vm.getUserList(query, 1, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }
}
