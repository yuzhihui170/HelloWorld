package com.forrest.carrecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.yzh.Utils.MemoryUtil;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String TAG = "yzh";
	private CarRecorderApplication mCarRecorderApplication;
	private Camera mCamera;
	private Camera.Parameters mParam;
	private RecorderSurfaceView mRecorderSurfaceView; 
	private Button mBtnBack;        //后台录像
	private Button mBtn_stopService; //停止后台录像服务
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mCarRecorderApplication = (CarRecorderApplication) getApplication();
//		mRecorderSurfaceView = (RecorderSurfaceView)findViewById(R.id.surfaceView1);
		mBtnBack = (Button)findViewById(R.id.btn_back);
		mBtn_stopService = (Button) findViewById(R.id.btn_topService);
		setViewListener();
//		getCameraInfo();
		mkDir();
		startService(new Intent(this, RecorderService.class));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mCarRecorderApplication.setMainActivityDisplay(true);
		Intent mIntent = new Intent(RecorderService.ACTION_NAME);
		mIntent.putExtra("display", true);
		sendBroadcast(mIntent);
//		if(mRecorderSurfaceView != null) {
//			mRecorderSurfaceView.setKeepScreenOn(true); //设置屏幕长亮
//		}
//		String timeStamp = new SimpleDateFormat("yyyy_MM_dd", Locale.CHINA).format(new Date());
//		Log.d(TAG,"timeStamp = " + timeStamp);
		MemoryUtil memoryUtil = new MemoryUtil(this);
		Log.d("mem"," build-in = " + memoryUtil.getRomTotalSize() + "--" + memoryUtil.getRomAvailableSize() + "--" + memoryUtil.getRomAvailableSizeLong());
		Log.d("mem"," SD = " + memoryUtil.getSDTotalSize() + "--" + memoryUtil.getSDAvailableSize());
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Intent mIntent = new Intent(RecorderService.ACTION_NAME);
		mIntent.putExtra("display", false);
		sendBroadcast(mIntent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mCarRecorderApplication.setMainActivityDisplay(false);
	}
	
	private void setViewListener() {
		mBtnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mBtn_stopService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(new Intent(MainActivity.this, RecorderService.class));
			}
		});
	}
	
	/** 创建保存图片的目录,保存后面创建文件正确 */
	private void mkDir() {
//		File file = new File("/mnt/internal_sd/CarRecoder");
		File file = new File(Constant.SAVE_DIRECTORY);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public void getCameraInfo() {
		mCamera = Camera.open(0);
		mParam = mCamera.getParameters();
		List<Size> listSizes =  mParam.getSupportedPreviewSizes();
		for(int i = 0; i<listSizes.size(); i++) {
			Log.d(TAG,"SupportedPreviewSizes = " + listSizes.get(i).width + " * " + listSizes.get(i).height);
		}

		List<Size> listSizesVideo = mParam.getSupportedVideoSizes();
		Log.d(TAG,"listSizesVideo = "  + listSizesVideo);
		if(listSizesVideo != null) {
			for(int i = 0; i<listSizesVideo.size(); i++) {
				Log.d(TAG,"SupportedVideoSizes = " + listSizesVideo.get(i).width + " * " + listSizesVideo.get(i).height);
			}
		}
		
		if(mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
}
