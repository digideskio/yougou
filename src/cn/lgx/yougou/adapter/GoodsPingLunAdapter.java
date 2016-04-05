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
			// ��ʼ���ؼ�
			vh.tvyonghu = (TextView) v.findViewById(R.id.pinglun_item_yonghu);
			vh.tvtime = (TextView) v.findViewById(R.id.pinglun_item_time);
			vh.tvpinglun = (TextView) v.findViewById(R.id.pinglun_item_conten);
			vh.rating = (RatingBar) v.findViewById(R.id.pinglun_item_ratingbar);
			// ���ñ�ǩ
			v.setTag(vh);
		} else {
			// ��������
			vh = (ViewHolder) v.getTag();
		}
		// ������������
		vh.tvpinglun.setText(datas.get(position).getContent());
		// ��ʾ����ʱ��
		vh.tvtime.setText(datas.get(position).getCreatedAt());
		// ��ʾ�������Ǽ���
		vh.rating.setProgress((int) datas.get(position).getRating_num() * 2);
		// ����RatingBar���ɵ��
		vh.rating.setIsIndicator(true);
		String username = datas.get(position).getUsername();
		UserBean ub = BmobUser.getCurrentUser(context, UserBean.class);
		// ������ʾ�������۵��û���
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
		// �������ͬ���������û��ĵ��ֻ����м���λ���*��
		StringBuilder sb = new StringBuilder();
		// ѭ�����ֻ����ַ�������ַ��������ж��滻
		for (int i = 0; i < username.length(); i++) {
			char c = username.charAt(i);
			if (i >= 3 && i <= 6) {
				sb.append('*');
			} else {
				sb.append(c);
			}
		}
		// ��ʾ�滻����ֻ���
		tvyonghu.setText(sb.toString());
	}

	// ����ViewHolder��
	class ViewHolder {
		TextView tvyonghu, tvtime, tvpinglun;
		RatingBar rating;
	}

}
