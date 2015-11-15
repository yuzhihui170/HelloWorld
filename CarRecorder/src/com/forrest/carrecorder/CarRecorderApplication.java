package com.forrest.carrecorder;

import android.app.Application;
import android.view.WindowManager.LayoutParams;

public class CarRecorderApplication extends Application{
	/**可以随着手指移动的ImageView的布局参数*/
	private LayoutParams mMoveImageViewLayoutParamws;
	private boolean mMainActivityDisplay;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	/**设置可以随手指移动的ImageView的布局参数*/
	public void setLayoutParams(LayoutParams lp) {
		this.mMoveImageViewLayoutParamws = lp;
	}
	
	public LayoutParams getLayoutParams() {
		return this.mMoveImageViewLayoutParamws;
	}
	/**设置MainActivity是否显示了*/
	public void setMainActivityDisplay(boolean display) {
		this.mMainActivityDisplay = display;
	}
	
	public boolean getMainActivityDisplay() {
		return this.mMainActivityDisplay;
	}
}
