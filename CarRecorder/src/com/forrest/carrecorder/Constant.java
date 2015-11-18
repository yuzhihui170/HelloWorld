package com.forrest.carrecorder;

public class Constant {
	/**保存视频的目录*/
	public static String SAVE_DIRECTORY = "/mnt/sdcard/CarRecorder";
	
	/**空间不足的剩余容量*/
	public static long FREE_SPACE = 1* 1024 * 1024 * 1024; //1G 
	
	/**空间不足时删除的个数*/
	public static int DELETE_NUM = 10;
	
	/**每次录像的时间间隔  单位:min*/
	public static int TIME_SPAN = 1;
	
	/**空间不足时删除的文件个数*/
	public static int DELETE_FILE_NUM = 10;
}
