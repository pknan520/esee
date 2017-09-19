package com.nong.nongo2o.uils;

import android.text.TextUtils;

import com.nong.nongo2o.entity.domain.City;
import com.nong.nongo2o.greenDaoGen.CityDao;
import com.nong.nongo2o.uils.dbUtils.GreenDaoManager;

import org.greenrobot.greendao.query.Query;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017-9-19.
 */

public class AddressUtils {

    public static List<City> getCities(String[] arrays) {
        if (arrays != null && arrays.length > 0) {
            List<City> cities = new ArrayList<>();

            CityDao cityDao = GreenDaoManager.getInstance().getSession().getCityDao();
            Query<City> query = cityDao.queryBuilder().where(CityDao.Properties.City_code.eq(arrays[0])).build();

            for (int i = 0; i < arrays.length; i++) {
                if (!TextUtils.isEmpty(arrays[i])) {
                    query.setParameter(0, arrays[i]);
                    City city = query.unique();
                    if (city != null) {
                        cities.add(city);
                    }
                }
            }

            if (cities.size() > 0) return cities;
        }

        return null;
    }

    public static String getCityNameWithSpace(List<City> cities) {
        if (cities != null && cities.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (City city : cities) {
                sb.append(city.getCity_name());
                sb.append(" ");
            }
            return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
        }
        return "";
    }

    public static String getCityName(List<City> cities) {
        if (cities != null && cities.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (City city : cities) {
                sb.append(city.getCity_name());
            }
            return sb.toString();
        }
        return "";
    }

}
