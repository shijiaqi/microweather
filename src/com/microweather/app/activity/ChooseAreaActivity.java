package com.microweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.microweather.app.R;
import com.microweather.app.callback.HttpCallbackListener;
import com.microweather.app.db.MicroWeatherDB;
import com.microweather.app.model.City;
import com.microweather.app.model.County;
import com.microweather.app.model.Province;
import com.microweather.app.util.FileUtils;
import com.microweather.app.util.HttpUtil;
import com.microweather.app.util.Utility;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ListView listView;
	private TextView titleText;
	private List<String> dataList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private MicroWeatherDB microWeatherDB;
	private ProgressDialog progressDialog;

	/**
	 * 省级列表
	 */
	private List<Province> provinceList;
	/**
	 * 市级列表
	 */
	private List<City> cityList;
	/**
	 * 县级列表
	 */
	private List<County> countyList;
	/**
	 * 选中省份
	 */
	private Province selectedProvince;
	/**
	 * 选中级别
	 */
	private City selectedCity;

	/**
	 * 当前选中级别
	 */
	private int currentLevel;
	
	private boolean isFromWeatherActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		isFromWeatherActivity=getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(prefs.getBoolean("city_selected", false)&&!isFromWeatherActivity){
			Intent intent = new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		microWeatherDB = MicroWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {

				/*if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCounties();
				} else if (currentLevel == LEVEL_COUNTY) {
					String countyCode = countyList.get(index).getCountyCode();
					Intent intent = new Intent(ChooseAreaActivity.this,
							WeatherActivity.class);
					intent.putExtra("county_code", countyCode);
					startActivity(intent);
					finish();
				}*/
				
				String code = provinceList.get(index).getProvinceCode();
				String name = provinceList.get(index).getProvinceName();
				Intent intent = new Intent(ChooseAreaActivity.this,
						WeatherActivity.class);
				intent.putExtra("code",code);
				intent.putExtra("name", name);
				startActivity(intent);
				finish();
			}
		});
		queryProvinces();
	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryProvinces() {
		provinceList = microWeatherDB.loadProvince();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * 查询选中省内所有的城市，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCities() {
		cityList = microWeatherDB.loadCity(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCounties() {
		countyList = microWeatherDB.loadCounty(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 * 
	 * @param code
	 * @param type
	 */
	private void queryFromServer(final String code, final String type) {
//		String address;
//		if (!TextUtils.isEmpty(code)) {
//			address = "http://www.weather.com.cn/data/list3/city" + code
//					+ ".xml";
//		} else {
//			address = "http://www.weather.com.cn/data/list3/city.xml";
//		}
		showProgressDialog();
		//读取本地城市列表
		String response =FileUtils.readAssets(this);
		//Log.v("ddd", response);
		
		boolean result = Utility.handleProvinceResponse(microWeatherDB,
				response);
		
		if (result) {
			// 通过runOnUiThread() 方法回到主线程处理逻辑
			runOnUiThread(new Runnable() {
				public void run() {
					closeProgressDialog();
					queryProvinces();
					/*if ("province".equals(type)) {
						queryProvinces();
					} else if ("city".equals(type)) {
						queryCities();
					} else if ("county".equals(type)) {
						queryCounties();
					}*/
				}

			});
		}
		
		/*HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvinceResponse(microWeatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(microWeatherDB,
							response, selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(microWeatherDB,
							response, selectedCity.getId());
				}

				if (result) {
					// 通过runOnUiThread() 方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}

					});
				}

			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});*/
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 捕捉Back键，根据当前的级别来判断，此时应该返回市列表，省列表，还是直接退出
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			if(isFromWeatherActivity){
				Intent intent = new Intent(this,WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
}
