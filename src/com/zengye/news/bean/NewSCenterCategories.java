package com.zengye.news.bean;

import java.util.List;

public class NewSCenterCategories extends BaseBean{

	public int[] extend;
	public List<NewsData> data;
	public static class NewsData{
		public int type;
		public String title;
		public int id;
		public String url;
		public String url1;
		public String excurl;
		public String weekurl;
		public String dayurl;
		public List<Child> children;
	}
	
	public static class Child {
		public int type;
		public String title;
		public int id;
		public String url;
	}
}
