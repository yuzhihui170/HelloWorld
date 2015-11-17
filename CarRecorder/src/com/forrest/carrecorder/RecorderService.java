package com.forrest.carrecorder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.yzh.Utils.DataUtil;
import com.yzh.Utils.MemoryUtil;
import com.yzh.Utils.YLog;
import com.yzh.ui.MoveImageView;
import com.yzh.ui.MyLinearLayout;

import android.app.ActionBar.LayoutParams;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Handler;
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
import android.widget.TextView;

public class RecorderService extends Service implements SurfaceHolder.Callback{
	public static final String TAG = "yzh";
	public static final String ACTION_NAME = "com.forrest.carrecorder.RecorderService";
	private CarRecorderApplication mCarRecorderApplication;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mSurfaceLayoutParams;
	private WindowManager.LayoutParams mLayoutParams;
	
	private LinearLayout mLinearLayout;
	private SurfaceView mSurfaceView; //预览窗口/ 
	private SurfaceHolder mSurfaceHolder;
	private MoveImageView mImageView;
	private Button mBtnChangeWindow;
	private Button mBtnStopService;
	private TextView mTextViewTime;
	
	private MediaRecorder mMediaRecorder; //录像的对象
	private String mSaveFileName;
	private int num = 1;
	
	private final Timer timer = new Timer();
	
	private boolean display = true;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	} 
	
	@Override
	public void onCreate() {
		mCarRecorderApplication = (CarRecorderApplication) getApplication();
		mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		mSurfaceLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams = new WindowManager.LayoutParams();
		mSurfaceLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mSurfaceLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mSurfaceLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
		mSurfaceLayoutParams.x = 0;
		mSurfaceLayoutParams.y = 140;
		mSurfaceLayoutParams.width = 800;
		mSurfaceLayoutParams.height =600;
		mLinearLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.preview, null);
		
		mSaveFileName = Constant.SAVE_DIRECTORY + "/video_" + DataUtil.getTime() + ".3gp"; // /mnt/sdcard/CarRecorder/recorder_1.3gp
		
		mSurfaceView = (SurfaceView)mLinearLayout.findViewById(R.id.surfaceView);
		mSurfaceView = (SurfaceView)mLinearLayout.findViewById(R.id.surfaceView);
		mTextViewTime = (TextView) mLinearLayout.findViewById(R.id.textview_time);
		mBtnChangeWindow = (Button)mLinearLayout.findViewById(R.id.btn_changeWindow);
		mBtnStopService = (Button)mLinearLayout.findViewById(R.id.btn_stop);
		
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this); //添加回调接口
		
		mWindowManager.addView(mLinearLayout, mSurfaceLayoutParams); //在悬浮窗中显示预览
		
		mImageView = new MoveImageView(this);
		mImageView.setImageResource(R.drawable.record);
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
		mLayoutParams.x = 960;
		mLayoutParams.y = 25;
		mLayoutParams.width = 200;
		mLayoutParams.height = 200;
		mLayoutParams.format= PixelFormat.RGBA_8888;
		mCarRecorderApplication.setLayoutParams(mLayoutParams);
		mWindowManager.addView(mImageView, mLayoutParams); //在悬浮窗中显示预览
		
		setViewListener(); //设置监听器
		
		registerBoradcastReceiver();
		
//		timer.schedule(myTimeTask, 10*60*1000, 10*60*1000); TIME_SPAN
		timer.schedule(myTimeTask, Constant.TIME_SPAN*60*1000, Constant.TIME_SPAN*60*1000);
		Log.d(TAG,"[RecorderService] ------- onCreate -------");
		
		MemoryUtil mMemoryUtil = new MemoryUtil(this);
		mMemoryUtil.getMemoryInfo();
//		YLog.d("data : " + DataUtil.getTime());
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		mWindowManager.removeView(mLinearLayout);
		mWindowManager.removeView(mImageView);
		unregisterReceiver(mBroadcastReceiver);
		Log.d(TAG,"[RecorderService]  ------- onDestroy ------- ");
	}
	
	boolean flag = true;
	public void changeWindow(boolean display) {
		if(mLayoutParams != null && mWindowManager != null && mLinearLayout != null) {
			if(display) {
				mSurfaceLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
				mSurfaceLayoutParams.x = 0;
				mSurfaceLayoutParams.y = 140;
				mSurfaceLayoutParams.width = 800;
				mSurfaceLayoutParams.height =600;
				mWindowManager.updateViewLayout(mLinearLayout, mSurfaceLayoutParams); //更新参数
				
				mLayoutParams.x = 960;
				mLayoutParams.y = 25;
				mWindowManager.updateViewLayout(mImageView, mLayoutParams); //更新参数
			}else {
				mSurfaceLayoutParams.gravity = Gravity.RIGHT | Gravity.TOP;
				mSurfaceLayoutParams.x = 0;
				mSurfaceLayoutParams.y = 700;
				mSurfaceLayoutParams.width = 50;
				mSurfaceLayoutParams.height =50;
				mWindowManager.updateViewLayout(mLinearLayout, mSurfaceLayoutParams); //更新参数
				
				mLayoutParams.x = 540;
				mLayoutParams.y = 25;
				mWindowManager.updateViewLayout(mImageView, mLayoutParams); //更新参数
			}
		}
	}
	
	/**给view空间设置监听器*/
	public void setViewListener() {
		if(mBtnChangeWindow != null) {
			mBtnChangeWindow.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					changeWindow(true);
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
			mHandler.post(mRunnable);
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
			time = 0;
		}
	}
	
	//定时任务,先关闭录像,再打开录像,分段保存.
	private TimerTask myTimeTask = new TimerTask() {
		@Override
		public void run() {
			stopRecording(); 
			num++;
			mSaveFileName = Constant.SAVE_DIRECTORY + "/video" + DataUtil.getTime() + ".3gp";
			startRecording(mSurfaceView);
		}
	};
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();
            
            if(action.equals(ACTION_NAME)){
            	display = intent.getBooleanExtra("display", true);
            	changeWindow(display);
            	YLog.d("receiver BroadcastReceiver");
            }  
        } 
	};
	
	private void registerBoradcastReceiver(){  
        IntentFilter myIntentFilter = new IntentFilter();  
        myIntentFilter.addAction(ACTION_NAME);  
        //注册广播        
        registerReceiver(mBroadcastReceiver, myIntentFilter);  
    }  
	
	private Handler mHandler = new Handler();
	private int time = 0;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mTextViewTime.setText(DataUtil.formatTime(time, false));
			time = time + 1000;
			mHandler.postDelayed(mRunnable, 1000);
		}
	};

}
