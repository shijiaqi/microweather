package com.microweather.app.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;

public class FileUtils {

	public static String readAssets(Context context) {
		try {
			InputStream is = context.getAssets().open("city.txt");
			InputStreamReader reader = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(reader);
			StringBuffer buffer = new StringBuffer("");
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
				//buffer.append("\n");
			}
			return buffer.toString();
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取编码格式
	 * 
	 * @param is
	 * @return
	 */
	public static String getCode(InputStream is) {
		try {
			BufferedInputStream bin = new BufferedInputStream(is);
			int p;

			p = (bin.read() << 8) + bin.read();

			String code = null;

			switch (p) {
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "UTF-16BE";
				break;
			default:
				code = "GBK";
			}
			is.close();
			return code;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
