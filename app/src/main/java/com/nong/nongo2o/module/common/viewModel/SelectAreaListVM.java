package com.nong.nongo2o.module.common.viewModel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.kelin.mvvmlight.base.ViewModel;
import com.kelin.mvvmlight.command.ReplyCommand;
import com.nong.nongo2o.BR;
import com.nong.nongo2o.R;
import com.nong.nongo2o.base.RxBaseActivity;
import com.nong.nongo2o.entities.common.Area;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.common.fragment.SelectAreaListFragment;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-8-19.
 */

public class SelectAreaListVM implements ViewModel {

    private SelectAreaListFragment fragment;
    private int level;
    private Area areaP, areaC;

    public SelectAreaListVM(SelectAreaListFragment fragment, int level, Area areaP, Area areaC) {
        this.fragment = fragment;
        this.level = level;
        this.areaP = areaP;
        this.areaC = areaC;
        //  假数据
        initFakeData();
    }

    private void initFakeData() {
        switch (level) {
            case SelectAreaActivity.AREA_PROVINCE:
                for (int i = 0; i < 20; i++) {
                    itemAreaListVMs.add(new ItemAreaListVM(new Area(i, 0, "省-父编号：" + 0 + "，子编号：" + i)));
                }
                break;
            case SelectAreaActivity.AREA_CITY:
                for (int i = 0; i < 20; i++) {
                    itemAreaListVMs.add(new ItemAreaListVM(new Area(i, areaP.getAreaCode(), "市-父编号" + areaP.getAreaCode() + "，子编号：" + i)));
                }
                break;
            case SelectAreaActivity.AREA_DISTRICT:
                for (int i = 0; i < 20; i++) {
                    itemAreaListVMs.add(new ItemAreaListVM(new Area(i, areaC.getAreaCode(), "区-父编号" + areaC.getAreaCode() + "，子编号：" + i)));
                }
                break;
        }
    }

    public final ObservableList<ItemAreaListVM> itemAreaListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemAreaListVM> itemAreaListBinding = ItemBinding.of(BR.viewModel, R.layout.item_area_list);

    public class ItemAreaListVM implements ViewModel {

        private Area area;

        public final ObservableField<String> areaName = new ObservableField<>();

        public ItemAreaListVM(Area area) {
            this.area = area;
            this.areaName.set(area.getAreaName());
        }

        public final ReplyCommand itemClick = new ReplyCommand(() -> {
            switch (level) {
                case SelectAreaActivity.AREA_PROVINCE:
                    ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                            SelectAreaListFragment.newInstance(SelectAreaActivity.AREA_CITY, area, null), SelectAreaListFragment.TAG);
                    break;
                case SelectAreaActivity.AREA_CITY:
                    ((RxBaseActivity) fragment.getActivity()).switchFragment(R.id.fl, fragment,
                            SelectAreaListFragment.newInstance(SelectAreaActivity.AREA_DISTRICT, areaP, area), SelectAreaListFragment.TAG);
                    break;
                case SelectAreaActivity.AREA_DISTRICT:
                    Intent intent = new Intent();
                    intent.putExtra("areaP", areaP);
                    intent.putExtra("areaC", areaC);
                    intent.putExtra("areaD", area);
                    fragment.getActivity().setResult(Activity.RESULT_OK, intent);
                    fragment.getActivity().finish();
                    fragment.getActivity().overridePendingTransition(0, R.anim.anim_right_out);
                    break;
            }
        });
    }
}
