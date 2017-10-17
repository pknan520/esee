package com.nong.nongo2o.module.personal.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.Address;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.request.IdRequest;
import com.nong.nongo2o.module.personal.activity.AddressMgrActivity;
import com.nong.nongo2o.module.personal.fragment.AddressEditFragment;
import com.nong.nongo2o.module.personal.fragment.AddressListFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AddressUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-29.
 */

public class AddressListVM implements ViewModel {

    private AddressListFragment fragment;
    private int status;

    private final int pageSize = 20;
    private int total;

    //  地址列表
    public final ObservableList<ItemAddressVM> itemAddressVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemAddressVM> itemAddressBinding = ItemBinding.of(BR.viewModel, R.layout.item_address_list);

    @DrawableRes
    public final int emptyImg = R.mipmap.default_none;

    public AddressListVM(AddressListFragment fragment, int status) {
        this.fragment = fragment;
        this.status = status;

        initData();
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

        public final ObservableBoolean isEmpty = new ObservableBoolean(false);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        searchAddress(1, true);
    }

    /**
     * 获取地址列表
     */
    private void searchAddress(int page, boolean force) {
        viewStyle.isRefreshing.set(true);
        RetrofitHelper.getAddressAPI()
                .userAddressSearch(page, pageSize)
                .subscribeOn(Schedulers.io())
                .map(new ApiResponseFunc<>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    if (force) itemAddressVMs.clear();
                    total = resp.getTotal();
                    for (Address address : resp.getRows()) {
                        if (address.getDefaultAddr() == 1) {
                            itemAddressVMs.add(0, new ItemAddressVM(address));
                        } else {
                            itemAddressVMs.add(new ItemAddressVM(address));
                        }
                    }
                    viewStyle.isEmpty.set(itemAddressVMs.isEmpty());
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
        searchAddress(1, true);
    }

    /**
     * 加载更多
     */
    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>(integer -> onLoadMoreAddr());

    private void onLoadMoreAddr() {
        if (itemAddressVMs.size() < total) {
            searchAddress(itemAddressVMs.size() / pageSize + 1, false);
        } else {
            Toast.makeText(fragment.getActivity(), "没有更多内容了^.^", Toast.LENGTH_SHORT).show();
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

        public final ViewStyle viewStyle = new ViewStyle();

        public class ViewStyle {
            public final ObservableBoolean checkVisi = new ObservableBoolean(false);
            public final ObservableBoolean isDef = new ObservableBoolean(false);
        }

        /**
         * 初始化数据
         */
        private void initData() {
            if (address != null) {
                receiver.set(address.getConsigneeName());
                receivePhone.set(address.getConsigneeMobile());

                if (!TextUtils.isEmpty(address.getConsigneeProvince()) && !TextUtils.isEmpty(address.getConsigneeCity())
                        && ! TextUtils.isEmpty(address.getConsigneeArea()) && !TextUtils.isEmpty(address.getConsigneeTown())) {
                    List<City> cityList = AddressUtils.getCities(new String[]{address.getConsigneeProvince(), address.getConsigneeCity(), address.getConsigneeArea(), address.getConsigneeTown()});
                    receiveAddr.set(AddressUtils.getCityName(cityList) + address.getConsigneeAddress());
                } else {
                    receiveAddr.set(address.getConsigneeAddress());
                }

                if (status == AddressMgrActivity.ADDR_MGR) {
                    //  管理模式
                    viewStyle.checkVisi.set(true);
                    viewStyle.isDef.set(address.getDefaultAddr() == 1);
                } else {
                    //  选择模式
                    viewStyle.checkVisi.set(false);
                }
            }
        }

        /**
         * 选择框点击事件
         */
        public final ReplyCommand checkClick = new ReplyCommand(() -> {
            if (!viewStyle.isDef.get()) {
                //  遍历清除默认
                for (ItemAddressVM item : itemAddressVMs) {
                    item.viewStyle.isDef.set(false);
                }
                address.setDefaultAddr(1);
                viewStyle.isDef.set(true);
                setDefAddr();
            }
        });

        /**
         * 修改默认地址
         */
        private void setDefAddr() {
            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(address));

            RetrofitHelper.getAddressAPI()
                    .updateUserAddress(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        itemAddressVMs.add(0, itemAddressVMs.remove(itemAddressVMs.indexOf(this)));
                        Toast.makeText(fragment.getActivity(), "修改默认地址成功", Toast.LENGTH_SHORT).show();
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        }

        /**
         * item点击事件
         */
        public final ReplyCommand itemClick = new ReplyCommand(() -> {
            if (status == AddressMgrActivity.ADDR_MGR) {
                //  管理模式下进入编辑
                ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                        AddressEditFragment.newInstance(address), AddressEditFragment.TAG);
            } else if (status == AddressMgrActivity.ADDR_SEL) {
                //  选择模式下返回
                Intent intent = new Intent();
                intent.putExtra("address", address);
                fragment.getActivity().setResult(Activity.RESULT_OK, intent);
                fragment.getActivity().finish();
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
