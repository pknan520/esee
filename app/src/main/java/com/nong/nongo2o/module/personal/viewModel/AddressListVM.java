package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.widget.Toast;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entities.common.Address;
import com.nong.nongo2o.entities.common.ApiListResponse;
import com.nong.nongo2o.entities.response.User;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.fragment.AddressEditFragment;
import com.nong.nongo2o.module.personal.fragment.AddressListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.widget.checkbox.ViewBindingAdapter;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-6-29.
 */

public class AddressListVM implements ViewModel {

    private AddressListFragment fragment;
    private int status;

    private List<Address> addressList;
    private final int pageSize = 10;
    private int total;

    /**
     * 地址列表
     */
    public final ObservableList<ItemAddressVM> itemAddressVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemAddressVM> itemAddressBinding = ItemBinding.of(BR.viewModel, R.layout.item_address_list);

    public AddressListVM(AddressListFragment fragment, int status) {
        this.fragment = fragment;
        this.status = status;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        itemAddressVMs.clear();
        searchAddress(1);
    }

    /**
     * 获取地址列表
     */
    private void searchAddress(int page) {
        viewStyle.isRefreshing.set(true);

        RetrofitHelper.getUserAPI()
//                .searchAddress(User.getInstance().getUserCode(), page, 10)
                .searchAddress("usercode000000000000", page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    total = resp.getTotal();
                    addressList = resp.getRows();
                    for (Address address : addressList) {
                        itemAddressVMs.add(new ItemAddressVM(address));
                    }
                }, throwable -> {
                    Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    viewStyle.isRefreshing.set(false);
                }, () -> viewStyle.isRefreshing.set(false));
    }

    /**
     * 添加地址
     */
    public final ReplyCommand addClick = new ReplyCommand(() -> {
        ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                AddressEditFragment.newInstance(), AddressEditFragment.TAG);
    });

    /**
     * 刷新
     */
    public final ReplyCommand onRefreshCommand = new ReplyCommand(this::onRefresh);

    private void onRefresh() {
        itemAddressVMs.clear();
        searchAddress(1);
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> onLoadMoreAddr());

    private void onLoadMoreAddr() {
        if (itemAddressVMs.size() < total) {
            searchAddress(itemAddressVMs.size() / pageSize + 1);
        }
    }

    public class ItemAddressVM implements ViewModel {

        private Address address;
        @DrawableRes
        public final int isDef = R.mipmap.icon_select;
        @DrawableRes
        public final int notDef = R.mipmap.icon_select_none;

        public final ObservableField<String> receiver = new ObservableField<>();
        public final ObservableField<String> receivePhone = new ObservableField<>();
        public final ObservableField<String> receiveAddr = new ObservableField<>();

        public ItemAddressVM(Address address) {
            this.address = address;
            initData();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            receiver.set(address.getConsigneeName());
            receivePhone.set(address.getConsigneeMobile());
            receiveAddr.set(address.getConsigneeAddress());

            if (status == AddressMgrActivity.ADDR_MGR) {
                //  管理模式
                viewStyle.checkVisi.set(true);
                viewStyle.isDef.set(address.getDefaultAddr() == 1);
            } else {
                //  选择模式
            }
        }

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean checkVisi = new ObservableBoolean(false);
            public final ObservableBoolean isDef = new ObservableBoolean(false);
        }

        /**
         * 选择框点击事件
         */
        public final ReplyCommand checkClick = new ReplyCommand(() -> {
            if (!viewStyle.isDef.get()) {
                //  遍历清除默认
                for (int i = 0; i < addressList.size(); i++) {
                    addressList.get(i).setDefaultAddr(0);
                    itemAddressVMs.get(i).viewStyle.isDef.set(false);
                }
                address.setDefaultAddr(1);
                viewStyle.isDef.set(true);
            }
        });

        /**
         * 选择状态下点击item选择
         */
        public final ReplyCommand itemClick = new ReplyCommand(() -> {
            if (status == AddressMgrActivity.ADDR_SEL) {

            }
        });

        /**
         * 编辑地址
         */
        public final ReplyCommand editClick = new ReplyCommand(() -> {
            ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                    AddressEditFragment.newInstance(address), AddressEditFragment.TAG);
        });
    }
}
