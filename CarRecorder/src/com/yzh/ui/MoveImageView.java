package com.yzh.ui;

import com.yzh.logUtils.YLog;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class MoveImageView extends ImageView {
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;

	public MoveImageView(Context context) {
		super(context);
		wm = (WindowManager) getContext().getApplicationContext().getSystemService("window");
		wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.width = 100;
		wmParams.height = 100;
		wmParams.x = 960;
		wmParams.y = 30;
		wmParams.format= PixelFormat.RGBA_8888;
	}
	
	public MoveImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX() - 960;
		y = event.getRawY() - 540; // 25是系统状态栏的高度
		updateViewPosition();
//		YLog.i("currX " + x + "====currY " + y);
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
//			// 获取相对View的坐标，即以此View左上角为原点
//			mTouchStartX = event.getX();
//			mTouchStartY = event.getY();
////			updateViewPosition();
//			YLog.i("1111 startX " + mTouchStartX + "====startY " + mTouchStartY);
//			break;
//
//		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
//			mTouchStartX = event.getX();
//			mTouchStartY = event.getY();
//			updateViewPosition();
//			YLog.i("2222 startX " + mTouchStartX + "====startY " + mTouchStartY);
//			break;
//
//		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
////			updateViewPosition();
////			mTouchStartX = 0;
////			mTouchStartY = 0;
//			YLog.i("3333 startX " + mTouchStartX + "====startY " + mTouchStartY);
//			break;
//		} 
		return true;
	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x);
		wmParams.y = (int) (y);
		YLog.i("++++++++4444 X " + wmParams.x + " Y " + wmParams.y);
		wm.updateViewLayout(this, wmParams); // 刷新显示
	}

}
