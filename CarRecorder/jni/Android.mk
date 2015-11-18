LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
	
		
LOCAL_SRC_FILES :=  \
			measure.cpp
			
LOCAL_CFLAGS := -O2 $(INCLUDES) -Wno-psabi

LOCAL_LDLIBS := -llog  

LOCAL_MODULE    := measure

include $(BUILD_SHARED_LIBRARY)

