package com.yzh.ui;

import com.forrest.carrecorder.CarRecorderApplication;
import com.forrest.carrecorder.MainActivity;
import com.yzh.Utils.YLog;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class MoveImageView extends ImageView {
	private CarRecorderApplication mCarRecorderApplication;
	private Context mContext;
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	private Point point; 
	private long time = 101;
	private long time0;
	private long time1;

	public MoveImageView(Context context) {
		super(context);
		this.mContext = context;
		mCarRecorderApplication = (CarRecorderApplication) getContext().getApplicationContext();
		wm = (WindowManager) getContext().getApplicationContext().getSystemService("window");
		point = new Point();
	}
	
	public MoveImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	 /*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		wm.getDefaultDisplay().getSize(point);
//		YLog.i("0000 point " + point.x + "  " + point.y);
		x = point.x - event.getRawX();
		y = event.getRawY() - 10;
		
//		YLog.i("0000 currX " + x + "====currY " + y);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
			time0 = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
//			updateViewPosition();
			YLog.d("-----");
			break;
		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			time1 = System.currentTimeMillis();
			time = time1 - time0;
		}
		YLog.i("time = " + time);
		if(time < 100) {
			YLog.d("+++++++");
			time = 101;
			return true;
		}
		updateViewPosition();
		return true;
	}
		private void updateViewPosition() {
		if(null == wmParams) {
			wmParams = mCarRecorderApplication.getLayoutParams();
			YLog.d("wmParams = " + wmParams);
		}
		// 更新浮动窗口位置参数
		wmParams.x = (int)(x); 
		wmParams.y = (int)(y); 
//		YLog.i("++++++++4444 X " + wmParams.x + " Y " + wmParams.y);
		wm.updateViewLayout(this, wmParams); // 刷新显示
	}
	// */
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - 25; // 25是系统状态栏的高度
//		YLog.i("0000currX " + x + "  = currY " + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			time0 = System.currentTimeMillis();
//			YLog.i("1111 startX" + mTouchStartX + "====startY" + mTouchStartY);
			break;
		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
//			YLog.i("2222 startX");
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			updateViewPosition();
			time1 = System.currentTimeMillis();
			mTouchStartX = 0;
			mTouchStartY = 0;
//			YLog.i("3333 startX");
			if(time1 - time0 < 100 && !mCarRecorderApplication.getMainActivityDisplay()) {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				mContext.startActivity(intent);
				YLog.d("--++-- start MainActivity");
			}
			break;
		}
		return true;
	}
	
	private void updateViewPosition() {
		if(null == wmParams) {
			wmParams = mCarRecorderApplication.getLayoutParams();
			YLog.d("wmParams = " + wmParams);
		}
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(this, wmParams); // 刷新显示
	}

}
