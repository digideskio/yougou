package cn.lgx.yougou.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.base.MyBaseAdapter;
import cn.lgx.yougou.bean.MyAddressBean;

public class AddressAdapter extends MyBaseAdapter {

	private List<MyAddressBean> datas;
	private LayoutInflater inflater;
	@SuppressWarnings("unused")
	private Context context;
	
	public AddressAdapter(List<MyAddressBean> datas, Context context) {
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
		//回收池判断
		if (v == null) {
			v = inflater.inflate(R.layout.address_item, null);
			vh = new ViewHolder();
			//初始化控件
			vh.tvinfo = (TextView) v.findViewById(R.id.address_item_info);
			vh.tvname = (TextView) v.findViewById(R.id.address_item_name);
			vh.tvphone = (TextView) v.findViewById(R.id.address_item_phone);
			//设置标签
			v.setTag(vh);
		}else {
			vh = (ViewHolder) v.getTag();
		}
		//显示数据
		vh.tvinfo.setText(datas.get(position).getInfo());
		vh.tvname.setText(datas.get(position).getName());
		vh.tvphone.setText(datas.get(position).getPhone());
		return v;
	}
	
	class ViewHolder{
		TextView tvinfo,tvname,tvphone;
	}
}
