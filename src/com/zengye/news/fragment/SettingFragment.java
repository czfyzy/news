package com.zengye.news.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.zengye.news.R;

public class SettingFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
//		Preference preference = findPreference(NewsConstants.READ_MODEL);
//		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//			
//			@Override
//			public boolean onPreferenceChange(Preference preference, Object newValue) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
	}
	
}
