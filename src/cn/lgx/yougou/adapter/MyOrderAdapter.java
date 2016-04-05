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
			//��ʼ������������ʾ�Ŀؼ�
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
			//���ñ�ǩ
			v.setTag(vh);
		} else {
			//��������
			vh = (ViewHolder) v.getTag();
		}
		final OrderBean order = datas.get(position);
		//��ʾ��������
		vh.tvaddress.setText(order.getAddress());
		vh.tvinfo.setText(order.getOrderinfo());
		vh.tvcolor.setText(order.getColor());
		vh.tvsize.setText(order.getSize());
		vh.tvprice.setText(order.getPrice() + "");
		vh.tvtime.setText(order.getCreatedAt());
		vh.tvnumber.setText(order.getNumber() + "");
		ImageUtils.loadSmallImg(vh.goodsimg, order.getFileUrl());
		
		//�ж�ȷ���ջ��ͷ������۵���ʾ״̬�����ü���
		if (!order.getIsshouhuo()) {//ûȷ���ջ�
			vh.tvshouhuo.setVisibility(View.VISIBLE);
			vh.tvpinglun.setVisibility(View.INVISIBLE);
			//�ջ�ļ���
			setShouhuo(vh.tvshouhuo,vh.tvpinglun,order);
		} else if (!order.getIspinglun()) {//û����
			vh.tvshouhuo.setVisibility(View.INVISIBLE);
			vh.tvpinglun.setVisibility(View.VISIBLE);
			vh.tvpinglun.setText("��������");
		} else {//������
			vh.tvshouhuo.setVisibility(View.INVISIBLE);
			vh.tvpinglun.setVisibility(View.VISIBLE);
			vh.tvpinglun.setText("׷������");
		}
		//���۵ļ���
		vh.tvpinglun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//��ת�����۽���
				MyApplication.putData("order", order);
				context.startActivity(new Intent(context, PingLunActivity.class));

			}
		});
	
		return v;
	}
	//ȷ���ջ��ļ���
	private void setShouhuo(final TextView tvshouhuo, final TextView tvpinglun, final OrderBean order) {
		tvshouhuo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OrderBean neworder = new OrderBean();
				neworder.setIsshouhuo(true);
				//���·�����
				neworder.update(context, order.getObjectId(), new UpdateListener() {

					@Override
					public void onSuccess() {
						//�л���ͬ����ʾ״̬
						Toast.makeText(context, "�ջ��ɹ��������Ʒ�������ۣ�", Toast.LENGTH_SHORT).show();
						tvshouhuo.setVisibility(View.INVISIBLE);
						tvpinglun.setVisibility(View.VISIBLE);
						tvpinglun.setText("��������");
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
