package com.zengye.news.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

public class GsonUtils {

	public static <T> T json2Obj(String json,Class<T> clazz) {
		if(TextUtils.isEmpty(json)) {
			return null;
		}
		Gson gson = new Gson();
		T t = gson.fromJson(json, clazz);
		return t;
	}
	
	public static <T> String obj2Json(T t) {
		if(t == null) {
			return null;
		}
		Gson gson = new Gson();
		return gson.toJson(t);
	}
}
