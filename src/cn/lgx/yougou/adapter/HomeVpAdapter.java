package cn.lgx.yougou.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class HomeVpAdapter extends PagerAdapter {
	
	private ArrayList<View> items;
	private int[] imgResIDs;
	private ViewPager pager;
	
	public HomeVpAdapter(ArrayList<View> items, int[] imgResIDs, ViewPager pager) {
		super();
		this.items = items;
		this.imgResIDs = imgResIDs;
		this.pager = pager;
	}

	public Object instantiateItem(View container, int position) {
		View layout = items.get(position % items.size());
		pager.addView(layout);
		return layout;
	}

	// Ïú»Ù
	@Override
	public void destroyItem(View container, int position, Object object) {
		View layout = items.get(position % items.size());
		pager.removeView(layout);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;

	}

	@Override
	public int getCount() {
		return imgResIDs.length;
	}

}
