package com.nong.nongo2o.module.personal.viewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entities.common.Address;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.common.fragment.SelectAreaFragment;
import com.nong.nongo2o.module.common.popup.AreaPopup;
import com.nong.nongo2o.module.personal.fragment.AddressEditFragment;

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

    public AddressEditVM(AddressEditFragment fragment, @Nullable Address address) {
        this.fragment = fragment;

        if (address != null) {
            //  编辑地址
            this.address = address;
            name.set(address.getConsigneeName());
            phone.set(address.getConsigneeMobile());
            viewStyle.hasSelectArea.set(true);
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
     * 保存按钮
     */
    public final ReplyCommand saveClick = new ReplyCommand(() -> {
        if (address != null) {
            //  编辑地址
            setAddressRequest(address);
        } else {
            //  新增地址
            Address newAddress = new Address();
            setAddressRequest(newAddress);
        }
    });

    private void setAddressRequest(Address addressRequest) {
        addressRequest.setConsigneeName(name.get());
        addressRequest.setConsigneeMobile(phone.get());
        addressRequest.setConsigneeAddress(addressStr.get());
        addressRequest.setDefaultAddr(isDefault.get() ? 1 : 0);
    }


}
