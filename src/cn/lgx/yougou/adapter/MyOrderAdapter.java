package cn.lgx.yougou.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.PingLunActivity;
import cn.lgx.yougou.R;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.MyBaseAdapter;
import cn.lgx.yougou.bean.OrderBean;
import cn.lgx.yougou.utils.ImageUtils;

public class MyOrderAdapter extends MyBaseAdapter {

	private List<OrderBean> datas;
	private Context context;
	private LayoutInflater inflater;

	public MyOrderAdapter(List<OrderBean> datas, Context context) {
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
			v = inflater.inflate(R.layout.order_item, null);
			vh = new ViewHolder();
			//初始化订单界面显示的控件
			vh.tvaddress = (TextView) v.findViewById(R.id.order_item_address);
			vh.tvcolor = (TextView) v.findViewById(R.id.order_item_color);
			vh.tvsize = (TextView) v.findViewById(R.id.order_item_size);
			vh.tvprice = (TextView) v.findViewById(R.id.order_item_price);
			vh.tvinfo = (TextView) v.findViewById(R.id.order_item_info);
			vh.tvtime = (TextView) v.findViewById(R.id.order_item_time);
			vh.tvpinglun = (TextView) v.findViewById(R.id.order_item_pinlun);
			vh.tvshouhuo = (TextView) v.findViewById(R.id.order_item_shouhuo);
			vh.goodsimg = (ImageView) v.findViewById(R.id.order_item_img);
			vh.tvnumber = (TextView) v.findViewById(R.id.order_item_number);
			//设置标签
			v.setTag(vh);
		} else {
			//回收利用
			vh = (ViewHolder) v.getTag();
		}
		final OrderBean order = datas.get(position);
		//显示订单详情
		vh.tvaddress.setText(order.getAddress());
		vh.tvinfo.setText(order.getOrderinfo());
		vh.tvcolor.setText(order.getColor());
		vh.tvsize.setText(order.getSize());
		vh.tvprice.setText(order.getPrice() + "");
		vh.tvtime.setText(order.getCreatedAt());
		vh.tvnumber.setText(order.getNumber() + "");
		ImageUtils.loadSmallImg(vh.goodsimg, order.getFileUrl());
		
		//判断确认收货和发表评论的显示状态和设置监听
		if (!order.getIsshouhuo()) {//没确认收货
			vh.tvshouhuo.setVisibility(View.VISIBLE);
			vh.tvpinglun.setVisibility(View.INVISIBLE);
			//收获的监听
			setShouhuo(vh.tvshouhuo,vh.tvpinglun,order);
		} else if (!order.getIspinglun()) {//没评论
			vh.tvshouhuo.setVisibility(View.INVISIBLE);
			vh.tvpinglun.setVisibility(View.VISIBLE);
			vh.tvpinglun.setText("发表评论");
		} else {//已评论
			vh.tvshouhuo.setVisibility(View.INVISIBLE);
			vh.tvpinglun.setVisibility(View.VISIBLE);
			vh.tvpinglun.setText("追加评论");
		}
		//评论的监听
		vh.tvpinglun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//跳转到评论界面
				MyApplication.putData("order", order);
				context.startActivity(new Intent(context, PingLunActivity.class));

			}
		});
	
		return v;
	}
	//确认收货的监听
	private void setShouhuo(final TextView tvshouhuo, final TextView tvpinglun, final OrderBean order) {
		tvshouhuo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrderBean neworder = new OrderBean();
				neworder.setIsshouhuo(true);
				//更新服务器
				neworder.update(context, order.getObjectId(), new UpdateListener() {

					@Override
					public void onSuccess() {
						//切换不同的显示状态
						Toast.makeText(context, "收货成功，请对商品做出评价！", Toast.LENGTH_SHORT).show();
						tvshouhuo.setVisibility(View.INVISIBLE);
						tvpinglun.setVisibility(View.VISIBLE);
						tvpinglun.setText("发表评论");
					}

					@Override
					public void onFailure(int arg0, String arg1) {

					}
				});

			}
		});
	}

	class ViewHolder {
		TextView tvinfo, tvcolor, tvsize, tvprice, tvaddress, tvtime, tvpinglun, tvshouhuo, tvnumber;
		ImageView goodsimg;

	}
}
