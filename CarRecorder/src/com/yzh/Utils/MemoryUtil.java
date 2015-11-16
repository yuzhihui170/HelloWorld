package com.yzh.Utils;

import java.io.File;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class MemoryUtil {
	private Context mContext;
	private ActivityManager mActivityManager;
	ActivityManager.MemoryInfo mMemoryInfo; 
	
	public MemoryUtil(Context context) {
		this.mContext = context;
		mActivityManager = (ActivityManager)mContext.getSystemService(Activity.ACTIVITY_SERVICE);
		mMemoryInfo = new ActivityManager.MemoryInfo();
	}
	
	public String getMemoryInfo() {
		mActivityManager.getMemoryInfo(mMemoryInfo);
		long memSize = mMemoryInfo.availMem; 
		YLog.d("mMemoryInfo.availMem = " + Formatter.formatFileSize(mContext, mMemoryInfo.availMem));
		YLog.d("mMemoryInfo.totalMem = " + Formatter.formatFileSize(mContext, mMemoryInfo.totalMem));
		String leftMemSize = Formatter.formatFileSize(mContext, memSize);
		return leftMemSize;
	}
	
	/**
	 * 获得SD卡总大小
	 * 
	 * @return
	 */
	private String getSDTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
	}

	/**
	 * 获得sd卡剩余容量，即可用大小
	 * 
	 * @return
	 */
	private String getSDAvailableSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
	}

	/**
	 * 获得机身内存总大小
	 * 
	 * @return
	 */
	private String getRomTotalSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCountLong();
		return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
	}

	/**
	 * 获得机身可用内存
	 * 
	 * @return
	 */
	private String getRomAvailableSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
	}

}
