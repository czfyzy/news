package com.zengye.news.base;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ZYBaseAdapter<T> extends BaseAdapter {

	public Context context;
	public List<T> list;
	public int viewResId;
	
	
	
	public ZYBaseAdapter(Context context, List<T> list, int viewResId) {
		super();
		this.context = context;
		this.list = list;
		this.viewResId = viewResId;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
