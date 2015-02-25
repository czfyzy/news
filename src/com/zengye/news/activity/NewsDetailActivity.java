package com.zengye.news.activity;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zengye.news.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NewsDetailActivity extends SherlockActivity {
	
	@ViewInject(R.id.news_detail_wv)
	private WebView mWebView;
	private WebSettings settings;
	private String url;
	private String title;

	@ViewInject(R.id.loading_view)
	protected View loadingView;
	@ViewInject(R.id.ll_load_fail)
	protected LinearLayout loadfailView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_news_detail);
		ViewUtils.inject(this);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		dealNewsDetail();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	

	


	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);
	}

	private void dealNewsDetail() {
		settings = mWebView.getSettings();
		settings.setUseWideViewPort(true);
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setLoadWithOverviewMode(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				loadurl(view, url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.e("onPageStarted", "");
				showLoadingView();
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				Log.e("onPageFinished", "");
				dismissLoadingView();
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(NewsDetailActivity.this, "加载失败，请检查网络", 0).show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		loadurl(mWebView, url);
	}


	private void showShare() {
		
	}
	
	public void showLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.VISIBLE);
	}

	public void dismissLoadingView() {
		if (loadingView != null)
			loadingView.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		}
		return true;
	}
}
