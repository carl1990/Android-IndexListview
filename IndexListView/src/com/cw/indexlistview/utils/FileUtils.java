package com.cw.indexlistview.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import android.content.Context;

import com.cw.indexlistview.IndexListviewApplication;
import com.cw.indexlistview.model.CityListCityModel;

public class FileUtils {

    public static CityListCityModel[] readCityList()
    {
        List<CityListCityModel> list = new ArrayList<CityListCityModel>();
        CityListCityModel cities[];
        String json = readAssertsFile("city.json", IndexListviewApplication.getContext());
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, CityListCityModel.class);
            list = mapper.readValue(json.toString(), javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cities = list.toArray(new CityListCityModel[list.size()]);
        return cities;
    }

    public static String readAssertsFile(String fileName, Context context)
    {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
