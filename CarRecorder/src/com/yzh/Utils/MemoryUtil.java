package com.yzh.Utils;

import java.io.File;
import java.util.Arrays;

import com.forrest.carrecorder.Constant;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

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
	
	/** 获得SD卡总大小 */
	public String getSDTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		Log.d("mem","pathSD = " + path.getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCountLong();
		return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
	}

	/** 获得sd卡剩余容量，即可用大小 */
	public String getSDAvailableSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
	}
	
	/** 获得sd卡剩余容量，即可用大小 */
	public long getSDAvailableSizeLong() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		return blockSize * availableBlocks;
	}

	/** 获得机身内存总大小 */
	public String getRomTotalSize() {
		File path = Environment.getDataDirectory();
		Log.d("mem","pathIn = " + path.getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCountLong();
		return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
	}

	/** 获得机身可用内存  */
	public String getRomAvailableSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
	}
	
	/** 获得机身可用内存  */
	public long getRomAvailableSizeLong() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSizeLong();
		long availableBlocks = stat.getAvailableBlocksLong();
		return blockSize * availableBlocks;
	}

}
