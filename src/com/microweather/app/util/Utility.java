package com.microweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;


import com.microweather.app.db.MicroWeatherDB;
import com.microweather.app.model.City;
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
			String [] allProvinces = response.split(",");
			if(allProvinces!=null&&allProvinces.length>0){
				for(String p :allProvinces){
					String [] array=p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
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
		
		saveWeatherInfo(context,"","");//保存到本地
	}
	
	public static void saveWeatherInfo(Context context,String cityName,String weatherCode){
		//TODO 保存天气的方法
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString("", "");
		editor.commit();
	}

}
