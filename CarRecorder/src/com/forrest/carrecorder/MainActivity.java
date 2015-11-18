package com.forrest.carrecorder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.yzh.Utils.FileUtils;
import com.yzh.Utils.MemoryUtil;
import com.yzh.ui.CustomerDialog;
import com.yzh.ui.SettingPreferenceFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	public static final String TAG = "yzh";
	private CarRecorderApplication mCarRecorderApplication;
	private Camera mCamera;
	private Camera.Parameters mParam;
	private RecorderSurfaceView mRecorderSurfaceView;
	private Button mBtnSetting; //设置按钮
	private Button mBtnBack;        //后台录像
	private Button mBtn_stopService; //停止后台录像服务
	private View mDialogView;
	private Dialog mDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mCarRecorderApplication = (CarRecorderApplication) getApplication();
//		mRecorderSurfaceView = (RecorderSurfaceView)findViewById(R.id.surfaceView1);
		mBtnSetting = (Button) findViewById(R.id.btn_setting);
		mBtnBack = (Button)findViewById(R.id.btn_back);
		mBtn_stopService = (Button) findViewById(R.id.btn_topService);
		setViewListener();
//		getCameraInfo();
		mkDir();
		startService(new Intent(this, RecorderService.class));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mCarRecorderApplication.setMainActivityDisplay(true);
		Intent mIntent = new Intent(RecorderService.ACTION_NAME);
		mIntent.putExtra("display", true);
		sendBroadcast(mIntent);
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
	
	@Override
	protected void onStop() {
		super.onStop();
		mCarRecorderApplication.setMainActivityDisplay(false);
		Intent mIntent = new Intent(RecorderService.ACTION_NAME);
		mIntent.putExtra("display", false);
		sendBroadcast(mIntent);
	}
	
	/**设置UI的监听器*/
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
		mBtnSetting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createSettingDialog();
			}
		});
	}
	
	private void createSettingDialog() {
//		if(mDialogView == null) {
//			mDialogView = LayoutInflater.from(this).inflate(R.layout.setting_dialog, null);
//		}
//		if(mDialog == null) {
//			mDialog = new Dialog(this);
//			mDialog.setContentView(mDialogView);
//			Window windown = mDialog.getWindow();
//			WindowManager.LayoutParams lp = windown.getAttributes();
//			windown.setGravity(Gravity.LEFT | Gravity.TOP);
//			lp.x = 300;
//			lp.y = 100;
//			lp.width = 800;
//			lp.height = 800;
//			lp.alpha = 0.7f;  
//			windown.setAttributes(lp);
//		}
//		mDialog.show();
		CustomerDialog.Builder builder = new CustomerDialog.Builder(this);
		builder.setMessage("这个就是自定义的提示框");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				//设置你的操作事项
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
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
