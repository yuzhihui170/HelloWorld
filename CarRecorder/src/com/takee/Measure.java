package com.takee;
/**3D 测距工具类*/
public class Measure {
	public static native int open();

	public static native int ioctlMeasure(int controlCode);

	public static native int close();
}
