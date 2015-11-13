package com.forrest.carrecorder;

import com.yzh.logUtils.YLog;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class RecorderSurfaceView extends SurfaceView {

	private static final String TAG = "yzh";
	private float mTouchStartX;
	private float mTouchStartY;
	private float x;
	private float y;
	private WindowManager wm;
	private WindowManager.LayoutParams wmParams;
	private LinearLayout mLinearLayout;	

	public RecorderSurfaceView(Context context) {
		super(context);
	}

	public RecorderSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		wm = (WindowManager) getContext().getApplicationContext().getSystemService("window");
		wmParams = new WindowManager.LayoutParams();
	}
	
	public void setLayout(LinearLayout linearLayout) {
		this.mLinearLayout = linearLayout;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取相对屏幕的坐标，即以屏幕左上角为原点
		x = event.getRawX();
		y = event.getRawY() - 25; // 25是系统状态栏的高度
		Log.i("currP", "currX" + x + "====currY" + y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
			// 获取相对View的坐标，即以此View左上角为原点
			mTouchStartX = event.getX();
			mTouchStartY = event.getY();
			Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
			break;
		case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
			updateViewPosition();
			break;
		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			updateViewPosition();
			mTouchStartX = mTouchStartY = 0;
			break;
		}
		return true;
	}

	private void updateViewPosition() {
		// 更新浮动窗口位置参数
		wmParams.x = (int) (x - mTouchStartX);
		wmParams.y = (int) (y - mTouchStartY);
		wm.updateViewLayout(mLinearLayout, wmParams); // 刷新显示
	}

}
