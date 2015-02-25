package com.zengye.news.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.zengye.news.R;
import com.zengye.news.base.BaseActivity;
import com.zengye.news.bean.NewSCenterCategories;
import com.zengye.news.bean.NewSCenterCategories.Child;
import com.zengye.news.bean.NewSCenterCategories.NewsData;
import com.zengye.news.fragment.MenuFragment;
import com.zengye.news.fragment.NewsCenterFragment;
import com.zengye.news.fragment.NewsCenterFragment2;
import com.zengye.news.utils.ApiUtils;
import com.zengye.news.utils.GsonUtils;
import com.zengye.news.utils.NewsConstants;
import com.zengye.news.utils.SharedPreferenceUtils;

public class MainActivity extends BaseActivity {

	protected static final String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.fragment_left_menu);
		setContentView(R.layout.activity_main);
		SlidingMenu sm = getSlidingMenu();
		sm.setFadeDegree(0.35f);
		sm.setShadowWidth(R.dimen.sliding_menu_shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.sliding_menu_offset);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setMode(SlidingMenu.LEFT);
		
		MenuFragment menuFragment = new MenuFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_left_menu, menuFragment,"Menu").commit();
		
		Log.i(TAG, "replace");
		NewsCenterFragment2 newsCenterFragment = new NewsCenterFragment2();
		getSupportFragmentManager().beginTransaction().replace(R.id.main_content_frame, newsCenterFragment,"Main").commit();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_launcher);
		
		
		LogUtils.allowI = true;
		LogUtils.i("onCreate");
		
		loadingData(HttpMethod.GET, ApiUtils.NEWS_CENTER_CATEGORIES, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						if (responseInfo.statusCode == 200) {
							SharedPreferenceUtils.saveSPString(
									MainActivity.this,
									NewsConstants.JSON_CACHE_FILE,
									ApiUtils.NEWS_CENTER_CATEGORIES,
									responseInfo.result);
							NewSCenterCategories categories = getCategories(responseInfo.result);
							ArrayList<String> menuList = new ArrayList<String>();
							for (NewsData newsData : categories.data) {
								menuList.add(newsData.title);
							}
							MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag("Menu");
							menuFragment.initMenuList(menuList);
							
							Log.i(TAG, "findFragmentByTag");
							NewsCenterFragment2 newsCenterFragment = (NewsCenterFragment2) getSupportFragmentManager().findFragmentByTag("Main");
							List<Child> children = categories.data.get(0).children;
							children.remove(0);
							newsCenterFragment.initViewPager(children);
						} else {
							showToast("加载失败");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub

					}
				});
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			break;
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private NewSCenterCategories getCategories(String json) {
		return GsonUtils.json2Obj(json, NewSCenterCategories.class);
	}

}
