package com.yzh.ui;

import java.io.Serializable;

import com.forrest.carrecorder.R;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingPreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener,Serializable {
	public static final String EXPOSURE_KEY = "exposure";  //曝光值
	public static final String ISO_KEY = "iso";            //感光度	
	public static final String AWB_KEY = "awb";			   //白平衡
	public static final String PIC_SIZE_KEY = "pictureSize";    //图片大小
	public static final String PIC_NUM_KEY = "pictureNum"; //拍照次数
	private static final String TAG = "yzh";

	private String[] mWhitebalanceKey ; //白平衡key
	private String[] mWhitebalanceValues;//白平衡value
	
	private ListPreference mExposurePref;
	private ListPreference mIsoPref;
	private ListPreference mAwbPref;
	private ListPreference mPicSizePref;
	private ListPreference mPicNumPref;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.set_preferences);
//		findPreference(EXPOSURE_KEY).setOnPreferenceChangeListener(this);
		
//		findPreference(ISO_KEY).setOnPreferenceChangeListener(this);
//		findPreference(AWB_KEY).setOnPreferenceChangeListener(this);
//		findPreference(PIC_SIZE_KEY).setOnPreferenceChangeListener(this);
//		findPreference(PIC_NUM_KEY).setOnPreferenceChangeListener(this);
		mExposurePref = (ListPreference)findPreference(EXPOSURE_KEY);
		mExposurePref.setOnPreferenceChangeListener(this);
		mExposurePref.setSummary("设置拍照曝光值: " + mExposurePref.getEntry());
//		Log.d("yzh","entry = " + mExposurePref.getEntry());
//		Log.d("yzh","value = " + mExposurePref.getValue());
		mIsoPref = (ListPreference)findPreference(ISO_KEY);
		mIsoPref.setOnPreferenceChangeListener(this);
		mIsoPref.setSummary("设置感光度值: " + mIsoPref.getEntry());
		
		mAwbPref = (ListPreference)findPreference(AWB_KEY);
		mAwbPref.setOnPreferenceChangeListener(this);
		mAwbPref.setSummary("设置白平衡: " + mAwbPref.getEntry());
		
		mPicSizePref = (ListPreference)findPreference(PIC_SIZE_KEY);
		mPicSizePref.setOnPreferenceChangeListener(this);
		mPicSizePref.setSummary("设置照片大小: " + mPicSizePref.getEntry());

		mPicNumPref = (ListPreference)findPreference(PIC_NUM_KEY);
		mPicNumPref.setOnPreferenceChangeListener(this);
		mPicNumPref.setSummary("设置拍照次数: " + mPicNumPref.getEntry());
		
		mWhitebalanceKey = getResources().getStringArray(R.array.whitebalance_entries);
		mWhitebalanceValues = getResources().getStringArray(R.array.whitebalance_entries_value);
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Log.d("yzh","key = " + preference.getKey());
		String summary = "";
		ListPreference lp = (ListPreference)preference;
//		Log.d("yzh","ListPreference = " + lp.getEntry());
		if(preference.getKey().equals(EXPOSURE_KEY)) {
			summary = "设置拍照曝光值: " + (String)newValue;
			preference.setSummary(summary);
			return true;
		}else if(preference.getKey().equals(ISO_KEY)) { //感光值
			summary = "设置感光度值: " + (String)newValue;
			preference.setSummary(summary);
			return true;
		}else if(preference.getKey().equals(AWB_KEY)) { //不能使用getEntry,这个时候Entry值还没有变化
			summary = "设置白平衡: " + findValue((String)newValue, mWhitebalanceKey, mWhitebalanceValues);
			preference.setSummary(summary);
			return true;
		}else if(preference.getKey().equals(PIC_SIZE_KEY)) {
			summary = "设置照片大小: " + (String)newValue;
			preference.setSummary(summary);
			return true;
		}else if(preference.getKey().equals(PIC_NUM_KEY)) {
			summary = "设置拍照次数: " + (String)newValue;
			preference.setSummary(summary);
			return true;
		}
		return false;
	}
	
	private String findValue(String value, String[] keys, String[] values) {
		int index = -1;
		if(value==null || keys ==null || values == null) {
			return null;
		}
		for(int i=0;i<values.length;i++) {
			if(values[i].equals(value)) {
				index = i;
				break;
			}
		}
		if(index >= 0 && index < keys.length) {
			return keys[index];
		}
		return null;
	}
	
}
