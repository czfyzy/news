package com.zengye.news.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zengye.news.R;
import com.zengye.news.base.BaseFragment;
import com.zengye.news.bean.NewSCenterCategories.Child;
import com.zengye.news.pagerIndicator.TabPageIndicator;


public class NewsCenterFragment extends BaseFragment {
	
	private static final String TAG = "NewsCenterFragment";

	@ViewInject(R.id.pager)
	private ViewPager viewPager;
	
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;
	
	private View view;

	private TabPageIndicatorAdapter adapter;
	
//	private List<NewsListFragment> fragments = new ArrayList<NewsListFragment>();
	@Override
	public View initView(LayoutInflater inflater) {
		
		Log.i(TAG, "initView");
	    view = inflater.inflate(R.layout.fragment_news_center, null);
		ViewUtils.inject(this, view);
		
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}
	public void initViewPager(List<Child> children) {
		
		adapter = new TabPageIndicatorAdapter(getFragmentManager(), children);
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
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
		
		Log.i(TAG, "initViewPager");
		
	}
	/** 
     * ViewPager适配器 
	 * @author 
	 * 
	 * 
	 */  
	 class TabPageIndicatorAdapter extends FragmentPagerAdapter {
		private List<Child> chidren;
		
		public TabPageIndicatorAdapter(FragmentManager fm,List<Child> chidren) {
			super(fm);
			// TODO Auto-generated constructor stub
			this.chidren = chidren;
		}

		
		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			Log.i(TAG, "getItem");
			NewsListFragment fragment = new NewsListFragment(chidren.get(position).url,getActivity());;
			
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chidren.size();
		} 
		
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return chidren.get(position).title;
		}
		
	 }

	 
}
