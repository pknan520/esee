package com.nong.nongo2o.module.common.viewModel;

import android.widget.PopupWindow;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.module.common.popup.AreaPopup;

/**
 * Created by Administrator on 2017-8-19.
 */

public class PopupAreaVM implements ViewModel {

    private AreaPopup areaPopup;

    public PopupAreaVM(AreaPopup areaPopup) {
        this.areaPopup = areaPopup;
    }

    public final ReplyCommand cancelClick = new ReplyCommand(() -> areaPopup.dismiss());

}
