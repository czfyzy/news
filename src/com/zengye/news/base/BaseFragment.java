package com.zengye.news.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zengye.news.activity.MainActivity;
import com.zengye.news.utils.CommonUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class  BaseFragment extends Fragment {

	protected Context context;
	protected SlidingMenu sm;
	protected View rootView;
	private static final String TAG = "BaseFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		if(context == null) {
			context = getActivity();
		}
		initData(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreateView");

		return initView(inflater);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub\
		Log.i(TAG, "onActivityCreated");

		super.onActivityCreated(savedInstanceState);
		if(getActivity() instanceof MainActivity) {
			sm = ((MainActivity)getActivity()).getSlidingMenu();
		}

	}

	/**
	 * 初始化界面
	 * @param inflater
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);
	
	/**
	 * 初始化数据
	 * @param savedInstanceState
	 * @return
	 */
	public abstract void initData(Bundle savedInstanceState);
	
	
	public void loadingData(HttpRequest.HttpMethod method, String url, RequestParams params,
            RequestCallBack<String> callBack) {
		HttpUtils  httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		
		if(CommonUtil.isNetworkAvailable(context) == 0) {
			showToast("加载失败，请检查网络！");
		} else {
			httpUtils.send(method, url, params, callBack);
		}
		
	}
	
	public void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
