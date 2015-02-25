package com.zengye.news.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zengye.news.R;
import com.zengye.news.base.BaseFragment;
import com.zengye.news.bean.NewSCenterCategories.Child;
import com.zengye.news.pagerIndicator.TabPageIndicator;
import com.zengye.news.view.NewsListPage;
import com.zengye.news.view.NewsListPage.CompletedCallBack;


public class NewsCenterFragment2 extends BaseFragment {
	
	private static final String TAG = "NewsCenterFragment";

	@ViewInject(R.id.pager)
	private ViewPager viewPager;
	
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;
	
	private View view;

	private TabPageIndicatorAdapter adapter;
	
	private List<NewsListPage> pages = new ArrayList<NewsListPage>();
	
	private CompletedCallBack callBack;
	private LinearLayout loading;
	@Override
	public View initView(LayoutInflater inflater) {
		
		Log.i(TAG, "initView");
	    view = inflater.inflate(R.layout.fragment_news_center, null);
	    loading = (LinearLayout) view.findViewById(R.id.loading);
		ViewUtils.inject(this, view);
		loading.setVisibility(View.VISIBLE);
		callBack = new CompletedCallBack() {
			
			@Override
			public void completed() {
				// TODO Auto-generated method stub
				loading.setVisibility(View.INVISIBLE);
			}
		};
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}
	public void initViewPager(List<Child> children) {
		pages.clear();
		for (Child child : children) {
			NewsListPage page = new NewsListPage(child.url, context,callBack);
			pages.add(page);
		}
		adapter = new TabPageIndicatorAdapter(children,pages);
		viewPager.setAdapter(adapter);
		indicator.setVisibility(View.VISIBLE);
		
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageSelected(int arg0) {
				if(arg0==0){
					sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				}else{
					sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
				
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		indicator.setViewPager(viewPager);
		Log.i(TAG, "initViewPager");
		pages.get(0).initData();
	}
	/** 
     * ViewPager适配器 
	 * @author 
	 * 
	 * 
	 */  
	 class TabPageIndicatorAdapter extends PagerAdapter {
		private List<Child> children;
		private List<NewsListPage> pages;
		public TabPageIndicatorAdapter(List<Child> children, List<NewsListPage> pages) {
			 
			super();
			this.children = children;
			this.pages = pages;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return children.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			Log.i(TAG, "destroyItem");
			if(position>=pages.size()) return;
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			Log.i(TAG, "instantiateItem");
			View view = pages.get(position).getContentView();
			container.addView(view,0);
			if(!pages.get(position).isInitData) {
				pages.get(position).initData();
			}
			return view;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return children.get(position).title;
		}
		
	 }
}
