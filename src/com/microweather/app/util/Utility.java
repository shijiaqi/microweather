package com.microweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.microweather.app.db.MicroWeatherDB;
import com.microweather.app.model.City;
import com.microweather.app.model.CityModel;
import com.microweather.app.model.County;
import com.microweather.app.model.Province;

public class Utility {
	
	/**
	 * 解析和处理服务器返回的省级数据
	 * @param microWeatherDB
	 * @param response
	 * @return
	 */
	public synchronized static boolean handleProvinceResponse(MicroWeatherDB microWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String [] allProvinces = response.split("\\|");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p :allProvinces){
					String [] array=p.split("\\:");
					Province province = new Province();
					province.setProvinceCode(array[1]);
					province.setProvinceName(array[0]);
					//将解析出来的对象存到Province表中
					microWeatherDB.saveProvince(province);
				}
				return true;
			}
			 
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的市级数据
	 * @param microWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public static boolean handleCitiesResponse(MicroWeatherDB microWeatherDB,String response,int provinceId){
		
		if(!TextUtils.isEmpty(response)){
			String [] allCities = response.split(",");
			if(allCities!=null && allCities.length>0){
				for(String c : allCities){
					String [] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					microWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的县级数据
	 * @param microWeatherDB
	 * @param response
	 * @param cityId
	 * @return
	 */
	public static boolean handleCountiesResponse(MicroWeatherDB microWeatherDB,String response,int cityId){
		
		if(!TextUtils.isEmpty(response)){
			String [] allCounties =response.split(",");
			if(allCounties!=null && allCounties.length>0){
				for(String c:allCounties){
					String [] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					microWeatherDB.saveCounty(county);
				}
				return true;
			}
			
		}
		return false;
	}
	
	/**
	 * 解析服务器返回的数据，并将解析出来的数据存到本地
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context,String response){
		//TODO 解析具体天气的方法
		Gson gson = new Gson();
		CityModel cm=gson.fromJson(response, CityModel.class);
		saveWeatherInfo(context,cm);//保存到本地
	}
	
	public static void saveWeatherInfo(Context context,CityModel cm){	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString("city", cm.getRetData().city);
		editor.putString("pinyin", cm.getRetData().pinyin);
		editor.putString("citycode", cm.getRetData().citycode);
		editor.putString("date", cm.getRetData().date);
		editor.putString("time", cm.getRetData().time);
		editor.putString("postCode", cm.getRetData().postCode);
		editor.putString("longitude", cm.getRetData().longitude);
		editor.putString("latitude", cm.getRetData().latitude);
		editor.putString("altitude", cm.getRetData().altitude);
		editor.putString("weather", cm.getRetData().weather);
		editor.putString("temp", cm.getRetData().temp);
		editor.putString("l_tmp", cm.getRetData().l_tmp);
		editor.putString("h_tmp", cm.getRetData().h_tmp);
		editor.putString("WD", cm.getRetData().WD);
		editor.putString("WS", cm.getRetData().WS);
		editor.putString("sunrise", cm.getRetData().sunrise);
		editor.putString("sunset", cm.getRetData().sunset);
		editor.putBoolean("city_selected", true);
		editor.commit();
	}

}
