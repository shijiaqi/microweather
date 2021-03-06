package com.microweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import android.util.Log;

import com.microweather.app.callback.HttpCallbackListener;

public class HttpUtil {

	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				HttpURLConnection connection =null;				
				try {
					URL url =new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setRequestProperty("apikey",  "c28960492af376d319f4b9898c17b551");
				    connection.connect();
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
					StringBuilder response =new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
						response.append(line);
					}
					if(listener!=null){
						//请求成功回调
						listener.onFinish(response.toString());
					}
					
				} catch (Exception e) {
					if(listener!=null){
						//请求异常回调
						listener.onError(e);
					}
					e.printStackTrace();
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();

	}

}
