package com.zengye.news.base;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zengye.news.utils.CommonUtil;

import android.widget.Toast;

public class BaseActivity extends SlidingFragmentActivity {

	public void loadingData(HttpRequest.HttpMethod method, String url, RequestParams params,
            RequestCallBack<String> callBack) {
		HttpUtils  httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		
		if(CommonUtil.isNetworkAvailable(this) == 0) {
			showToast("加载失败，请检查网络！");
		} else {
			httpUtils.send(method, url, params, callBack);
		}
		
	}
	
	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	
	
}
