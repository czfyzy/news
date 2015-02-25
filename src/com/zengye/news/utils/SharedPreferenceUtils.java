package com.zengye.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {

	public static final String DEFAULT_FILE = "config";
	public static final String CACHE = "cache";
	/**
	 * 如果没取到值则返回null
	 * @param context
	 * @param spName
	 * @param key
	 * @return
	 */
	public static String getSPString(Context context, String spName, String key) {
		SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		
		return sp.getString(key, null);
	}
	
	public static void saveSPString(Context context, String spName,String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	public static int getInt(Context context, String readModel, int i) {
		// TODO Auto-generated method stub
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return  sp.getInt(readModel, i);
	}

	public static void saveSPString(Context context, String readNewsIds,
			String string) {
		// TODO Auto-generated method stub
		 saveSPString(context, DEFAULT_FILE, readNewsIds, string);
	}

	public static String getSPString(Context context, String key) {
		// TODO Auto-generated method stub
		return getSPString(context, DEFAULT_FILE, key);
	}
}
