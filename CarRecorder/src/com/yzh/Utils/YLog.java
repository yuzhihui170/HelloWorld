package com.yzh.Utils;

import android.util.Log;

public class YLog {
	final private static String TAG = "yzh";
	
	public static void i(String msg) {
		Log.i(TAG, msg);
	}
	
	public static void d(String msg) {
		Log.d(TAG, msg);
	}
	
	public static void e(String msg) {
		Log.e(TAG, msg);
	}

}
