package com.estar.update2;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class XXXActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

//		findViewById(R.id.checkUpdate).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						HttpClient.checkUpdate(MainActivity.this, callBack);
//					}
//				});
	}

	private RequestCallBack<String> callBack = new RequestCallBack<String>() {
		/**
		 * 连接
		 */
		@Override
		public void onStart() {
			super.onStart();
		}
		
		/**
		 * 加载中
		 */
		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			super.onLoading(total, current, isUploading);
		}
		/**
		 * 加载成功
		 */
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			/**
			 * 成功返回字符串 ,json
			 */
			String success = responseInfo.result;//一般为json
			try {
				JSONObject obj = new JSONObject(success);

				JSONObject jsonObject = obj.getJSONObject("obj");

				long fileSize = jsonObject.getLong("fileSize");
				String verCode = jsonObject.getString("verCode");
				String verName = jsonObject.getString("verName");
				String publishDate = jsonObject.getString("publishDate");
				String alertTitle = jsonObject.getString("alertTitle");
				String alertContent = jsonObject.getString("alertContent");
				int hasUpdate = jsonObject.getInt("hasUpdate");
				int needForcedUpdate = jsonObject.getInt("needForcedUpdate");
				String downloadUrl = jsonObject.getString("downloadUrl");
				
				Log.d("yzh","-file = -" + downloadUrl);
				/**
				 * 处理..............
				 */
				
			
				 Intent intent = new Intent(XXXActivity.this, DownAPKServiceNotify.class);
				 intent.putExtra("APK_url", downloadUrl);
				 intent.putExtra("id", 1);//序列id,随便填一个数字
				 startService(intent);
				 Log.d("yzh","=========");
				
				
				/**
				 * ②
				 * 跳出升级对话框
				 */
				
			} catch (JSONException e) {
				Log.e("yzh",e.toString());
				e.printStackTrace();
			}
		}
		/**
		 * 加载失败
		 */
		@Override
		public void onFailure(HttpException arg0, String arg1) {

		}
	};

}
