package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entity.domain.Address;
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.entity.request.CreateAddressRequest;
import com.nong.nongo2o.entity.request.UpdateAddressRequest;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;
import com.nong.nongo2o.module.common.popup.AreaPopup;
import com.nong.nongo2o.module.personal.fragment.AddressEditFragment;
import com.nong.nongo2o.network.RetrofitHelper;
import com.nong.nongo2o.network.auxiliary.ApiResponseFunc;
import com.nong.nongo2o.uils.AddressUtils;
import com.nong.nongo2o.widget.checkbox.ViewBindingAdapter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017-6-30.
 */

public class AddressEditVM implements ViewModel {

    private AddressEditFragment fragment;
    private Address address = null;

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> phone = new ObservableField<>();
    public final ObservableField<String> city = new ObservableField<>();
    public final ObservableField<String> addressStr = new ObservableField<>();
    public final ObservableBoolean isDefault = new ObservableBoolean(false);

    private City cityP, cityC, cityD, cityS;

    public AddressEditVM(AddressEditFragment fragment, @Nullable Address address) {
        this.fragment = fragment;

        if (address != null) {
            //  编辑地址
            this.address = address;
            name.set(address.getConsigneeName());
            phone.set(address.getConsigneeMobile());
            if (!TextUtils.isEmpty(address.getConsigneeProvince()) && !TextUtils.isEmpty(address.getConsigneeCity())
                    && ! TextUtils.isEmpty(address.getConsigneeArea()) && !TextUtils.isEmpty(address.getConsigneeTown())) {
                viewStyle.hasSelectArea.set(true);
                List<City> cityList = AddressUtils.getCities(new String[] {address.getConsigneeProvince(), address.getConsigneeCity(), address.getConsigneeArea(), address.getConsigneeTown()});
                if (cityList != null && cityList.size() > 3) {
                    cityP = cityList.get(0);
                    cityC = cityList.get(1);
                    cityD = cityList.get(2);
                    cityS = cityList.get(3);
                }
                city.set(AddressUtils.getCityNameWithSpace(cityList));
            }
            addressStr.set(address.getConsigneeAddress());
            if (address.getDefaultAddr() == 1) {
                isDefault.set(true);
            }
        } else {
            //  新增地址
            city.set("请选择所在城市");
        }
    }

    public final ViewStyle viewStyle = new ViewStyle();

    public class ViewStyle {
        public final ObservableBoolean hasSelectArea = new ObservableBoolean(false);
    }

    /**
     * 选择城市
     */
    public final ReplyCommand selectCityClick = new ReplyCommand(() -> {
        fragment.startActivityForResult(SelectAreaActivity.newIntent(fragment.getActivity()), AddressEditFragment.GET_AREA);
        fragment.getActivity().overridePendingTransition(R.anim.anim_right_in, 0);
    });

    /**
     * 设置城市
     */
    public void setCities(City cityP, City cityC, City cityD, City cityS) {
        viewStyle.hasSelectArea.set(true);
        this.cityP = cityP;
        this.cityC = cityC;
        this.cityD = cityD;
        this.cityS = cityS;
        city.set(cityP.getCity_name() + " " + cityC.getCity_name() + " " + cityD.getCity_name() + " " + cityS.getCity_name());
    }

    /**
     * 保存按钮
     */
    public final ReplyCommand saveClick = new ReplyCommand(() -> {
        if (TextUtils.isEmpty(name.get())) {
            Toast.makeText(fragment.getActivity(), "请输入收货人姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone.get())) {
            Toast.makeText(fragment.getActivity(), "请输入收货人电话", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cityP == null || cityC == null || cityD == null || cityS == null) {
            Toast.makeText(fragment.getActivity(), "请选择所在地区", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addressStr.get())) {
            Toast.makeText(fragment.getActivity(), "请输入收货地址", Toast.LENGTH_SHORT).show();
            return;
        }

        saveAddress();
    });

    private void saveAddress() {
        if (address != null) {
            //  编辑地址
            UpdateAddressRequest updateAddressRequest = new UpdateAddressRequest();
            updateAddressRequest.setId(address.getId());
            updateAddressRequest.setAddressCode(address.getAddressCode());

            updateAddressRequest.setDefaultAddr(isDefault.get() ? 1 : 0);

            updateAddressRequest.setConsigneeName(name.get());
            updateAddressRequest.setConsigneeMobile(phone.get());
            updateAddressRequest.setConsigneeAddress(addressStr.get());

            updateAddressRequest.setConsigneeProvince(cityP.getCity_code());
            updateAddressRequest.setConsigneeCity(cityC.getCity_code());
            updateAddressRequest.setConsigneeArea(cityD.getCity_code());
            updateAddressRequest.setConsigneeTown(cityS.getCity_code());

            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(updateAddressRequest));

            RetrofitHelper.getAddressAPI().updateUserAddress(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        fragment.getFragmentManager().popBackStack();
                        Toast.makeText(fragment.getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        } else {
            //  新增地址
            CreateAddressRequest createAddressRequest = new CreateAddressRequest();

            createAddressRequest.setDefaultAddr(isDefault.get() ? 1 : 0);
            createAddressRequest.setConsigneeName(name.get());
            createAddressRequest.setConsigneeMobile(phone.get());
            createAddressRequest.setConsigneeAddress(addressStr.get());

            createAddressRequest.setConsigneeProvince(cityP.getCity_code());
            createAddressRequest.setConsigneeCity(cityC.getCity_code());
            createAddressRequest.setConsigneeArea(cityD.getCity_code());
            createAddressRequest.setConsigneeTown(cityS.getCity_code());

            RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                    new Gson().toJson(createAddressRequest));

            RetrofitHelper.getAddressAPI().userAddress(requestBody)
                    .subscribeOn(Schedulers.io())
                    .map(new ApiResponseFunc<>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resp -> {
                        fragment.getFragmentManager().popBackStack();
                        Toast.makeText(fragment.getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                    }, throwable -> Toast.makeText(fragment.getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show());

        }
    }

}
