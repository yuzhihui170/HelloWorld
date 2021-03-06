package com.yzh.Utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

public class FileUtils {
	private final static String TAG = "FileUtils";
	
	/** 删除制定文件目录下所有文件,但是保留了目录 */
	public static void deleteAllFiles(File directoryFile) {
		File[] fileList = directoryFile.listFiles();
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {
					fileList[i].delete();
				} else if (fileList[i].isDirectory()) {
					deleteAllFiles(fileList[i]);
				}
			}
		}
	}

	public static void deleteFile(File oldPath) {
		if (oldPath.isDirectory()) {
			File[] files = oldPath.listFiles();
			for (File file : files) {
				deleteFile(file);
			}
		} else {
			oldPath.delete();
		}
	}
	
	/** 创建保存目录,保证后面创建文件正确 */
	public static void mkDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**删除指定目录中num数目的文件*/
	public static void deleteFile(File dirFile, int num) {
		String[] filePaths = dirFile.list();
		Arrays.sort(filePaths);
		for (String string : filePaths) {
			Log.d(TAG, " filePath = " + string);
		}
	}
	
	/**删除指定文件夹下的指定数量的文件*/
	public static void deleteFile(String dirPath, int num) {
		File dirFile = new File(dirPath);
		
		String[] filePaths = dirFile.list();
		Arrays.sort(filePaths);
//		for(String string : filePaths) {
//			Log.d(TAG, " filePath = " + string);
//		}
		if(filePaths.length < num) {
			return;
		}
		for(int i=0; i<num; i++) {
			File file = new File(dirPath + File.separator + filePaths[i]);
			file.delete();
		}
//		filePaths = new File(dirPath).list();
//		Arrays.sort(filePaths);
//		for(String string : filePaths) {
//			Log.d(TAG, " filePath = " + string);
//		}
	}
}
