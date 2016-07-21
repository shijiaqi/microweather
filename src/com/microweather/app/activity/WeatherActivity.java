package com.microweather.app.activity;

import com.microweather.app.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private Button switch_city;
	private Button refresh_city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		initView();
		String countyCode = getIntent().getStringExtra("county_code");
		initData(countyCode);
	}
	
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		// TODO 初始化控件
		
		
		switch_city = (Button)findViewById(R.id.switch_city);
		refresh_city = (Button)findViewById(R.id.refresh_city);
		switch_city.setOnClickListener(this);
		refresh_city.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(String countyCode) {
		// TODO 初始化数据 
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号就去查天气
			
			queryWeatherCode(countyCode);
		}else{
			//没有显示本地天气
			showWeather();
		}
	}
	
	/**
	 * 查询县级代号所对应的天气代号
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode){
		//TODO 根据县级代号拼接链接
	}
	
	/**
	 * 根据传入的地址和类型请求服务器数据
	 */
	private void queryFromServer(){
		//TODO 请求县级服务器数据
	}
	
	/**
	 *从本地获取数据
	 */
	private void showWeather(){
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(this);
		//TODO 取出本地数据
	}


	@Override
	public void onClick(View v) {
		// TODO 更新城市和刷新天气的点击事件
		switch (v.getId()) {
		case R.id.switch_city:
			
			break;
		case R.id.refresh_city:
			
			break;
		default:
			break;
		}
		
	}


	
}
