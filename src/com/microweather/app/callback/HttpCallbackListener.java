package com.microweather.app.callback;

public interface HttpCallbackListener {
	
	void onFinish(String response);
	
	void onError(Exception e);

}
