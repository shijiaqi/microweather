package com.microweather.app.activity;

import com.microweather.app.R;
import com.microweather.app.callback.HttpCallbackListener;
import com.microweather.app.service.AutoUpdataService;
import com.microweather.app.util.HttpUtil;
import com.microweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private Button switch_city;
	private Button refresh_city;
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		initView();
		//String countyCode = getIntent().getStringExtra("county_code");
		String countyCode = getIntent().getStringExtra("code");
		name = getIntent().getStringExtra("name");
		initData(name);
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
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		
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
	private void queryWeatherCode(String name){
		
		String address ="http://apis.baidu.com/apistore/weatherservice/cityname?cityname="+name;
		queryFromServer(address,"weatherCode");
	}
	
	/**
	 * 
	 * 根据传入的地址和类型请求服务器数据
	 * @param address
	 * @param type
	 */
	private void queryFromServer(String address,final String type){
		//TODO 请求县级服务器数据
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				Log.v("ddd", response);
				if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				Log.v("ddd", "失败了");
				
			}
		});
		
	}
	
	/**
	 *从本地获取数据
	 */
	private void showWeather(){
		SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city", ""));
		temp1Text.setText(prefs.getString("l_tmp", "")+"℃");
		temp2Text.setText(prefs.getString("h_tmp", "")+"℃");
		weatherDespText.setText(prefs.getString("weather", "")+"|"+prefs.getString("temp", "")+"℃");
		publishText.setText("今天"+prefs.getString("time", "")+"发布");
		currentDateText.setText("日期："+prefs.getString("date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent  = new Intent(this,AutoUpdataService.class);
		startService(intent);
	}


	@Override
	public void onClick(View v) {
		// TODO 更新城市和刷新天气的点击事件
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_city:
			publishText.setText("同步中...");
			SharedPreferences prefs =PreferenceManager.getDefaultSharedPreferences(this);
			queryWeatherCode(prefs.getString("city", ""));
			
			break;
		default:
			break;
		}
		
	}


	
}
