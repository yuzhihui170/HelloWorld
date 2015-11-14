package com.forrest.carrecorder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.yzh.ui.MoveImageView;
import com.yzh.ui.MyLinearLayout;

import android.app.ActionBar.LayoutParams;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RecorderService extends Service implements SurfaceHolder.Callback{
	public static final String TAG = "yzh";
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	private LinearLayout mLinearLayout;
	private SurfaceView mSurfaceView; //预览窗口/ 
	private SurfaceHolder mSurfaceHolder;
	private MoveImageView mImageView;
	private Button mBtnChangeWindow;
	private Button mBtnStopService;
	private MediaRecorder mMediaRecorder; //录像的对象
	private String mSaveFileName;
	private int num = 1;
	
	private final Timer timer = new Timer(); 
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	} 
	
	@Override
	public void onCreate() {
		mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
		mLayoutParams.width = 640;//WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height =480;//WindowManager.LayoutParams.WRAP_CONTENT;
		mLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.preview, null);
		
		mSaveFileName = Constant.SAVE_DIRECTORY + "/recorder_" + num + ".3gp"; // /mnt/sdcard/CarRecorder/recorder_1.3gp
		
//		mSurfaceView = (SurfaceView)mLinearLayout.findViewById(R.id.surfaceView);
		mSurfaceView = (SurfaceView)mLinearLayout.findViewById(R.id.surfaceView);
		mBtnChangeWindow = (Button)mLinearLayout.findViewById(R.id.btn_changeWindow);
		mBtnStopService = (Button)mLinearLayout.findViewById(R.id.btn_stop);
		
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this); //添加回调接口
		
//		mWindowManager.addView(mLinearLayout, mLayoutParams); //在悬浮窗中显示预览
		
		mImageView = new MoveImageView(this);
		mImageView.setImageResource(R.drawable.ic_launcher);
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//		mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
		mLayoutParams.x = 960;
		mLayoutParams.y = 30;
		mLayoutParams.width = 100;
		mLayoutParams.height = 100;
		mLayoutParams.format= PixelFormat.RGBA_8888;
		mWindowManager.addView(mImageView, mLayoutParams); //在悬浮窗中显示预览
		
		setViewListener(); //设置监听器
		
		timer.schedule(myTimeTask, 1*60*1000, 1*60*1000);
		Log.d(TAG,"[RecorderService] ------- onCreate -------");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		//mWindowManager.removeView(mLinearLayout);
		mWindowManager.removeView(mImageView);
		Log.d(TAG,"[RecorderService]  ------- onDestroy ------- ");
	}
	
	boolean flag = true;
	public void changeWindow() {
		if(mLayoutParams != null && mWindowManager != null && mLinearLayout != null && flag) {
			mLayoutParams.width = 640;
			mLayoutParams.height =480;
			mWindowManager.updateViewLayout(mLinearLayout, mLayoutParams); //更新参数
//			ViewGroup.LayoutParams lp =   mSurfaceView.getLayoutParams();
//			lp.width = 960;
//			lp.height = 540;
//			mSurfaceView.setLayoutParams(lp);
			flag = false;
		}else {
			mLayoutParams.width = 800;
			mLayoutParams.height =600;
			mWindowManager.updateViewLayout(mLinearLayout, mLayoutParams); //更新参数
			flag = true;
		}
	}
	
	/**给view空间设置监听器*/
	public void setViewListener() {
		if(mBtnChangeWindow != null) {
			mBtnChangeWindow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					changeWindow();
					Log.d(TAG,"mBtnChangeWindow --");
				}
			});
		}
		
		if(mBtnStopService != null) {
			mBtnStopService.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					stopSelf();
					Log.d(TAG,"mBtnStopService --");
				}
			});
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
//		startRecording(mSurfaceView);
		startRecording(holder.getSurface());
		Log.d(TAG,"----[RecorderService]---- surfaceCreated");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		Log.d(TAG,"----[RecorderService]---- surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopRecording();
		Log.d(TAG,"----[RecorderService]---- surfaceDestroyed");
	}
	
	public void startRecording(Surface surface) {
		mMediaRecorder = new MediaRecorder();// 创建mediarecorder对象
		// 设置录制视频源为Camera(相机)
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置录制的视频编码 h264
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
		mMediaRecorder.setVideoSize(640, 480);
		// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//		mMediaRecorder.setVideoFrameRate(20);  //RK3288上使用必须注释掉这条代码,否则预览不能出来
		mMediaRecorder.setPreviewDisplay(surface);
		// 设置视频文件输出的路径
//		mSaveFileName = "/mnt/internal_sd/CarRecoder/recorder.3gp";
		mMediaRecorder.setOutputFile(mSaveFileName);
		Log.d(TAG,"mSaveFileName =" + mSaveFileName);
		try {
			// 准备录制
			mMediaRecorder.prepare();
			// 开始录制
			mMediaRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public void startRecording(SurfaceView surfaceView) {
		mMediaRecorder = new MediaRecorder();// 创建mediarecorder对象
		// 设置录制视频源为Camera(相机)
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		// 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
		mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置录制的视频编码 h264
		mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
		// 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
		mMediaRecorder.setVideoSize(640, 480);
		// 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
		mMediaRecorder.setVideoFrameRate(20);
		mMediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
		// 设置视频文件输出的路径
//		mSaveFileName = "/mnt/internal_sd/CarRecoder/recorder.3gp";
		mMediaRecorder.setOutputFile(mSaveFileName);
		Log.d(TAG,"mSaveFileName =" + mSaveFileName);
		try {
			// 准备录制
			mMediaRecorder.prepare();
			// 开始录制
			mMediaRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**停止录像*/
	public void stopRecording() {
		if (mMediaRecorder != null) {
			// 停止
			mMediaRecorder.stop();
			mMediaRecorder.release();
			mMediaRecorder = null;
		}
	}
	
	//定时任务,先关闭录像,再打开录像,分段保存.
	private TimerTask myTimeTask = new TimerTask() {
		@Override
		public void run() {
			stopRecording(); 
			num++;
			mSaveFileName = Constant.SAVE_DIRECTORY + "/recorder_" + num + ".3gp";
			startRecording(mSurfaceView);
		}
	};

}
