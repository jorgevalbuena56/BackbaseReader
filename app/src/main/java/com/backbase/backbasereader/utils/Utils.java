package com.backbase.backbasereader.utils;

import com.backbase.backbasereader.model.City;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by valgood on 12/6/2017.
 */

public class Utils {
    public static List<City> getListByTree(Map<String, TreeSet<City>> queryCities) {
        List<City> cityTemp = new ArrayList<>();
        for (String key : queryCities.keySet()) {
            TreeSet<City> cityNames = queryCities.get(key);
            cityTemp.addAll(new ArrayList<>(cityNames));
        }
        return cityTemp;
    }
}
