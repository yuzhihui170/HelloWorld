package com.yzh.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtil {
	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}
	
    public static String formatTime(long millis, boolean showMillis) {
        final int totalSeconds = (int) millis / 1000;
        final int millionSeconds = (int) (millis % 1000) / 10;
        final int seconds = totalSeconds % 60;
        final int minutes = (totalSeconds / 60) % 60;
        final int hours = totalSeconds / 3600;
        String text = null;
        if (showMillis) {
            if (hours > 0) {
                text = String.format(Locale.ENGLISH, "%d:%02d:%02d.%02d", hours, minutes, seconds,
                        millionSeconds);
            } else {
                text = String.format(Locale.ENGLISH, "%02d:%02d.%02d", minutes, seconds,
                        millionSeconds);
            }
        } else {
            if (hours > 0) {
                text = String.format(Locale.ENGLISH, "%d:%02d:%02d", hours, minutes, seconds);
            } else {
                text = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
            }
        }
        
        return text;
    }
}
