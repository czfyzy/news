package com.zengye.news.activity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.zengye.news.R;
import com.zengye.news.fragment.SettingFragment;

import android.os.Bundle;

public class SettingActivity extends SherlockFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		SettingFragment fragment = new SettingFragment();
		getFragmentManager().beginTransaction().replace(R.id.setting_content, fragment).commit();
	}
	
}
