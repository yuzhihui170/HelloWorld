package com.yzh.FileUtils;

import java.io.File;
import java.util.ArrayList;


public class FileUtils {
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

	public void deleteFile(File oldPath) {
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
	public void mkDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
