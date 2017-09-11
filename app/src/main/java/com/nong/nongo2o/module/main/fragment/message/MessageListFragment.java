package com.nong.nongo2o.module.main.fragment.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseFragment;
import com.nong.nongo2o.databinding.FragmentMessageListBinding;
import com.nong.nongo2o.module.main.viewModel.message.MessageListVM;
import com.nong.nongo2o.widget.recyclerView.LinearItemDecoration;

/**
 * Created by Administrator on 2017-7-19.
 */

public class MessageListFragment extends RxBaseFragment {

    private FragmentMessageListBinding binding;
    private MessageListVM vm;

    public static MessageListFragment newInstance() {
        return new MessageListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (vm == null) {
            vm = new MessageListVM(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageListBinding.inflate(inflater, container, false);
        initView();
        binding.setViewModel(vm);
        return binding.getRoot();
    }

    private void initView() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        getActivity().setTitle("");
        binding.tvToolbarTitle.setText("消息");

        binding.rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rv.addItemDecoration(new LinearItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_vertical_divider));
    }
}
