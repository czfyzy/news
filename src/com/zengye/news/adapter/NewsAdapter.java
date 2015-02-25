package com.zengye.news.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapDecoder;
import com.zengye.news.R;
import com.zengye.news.base.ZYBaseAdapter;
import com.zengye.news.bean.NewsListBean.News;
import com.zengye.news.utils.CommonUtil;
import com.zengye.news.utils.NewsConstants;
import com.zengye.news.utils.SharedPreferenceUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends ZYBaseAdapter<News> {
	 

	private int type;
	private BitmapUtils bitmapUtils;
	
	public NewsAdapter(Context context, List<News> list, int viewResId, int type) {
		super(context, list, viewResId);
		this.type = type;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		News news = list.get(position);
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, viewResId, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.pub_date = (TextView) convertView
					.findViewById(R.id.tv_pub_date);
			holder.comment_count = (TextView) convertView
					.findViewById(R.id.tv_comment_count);
			convertView.setTag(holder);
		} else { 
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(news.title);
		holder.pub_date.setText(news.pubdate);
		if (news.comment) {
			holder.comment_count.setVisibility(View.VISIBLE);
			if (news.commentcount > 0) {
				holder.comment_count.setText(news.commentcount + "");
			}else{
				holder.comment_count.setText("");
			}
			
		} else {
			holder.comment_count.setVisibility(View.INVISIBLE);
		}
		if(news.isRead){
			holder.title.setTextColor(context.getResources().getColor(R.color.news_item_has_read_textcolor));
		}else{
			holder.title.setTextColor(context.getResources().getColor(R.color.news_item_no_read_textcolor));
		}
		if (type == 0) {
			if (TextUtils.isEmpty(news.listimage)) {
				holder.iv.setVisibility(View.GONE);
			} else {
				SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(context);
				int read_model = Integer.valueOf(pre.getString(NewsConstants.READ_MODEL, "2"));
				switch (read_model) {
				case 2:
					int type = CommonUtil.isNetworkAvailable(context);
					if(type==1){
						holder.iv.setVisibility(View.VISIBLE);
						bitmapUtils.display(holder.iv, news.listimage);
					}else{
						holder.iv.setVisibility(View.GONE);
					}
					break;
				case 0:
					holder.iv.setVisibility(View.VISIBLE);
					bitmapUtils.display(holder.iv, news.listimage);
					break;
				case 1:
					holder.iv.setVisibility(View.GONE);
					break;

				default:
					break;
				}
				
				
			}
		} else {
			holder.iv.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	class ViewHolder {
		ImageView iv;
		TextView title;
		TextView pub_date;
		TextView comment_count;
	}

}
