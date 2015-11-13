package com.forrest.carrecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
	private Camera mCamera;
	private Camera.Parameters mParam;
	private RecorderSurfaceView mRecorderSurfaceView; 
	private Button mBtn_test;        //连续保存测试
	private Button mBtn_stopService; //停止后台录像服务
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		mRecorderSurfaceView = (RecorderSurfaceView)findViewById(R.id.surfaceView1);
		mBtn_test = (Button)findViewById(R.id.btn_test);
		mBtn_stopService = (Button) findViewById(R.id.btn_topService);
		setViewListener();
//		getCameraInfo();
		mkDir();
		startService(new Intent(this, RecorderService.class));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		if(mRecorderSurfaceView != null) {
//			mRecorderSurfaceView.setKeepScreenOn(true); //设置屏幕长亮
//		}
//		String timeStamp = new SimpleDateFormat("yyyy_MM_dd", Locale.CHINA).format(new Date());
//		Log.d(TAG,"timeStamp = " + timeStamp);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void setViewListener() {
		mBtn_test.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				mRecorderSurfaceView.stopRecording();
//				mRecorderSurfaceView.startRecording(mRecorderSurfaceView);
//				Log.d(TAG,"mBtn_test onClick");
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
