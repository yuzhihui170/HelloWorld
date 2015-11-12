package com.example.adv;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.estar.update2.DownAPKServiceNotify;
import com.estar.update2.HttpClient;
import com.estar.update2.XXXActivity;
import com.example.androidtestsrc.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yzh.logUtils.YLog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnGestureListener, SurfaceHolder.Callback {

	protected final static int H_UPDATE_WEATHER = 0x1;
	private int[] imgs = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5};
	private GestureDetector gestureDetector = null;
	private ViewFlipper viewFlipper = null;
	private Activity mActivity = null;
	private SurfaceView mSurfaceView;
	private MediaPlayer mMediaPlayer;
	
	private ImageView mImageViewWeather;
	private WebView webView;
	/**描述天气的对象*/
	private WeatherDes mWeatherDes;
	
	private RelativeLayout mRelativeLayoutWeather;
	private TextView mTextViewCity;
	private TextView mTextViewTemp;
	private TextView mTextViewWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mActivity = this;
		mWeatherDes = new WeatherDes();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getWeather();
	}

	

	@Override
	protected void onPause() {
		super.onPause();
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	private void initView() {
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);

		mSurfaceView.getHolder().addCallback(this);
		
		mRelativeLayoutWeather = (RelativeLayout)findViewById(R.id.relativeLayout_weather);
		mTextViewCity = (TextView) findViewById(R.id.textView_city);
		mTextViewTemp = (TextView) findViewById(R.id.textView_temp);
		mTextViewWeather = (TextView) findViewById(R.id.textView_weather);
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);    //设置JavaScript可用f
        webView.setWebChromeClient(new WebChromeClient());    //处理JavaScript对话框
        webView.setWebViewClient(new WebViewClient());    //处理各种通知和请求事件，如果不使用该句代码，将使用内置浏览器访问网页

		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		gestureDetector = new GestureDetector(this); // 声明检测手势事件

		for (int i = 0; i < imgs.length; i++) { // 添加图片源
			ImageView iv = new ImageView(this);
			iv.setImageResource(imgs[i]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			viewFlipper.addView(iv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}

		viewFlipper.setAutoStart(true); // 设置自动播放功能（点击事件，前自动播放）
		viewFlipper.setFlipInterval(3000);
		if (viewFlipper.isAutoStart() && !viewFlipper.isFlipping()) {
			viewFlipper.startFlipping();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		viewFlipper.stopFlipping(); // 点击事件后，停止自动播放
		viewFlipper.setAutoStart(false);
		return gestureDetector.onTouchEvent(event); // 注册手势事件
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e2.getX() - e1.getX() > 120) { // 从左向右滑动（左进右出）
			Animation rInAnim = AnimationUtils.loadAnimation(mActivity,	R.anim.push_right_in); // 向右滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
			Animation rOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_right_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

			viewFlipper.setInAnimation(rInAnim);
			viewFlipper.setOutAnimation(rOutAnim);
			viewFlipper.showPrevious();
			return true;
		} else if (e2.getX() - e1.getX() < -120) { // 从右向左滑动（右进左出）
			Animation lInAnim = AnimationUtils.loadAnimation(mActivity,	R.anim.push_left_in); // 向左滑动左侧进入的渐变效果（alpha 0.1 -> 1.0）
			Animation lOutAnim = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_out); // 向左滑动右侧滑出的渐变效果（alpha 1.0 -> 0.1）

			viewFlipper.setInAnimation(lInAnim);
			viewFlipper.setOutAnimation(lOutAnim);
			viewFlipper.showNext();
			return true;
		}
		return true;
	}

	private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.start();
			mp.setLooping(true);
		}
	};

	private OnCompletionListener mOnCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
		}
	};

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
		mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
		mMediaPlayer.setDisplay(holder);
//		String PathString = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/VID_20150102_233235.3gp";
//		AssetFileDescriptor fileDescriptor = null;
//		try {
//			fileDescriptor = MainActivity.this.getAssets().openFd("VID_20150102_173126.3gp");
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//
//		File mFile = new File(PathString);
//		Uri uri = Uri.parse("file://" + PathString);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
//			mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
			mMediaPlayer.setDataSource("/sdcard/niceView_2d.mp4");//("/sdcard/movies/niceView_2d.mp4");
		} catch (Exception e) {
			YLog.d(e.toString());
			e.printStackTrace();
		}
		mMediaPlayer.prepareAsync();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case H_UPDATE_WEATHER:
				WeatherDes weatherDes = (WeatherDes)msg.obj;
				mRelativeLayoutWeather.setBackgroundResource(R.drawable.tianqi);
				mTextViewCity.setText("城市: "+weatherDes.getCity());
				mTextViewTemp.setText("温度: "+weatherDes.getTemp());
				mTextViewWeather.setText("天气: "+weatherDes.getWeather());
				break;

			default:
				break;
			}
		}
		
	};
	
	/**获取天气*/
	private void getWeather() {
		HttpClient.checkUpdate(MainActivity.this, callBack);
	}
	
	private RequestCallBack<String> callBack = new RequestCallBack<String>() {
		/**连接 */
		@Override
		public void onStart() {
			super.onStart();
			YLog.d("RequestCallBack --> onStart");
		}
		
		/**加载中 */
		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			super.onLoading(total, current, isUploading);
			YLog.d("RequestCallBack --> onLoading");
		}
		/**加载成功 */
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			YLog.d("RequestCallBack --> onSuccess");
			/**成功返回字符串 ,json*/
			String success = responseInfo.result;//一般为json
			YLog.d("sucess = " + success);
			try {
				JSONObject obj = new JSONObject(success);
				JSONObject jsonObject = obj.getJSONObject("weatherinfo");
				
				String city = jsonObject.getString("city");
				String temp1 = jsonObject.getString("temp1");
				String temp2 = jsonObject.getString("temp2");
				String temp = temp1 + " ~ " + temp2;
				String weather = jsonObject.getString("weather");
				mWeatherDes.setWeatherDes(city, temp, weather);
				mHandler.sendMessage(mHandler.obtainMessage(H_UPDATE_WEATHER, mWeatherDes));
				YLog.d(mWeatherDes.toString());
			} catch (JSONException e) {
				YLog.e(e.toString());
				e.printStackTrace();
			}
		}
		/**加载失败 */
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			YLog.d("RequestCallBack --> onFailure arg1 =" + arg1);
		}
	};
}
