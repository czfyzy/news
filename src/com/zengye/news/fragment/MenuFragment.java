package com.zengye.news.fragment;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.R.menu;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.zengye.news.R;
import com.zengye.news.base.BaseFragment;
import com.zengye.news.base.ZYBaseAdapter;

public class MenuFragment extends BaseFragment {

	private static final String TAG = "MenuFragment";

	@ViewInject(R.id.left_menu_list)
	private ListView leftMenuList;

	private int selectPosition = 0;
	
	private MenuAdapter adapter;
	
	private List<String> menuStrList = new ArrayList<String>();
	
	@Override
	public View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);
		Log.i(TAG, "initView");
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("select_position", selectPosition);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("select_position")) {
			selectPosition = savedInstanceState
					.getInt("select_position");
		}
		Log.i(TAG, "initData");

	}
	
	public void initMenuList(List<String> menuStrs) {
		menuStrList.clear();
		menuStrList.addAll(menuStrs);
		leftMenuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					sm.toggle();
					break;

				default:
					break;
				}
			}
		});
		if(adapter == null) {
			adapter = new MenuAdapter(context, menuStrList, R.layout.layout_item_menu);
			leftMenuList.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		Log.i(TAG, "initMenuList");
	}
	
	class MenuAdapter extends ZYBaseAdapter<String> {
		
		public MenuAdapter(Context context, List<String> list, int viewResId) {
			super(context, list, viewResId);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView == null) {
				convertView = View.inflate(context, viewResId, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_menu_item);
				holder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_menu_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvTitle.setText(list.get(position));
			if(position == selectPosition) {
				convertView.setSelected(true);
				convertView.setPressed(true);
				holder.ivArrow.setImageResource(R.drawable.menu_arr_select);
				holder.tvTitle.setTextColor(context.getResources().getColor(R.color.menu_item_text_color));
				convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
			} else {
				convertView.setSelected(false);
				convertView.setPressed(false);
				convertView.setBackgroundColor(Color.TRANSPARENT);
				holder.ivArrow.setImageResource(R.drawable.menu_arr_normal);
				holder.tvTitle.setTextColor(context.getResources().getColor(R.color.white));
			}
			return convertView;
		}
		
	}
	
	class ViewHolder {
		TextView tvTitle;
		ImageView ivArrow;
	}
	@OnItemClick(R.id.left_menu_list)
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		if(position == selectPosition) {
			return;
		}
		selectPosition = position;
		adapter.notifyDataSetChanged();
	}

}
