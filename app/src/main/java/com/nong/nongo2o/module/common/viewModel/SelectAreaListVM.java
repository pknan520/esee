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
import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.greenDaoGen.CityDao;
import com.nong.nongo2o.module.common.activity.SelectAreaActivity;
import com.nong.nongo2o.module.common.fragment.SelectAreaListFragment;
import com.nong.nongo2o.uils.dbUtils.GreenDaoManager;

import org.greenrobot.greendao.rx.RxQuery;

import java.util.List;

import io.reactivex.Observable;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Administrator on 2017-8-19.
 */

public class SelectAreaListVM implements ViewModel {

    private SelectAreaListFragment fragment;
    private int level;
    private City cityP, cityC, cityD;

    //  城市列表
    public final ObservableList<ItemAreaListVM> itemAreaListVMs = new ObservableArrayList<>();
    public final ItemBinding<ItemAreaListVM> itemAreaListBinding = ItemBinding.of(BR.viewModel, R.layout.item_area_list);

    private CityDao cityDao;

    public SelectAreaListVM(SelectAreaListFragment fragment, int level, City cityP, City cityC, City cityD) {
        this.fragment = fragment;
        this.level = level;
        this.cityP = cityP;
        this.cityC = cityC;
        this.cityD = cityD;

        cityDao = GreenDaoManager.getInstance().getSession().getCityDao();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        switch (level) {
            case SelectAreaActivity.AREA_PROVINCE:
                List<City> citiesP = cityDao.queryBuilder().where(CityDao.Properties.Parent_code.eq("0")).list();
                for (City city : citiesP) {
                    itemAreaListVMs.add(new ItemAreaListVM(city));
                }
                break;
            case SelectAreaActivity.AREA_CITY:
                List<City> citiesC = cityDao.queryBuilder().where(CityDao.Properties.Parent_code.eq(cityP.getCity_code())).list();
                for (City city : citiesC) {
                    itemAreaListVMs.add(new ItemAreaListVM(city));
                }
                break;
            case SelectAreaActivity.AREA_DISTRICT:
                List<City> citiesD = cityDao.queryBuilder().where(CityDao.Properties.Parent_code.eq(cityC.getCity_code())).list();
                for (City city : citiesD) {
                    itemAreaListVMs.add(new ItemAreaListVM(city));
                }
                break;
            case SelectAreaActivity.AREA_STREET:
                List<City> citiesS = cityDao.queryBuilder().where(CityDao.Properties.Parent_code.eq(cityD.getCity_code())).list();
                for (City city : citiesS) {
                    itemAreaListVMs.add(new ItemAreaListVM(city));
                }
                break;
        }
    }

    /**
     * 获取当前等级
     */
    public int getLevel() {
        return level;
    }

    /**
     * 降低当前等级，并刷新数据
     */
    public void downLevel() {
        level--;
        itemAreaListVMs.clear();
        initData();
    }

    public class ItemAreaListVM implements ViewModel {

        private City mCity;

        public final ObservableField<String> areaName = new ObservableField<>();

        public ItemAreaListVM(City city) {
            this.mCity = city;
            this.areaName.set(city.getCity_name());
        }

        public final ReplyCommand itemClick = new ReplyCommand(() -> {
            switch (level) {
                case SelectAreaActivity.AREA_PROVINCE:
                    cityP = mCity;
                    upLevel();
                    break;
                case SelectAreaActivity.AREA_CITY:
                    cityC = mCity;
                    upLevel();
                    break;
                case SelectAreaActivity.AREA_DISTRICT:
                    cityD = mCity;
                    upLevel();
                    break;
                case SelectAreaActivity.AREA_STREET:
                    Intent intent = new Intent();
                    intent.putExtra("cityP", cityP);
                    intent.putExtra("cityC", cityC);
                    intent.putExtra("cityD", cityD);
                    intent.putExtra("cityS", mCity);
                    fragment.getActivity().setResult(Activity.RESULT_OK, intent);
                    fragment.getActivity().finish();
                    fragment.getActivity().overridePendingTransition(0, R.anim.anim_right_out);
                    break;
            }
        });

        private void upLevel() {
            level++;
            itemAreaListVMs.clear();
            initData();
        }
    }
}
