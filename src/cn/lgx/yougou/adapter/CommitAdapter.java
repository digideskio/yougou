package cn.lgx.yougou.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.base.MyBaseAdapter;
import cn.lgx.yougou.bean.CartBean;
import cn.lgx.yougou.utils.ImageUtils;

public class CommitAdapter extends MyBaseAdapter {
	
	private List<CartBean> datas;
	@SuppressWarnings("unused")
	private Context context;
	private LayoutInflater inflater;
	
	public CommitAdapter(List<CartBean> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.commit_item, null);
			vh = new ViewHolder();
			vh.tvinfo = (TextView) v.findViewById(R.id.commit_item_info);
			vh.tvprice = (TextView) v.findViewById(R.id.commit_item_price);
			vh.tvcolor = (TextView) v.findViewById(R.id.commit_item_color);
			vh.tvnumber = (TextView) v.findViewById(R.id.commit_item_number);
			vh.tvsize = (TextView) v.findViewById(R.id.commit_item_size);
			vh.goodsimg = (ImageView) v.findViewById(R.id.commit_item_img);
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		CartBean cart = datas.get(position);
		vh.tvinfo.setText(cart.getGoodsinfo());
		vh.tvprice.setText(cart.getPrice()+"");
		vh.tvcolor.setText(cart.getColor());
		vh.tvsize.setText(cart.getSize());
		vh.tvnumber.setText(cart.getNumber()+"");
		ImageUtils.loadSmallImg(vh.goodsimg, cart.getFileUrl());
		return v;
	}
	
	class ViewHolder{
		TextView tvinfo,tvprice,tvcolor,tvsize,tvnumber;
		ImageView goodsimg;
	}
}
