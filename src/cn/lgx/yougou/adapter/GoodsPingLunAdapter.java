package cn.lgx.yougou.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.lgx.yougou.R;
import cn.lgx.yougou.base.MyBaseAdapter;
import cn.lgx.yougou.bean.PingLunBean;
import cn.lgx.yougou.bean.UserBean;

public class GoodsPingLunAdapter extends MyBaseAdapter {

	private List<PingLunBean> datas;
	private Context context;
	private LayoutInflater inflater;

	public GoodsPingLunAdapter(List<PingLunBean> datas, Context context) {
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
			v = inflater.inflate(R.layout.pinglun_item, null);
			vh = new ViewHolder();
			// 初始化控件
			vh.tvyonghu = (TextView) v.findViewById(R.id.pinglun_item_yonghu);
			vh.tvtime = (TextView) v.findViewById(R.id.pinglun_item_time);
			vh.tvpinglun = (TextView) v.findViewById(R.id.pinglun_item_conten);
			vh.rating = (RatingBar) v.findViewById(R.id.pinglun_item_ratingbar);
			// 设置标签
			v.setTag(vh);
		} else {
			// 回收利用
			vh = (ViewHolder) v.getTag();
		}
		// 设置评论内容
		vh.tvpinglun.setText(datas.get(position).getContent());
		// 显示评论时间
		vh.tvtime.setText(datas.get(position).getCreatedAt());
		// 显示该评价是几星
		vh.rating.setProgress((int) datas.get(position).getRating_num() * 2);
		// 设置RatingBar不可点击
		vh.rating.setIsIndicator(true);
		String username = datas.get(position).getUsername();
		UserBean ub = BmobUser.getCurrentUser(context, UserBean.class);
		// 用于显示该条评论的用户名
		if (ub == null) {
			setText(username, vh.tvyonghu);
		}
		else if (username.equals(ub.getUsername())) {
			vh.tvyonghu.setText(username);
		}else{
			setText(username, vh.tvyonghu);
		}
		return v;
	}

	private void setText(String username,TextView tvyonghu) {
		// 如果不相同，则将其它用户的的手机号中间四位变成*号
		StringBuilder sb = new StringBuilder();
		// 循环将手机号字符串变成字符，进行判断替换
		for (int i = 0; i < username.length(); i++) {
			char c = username.charAt(i);
			if (i >= 3 && i <= 6) {
				sb.append('*');
			} else {
				sb.append(c);
			}
		}
		// 显示替换后的手机号
		tvyonghu.setText(sb.toString());
	}

	// 设置ViewHolder类
	class ViewHolder {
		TextView tvyonghu, tvtime, tvpinglun;
		RatingBar rating;
	}

}
