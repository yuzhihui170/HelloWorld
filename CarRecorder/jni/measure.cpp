#include <stdio.h>
#include <string.h>
#include <fcntl.h>
#include "measure.h"
#include "fflog.h"

#define  DEVICE_NAME  "/dev/lcm3d"  //device point
#define  MEASURE_ON 	0x5811
#define  MEASURE_OFF 	0x5812
#define  DISTANCE_SET 	0x5813
#define  DISTANCE_GET 	0x5814

int fd = -1;

JNIEXPORT jint JNICALL Java_com_takee_Measure_open(JNIEnv * env, jclass cls)
{
	if(fd < 0) {
		fd = open(DEVICE_NAME, O_RDWR); //打开设备
	}
	if(fd > 0) {
		LOGFD("measure open successful fd = %d", fd);
	}else {
		LOGFE("measure open failure fd = %d", fd);
	}
	return fd;
}

JNIEXPORT jint JNICALL Java_com_takee_Measure_ioctlMeasure(JNIEnv *env, jclass cls, jint controlCode)
{
	int CTLCODE = controlCode;
	if(fd < 0) {
		LOGFE("please open the device first!");
		return -1;
	}
	switch(CTLCODE) {
	case MEASURE_ON:
		ioctl(fd,MEASURE_ON);//调用驱动程序中的ioctrl接口，把命令measure_ON传下去，实现硬件操作
		LOGFD("measure ioctlMeasure MEASURE_ON");
	    break;
	case MEASURE_OFF:
		ioctl(fd,MEASURE_OFF);//调用驱动程序中的ioctrl接口，把命令measure_OFF传下去，实现硬件操作
		LOGFD("measure ioctlMeasure MEASURE_OFF");
		break;
	default:
		break;
	}
	return 1;
}

JNIEXPORT jint JNICALL Java_com_takee_Measure_close(JNIEnv *env, jclass cls)
{
	if(fd > 0) {
		close(fd);//关闭设备
		LOGFD("measure close device");
		return 0;
	}
	return -1;
}
