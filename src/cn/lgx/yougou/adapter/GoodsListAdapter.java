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
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.utils.ImageUtils;

public class GoodsListAdapter extends MyBaseAdapter {

	private List<GoodsBean> datas;
	private Context context;
	private LayoutInflater inflater;
	
	public GoodsListAdapter(List<GoodsBean> datas, Context context) {
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
			v = inflater.inflate(R.layout.goods_item, null);
			vh = new ViewHolder();
			vh.tvinfo = (TextView) v.findViewById(R.id.goods_item_info);
			vh.tvprice = (TextView) v.findViewById(R.id.goods_item_price);
			vh.tvsclaeNum = (TextView) v.findViewById(R.id.goods_item_sacle);
			vh.goodsimg = (ImageView) v.findViewById(R.id.goods_item_goodsimg);
			v.setTag(vh);
		}else{
			vh = (ViewHolder) v.getTag();
		}
		
		vh.tvinfo.setText(datas.get(position).getInfo());
		vh.tvprice.setText(datas.get(position).getPrice()+"");
		vh.tvsclaeNum.setText(datas.get(position).getSalesNum()+"");
		ImageUtils.loadSmallImg(vh.goodsimg, datas.get(position).getGoodspng().getFileUrl(context));
		return v;
	}
	
	class ViewHolder{
		TextView tvinfo,tvprice,tvsclaeNum;
		ImageView goodsimg;
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setData(List data) {
		this.datas = data;
		notifyDataSetChanged();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addData(List data) {
		this.datas.addAll(data);
		notifyDataSetChanged();
	}
}
