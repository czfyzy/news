package com.zengye.news.fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import com.zengye.news.R;
import com.zengye.news.activity.NewsDetailActivity;
import com.zengye.news.adapter.NewsAdapter;
import com.zengye.news.base.BaseFragment;
import com.zengye.news.bean.CountList;
import com.zengye.news.bean.NewsListBean;
import com.zengye.news.bean.NewsListBean.News;
import com.zengye.news.bean.NewsListBean.TopNews;
import com.zengye.news.pullRefreshView.PullToRefreshBase;
import com.zengye.news.pullRefreshView.PullToRefreshBase.OnRefreshListener;
import com.zengye.news.pullRefreshView.PullToRefreshListView;
import com.zengye.news.utils.CommonUtil;
import com.zengye.news.utils.GsonUtils;
import com.zengye.news.utils.NewsConstants;
import com.zengye.news.utils.SharedPreferenceUtils;
import com.zengye.news.view.RollViewPager;
import com.zengye.news.view.RollViewPager.OnPagerClickCallback;

public class NewsListFragment extends BaseFragment {

	private View topNewsView;

//	@ViewInject(R.id.lv_item_news)
	private PullToRefreshListView ptrLv;

//	@ViewInject(R.id.ll_top_news_roll)
	private LinearLayout topNewsViewpager;

//	@ViewInject(R.id.dots_ll)
	private LinearLayout dotLl;

//	@ViewInject(R.id.top_news_title)
	private TextView topNewsTitle;

//	@ViewInject(R.id.loading_view)
	protected View loadingView;

	private boolean isLoadSuccess;

	private String countCommentUrl;

	private ArrayList<TopNews> topNewsList;

	private ArrayList<String> urlList;

	private ArrayList<String> titleList;

	private ArrayList<View> dotList;

	private RollViewPager viewPager;

	private String moreUrl;

	private String readedIds;

	private String url;

	private ArrayList<News> newsList = new ArrayList<NewsListBean.News>();

	private NewsAdapter adapter;

	public NewsListFragment(String url, Context context) {
		super();
		this.context = context;
		this.url = url;
	}

	private HashSet<String> readedSet = new HashSet<String>();

	@Override
	public View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_news_list, null);
		topNewsView = inflater.inflate(R.layout.layout_top_news_view, null);
		ptrLv = (PullToRefreshListView) view.findViewById(R.id.lv_item_news);

		dotLl = (LinearLayout) topNewsView.findViewById(R.id.dots_ll);

		topNewsViewpager = (LinearLayout) topNewsView
				.findViewById(R.id.ll_top_news_roll);

		topNewsTitle = (TextView) topNewsView.findViewById(R.id.top_news_title);

		loadingView = view.findViewById(R.id.loading_view);

//		ViewUtils.inject(view);
//		ViewUtils.inject(topNewsView);
		// 设置禁用上拉刷新
		ptrLv.setPullLoadEnabled(false);
		// 设置滚动到底自动加载
		ptrLv.setScrollLoadEnabled(true);

		ptrLv.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context,
								NewsDetailActivity.class);
						String url = "";
						String title;
						News newsItem;
						if (ptrLv.getRefreshableView().getHeaderViewsCount() > 0) {
							newsItem = newsList.get(position - 1);
						} else {
							newsItem = newsList.get(position);
						}
						url = newsItem.url;
						if (!newsItem.isRead) {
							readedSet.add(newsItem.id);
							newsItem.isRead = true;
							SharedPreferenceUtils.saveSPString(context,
									NewsConstants.READ_NEWS_IDS, readedIds
											+ "," + newsItem.id);
						}

						title = newsItem.title;
						intent.putExtra("url", url);
						intent.putExtra("title", title);
						context.startActivity(intent);
					}
				});

		ptrLv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getNewsList(url, true);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				getNewsList(moreUrl, false);
			}
		});
		initFragment();
		return view;
	}

	@Override
	public void initData(Bundle savedInstanceState) {

	}

	public void initFragment() {
		// TODO Auto-generated method stub
		readedIds = SharedPreferenceUtils.getSPString(context, "READED_IDS");
		if (readedIds != null) {
			String[] ids = readedIds.split(",");
			for (String id : ids) {

				readedSet.add(id);
			}
		}

		if (!TextUtils.isEmpty(url)) {
			String result = SharedPreferenceUtils.getSPString(context,
					SharedPreferenceUtils.CACHE, url);
			if (!TextUtils.isEmpty(result)) {
				processDataFromCache(true, result);
			}

			getNewsList(url, true);
		}
	}

	private void setLastUpdateTime() {
		String text = CommonUtil.getStringDate();
		ptrLv.setLastUpdatedLabel(text);
	}

	private void getNewsList(final String loadUrl, final boolean isRefresh) {
		// TODO Auto-generated method stub
		loadingData(HttpMethod.GET, loadUrl, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						if (isRefresh) {
							SharedPreferenceUtils.saveSPString(context,
									SharedPreferenceUtils.CACHE, loadUrl,
									responseInfo.result);
						}

						processData(isRefresh, responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						onLoaded();
					}
				});
	}

	protected void processData(boolean isRefresh, String result) {
		// TODO Auto-generated method stub
		NewsListBean bean = GsonUtils.json2Obj(result, NewsListBean.class);
		if (bean.retcode == 200) {
			isLoadSuccess = true;
			countCommentUrl = bean.data.countcommenturl;
			if (isRefresh) {
				topNewsList = bean.data.topnews;

				if (topNewsList != null && topNewsList.size() > 0) {
					titleList = new ArrayList<String>();
					urlList = new ArrayList<String>();
					for (TopNews topNews : topNewsList) {
						titleList.add(topNews.title);
						urlList.add(topNews.topimage);
					}
					initTopNewsViewPager();
				}
			}

			moreUrl = bean.data.more;
			if (bean.data.news != null) {
				getNewsCommentCount(bean.data.countcommenturl, bean.data.news,
						isRefresh);
			}
		}
	}

	private void getNewsCommentCount(String countcommenturl,
			final ArrayList<News> tempNewsList, final boolean isRefresh) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder(countcommenturl);
		for (News news : tempNewsList) {
			sb.append(news.id + ",");
		}

		loadingData(HttpMethod.GET, sb.toString(), null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						CountList countList = GsonUtils.json2Obj(
								responseInfo.result, CountList.class);
						for (News news : tempNewsList) {
							news.commentcount = countList.data
									.get(news.id + "");
							if (readedSet.contains(news.id)) {
								news.isRead = true;
							} else {
								news.isRead = false;
							}
						}

						if (isRefresh) {
							newsList.clear();
						}
						newsList.addAll(tempNewsList);

						if (adapter == null) {
							adapter = new NewsAdapter(context, newsList,
									R.layout.layout_news_item, 0);
							ptrLv.getRefreshableView().setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						onLoaded();
						if (TextUtils.isEmpty(moreUrl)) {
							ptrLv.setHasMoreData(false);
						} else {
							ptrLv.setHasMoreData(true);
						}
						setLastUpdateTime();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						onLoaded();
					}
				});
	}

	private void processDataFromCache(boolean isRefresh, String result) {
		// TODO Auto-generated method stub
		NewsListBean bean = GsonUtils.json2Obj(result, NewsListBean.class);
		if (bean.retcode == 200) {
			isLoadSuccess = true;
			countCommentUrl = bean.data.countcommenturl;
			if (isRefresh) {
				topNewsList = bean.data.topnews;

				if (topNewsList != null && topNewsList.size() > 0) {
					titleList = new ArrayList<String>();
					urlList = new ArrayList<String>();
					for (TopNews topNews : topNewsList) {
						titleList.add(topNews.title);
						urlList.add(topNews.topimage);
					}
					initTopNewsViewPager();
				}
			}

			moreUrl = bean.data.more;
			if (bean.data.news != null) {
				if (!isRefresh) {
					newsList.clear();
				}
				newsList.addAll(bean.data.news);
			}

			for (News newsItem : newsList) {
				if (readedSet.contains(newsItem.id)) {
					newsItem.isRead = true;
				} else {
					newsItem.isRead = false;
				}

			}

			if (adapter == null) {
				adapter = new NewsAdapter(context, newsList,
						R.layout.layout_news_item, 0);
				ptrLv.getRefreshableView().setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}
			onLoaded();
			LogUtils.d("moreUrl---" + moreUrl);
			if (TextUtils.isEmpty(moreUrl)) {
				ptrLv.setHasMoreData(false);
			} else {
				ptrLv.setHasMoreData(true);
			}
			setLastUpdateTime();
		}
	}

	private void initTopNewsViewPager() {
		initDot(topNewsList.size());
//		viewPager = new RollViewPager(context, dotList, topNewsTitle,
//				new OnPagerClickCallback() {
//
//					@Override
//					public void onPagerClick(int position) {
//						// TODO Auto-generated method stub
//						TopNews news = topNewsList.get(position);
//						if (news.type.equals("news")) {
//							Intent intent = new Intent(context,
//									NewsDetailActivity.class);
//							String url = news.url;
//							boolean comment = news.comment;
//							String commentUrl = news.commenturl;
//							String title = news.title;
//							String newsId = news.id;
//							String imgUrl = news.topimage;
//							String commentListUrl = news.commentlist;
//							intent.putExtra("url", url);
//							intent.putExtra("commentUrl", commentUrl);
//							intent.putExtra("newsId", newsId);
//							intent.putExtra("imgUrl", imgUrl);
//							intent.putExtra("title", title);
//							intent.putExtra("comment", comment);
//							intent.putExtra("countCommentUrl", countCommentUrl);
//							intent.putExtra("commentListUrl", commentListUrl);
//							context.startActivity(intent);
//						} else if (news.type.equals("topic")) {
//
//						}
//					}
//				});
//
//		viewPager.setTitles(titleList);
		viewPager.setUriList(urlList);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
//		viewPager.startRoll();
		topNewsViewpager.removeAllViews();
		topNewsViewpager.addView(viewPager);
		if (ptrLv.getRefreshableView().getHeaderViewsCount() < 1) {
			ptrLv.getRefreshableView().addHeaderView(topNewsView);
		}
	}

	private void initDot(int length) {
		dotList = new ArrayList<View>();
		dotLl.removeAllViews();
		int i = 0;
		while (i < length) {
			View dot = new View(context);
			LinearLayout.LayoutParams params = new LayoutParams(
					CommonUtil.dip2px(context, 6),
					CommonUtil.dip2px(context, 6));
			params.setMargins(5, 0, 5, 0);
			dot.setLayoutParams(params);
			dot.setBackgroundResource(R.drawable.dot_selector_bg);
			if (i == 0) {
				dot.setEnabled(false);
			} else {
				dot.setEnabled(true);
			}
			dotLl.addView(dot);
			dotList.add(dot);
			i++;
		}
	}

	private void onLoaded() {
		dismissLoadingView();
		ptrLv.onPullDownRefreshComplete();
		ptrLv.onPullUpRefreshComplete();
	}

	private void dismissLoadingView() {
		// TODO Auto-generated method stub
		if (loadingView != null)
			loadingView.setVisibility(View.INVISIBLE);
	}
}
