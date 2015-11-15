package com.yzh.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {
	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
}
