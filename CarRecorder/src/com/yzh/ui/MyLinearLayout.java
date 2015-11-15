package com.yzh.ui;

import com.yzh.Utils.YLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout{
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;

	public MyLinearLayout(Context context) {
		super(context);
	}
	
	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		wm = (WindowManager) getContext().getApplicationContext().getSystemService("window");
		wmParams = new WindowManager.LayoutParams();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - 25; // 25是系统状态栏的高度
		YLog.i("currX " + x + "  = currY " + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			YLog.i("startX" + mTouchStartX + "====startY" + mTouchStartY);
			break;
		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			updateViewPosition();
			mTouchStartX = 0;
			mTouchStartY = 0;
			break;
		}
		return true;
	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(this, wmParams); // 刷新显示
	}

}
