package cn.lgx.yougou.adapter;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.base.MyBaseAdapter;
import cn.lgx.yougou.bean.CartBean;
import cn.lgx.yougou.utils.ImageUtils;

@SuppressLint("HandlerLeak")
public class CartAdapter extends MyBaseAdapter {

	private List<CartBean> datas;
	@SuppressWarnings("unused")
	private Context context;
	private LayoutInflater inflater;
	private Handler mHandler;
	private static HashMap<Integer, Boolean> isSelected; // �ж��Ƿ�ѡ�еļ���
	int number = 0; // ��¼��Ʒ������
	private boolean flag;

	@SuppressLint("UseSparseArrays")
	public CartAdapter(List<CartBean> datas, Context context, Handler mHandler, boolean flag) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.mHandler = mHandler;
		this.flag = flag;
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}

	private void initData() {
		// ����δ��ѡ��
		for (int i = 0; i < datas.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		CartAdapter.isSelected = isSelected;
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
	public View getView(final int position, View v, ViewGroup parent) {
		number = 0;
		ViewHolder vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.cart_item, null);
			vh = new ViewHolder();
			vh.tvcolor = (TextView) v.findViewById(R.id.cart_item_color);
			vh.tvinfo = (TextView) v.findViewById(R.id.cart_item_info);
			vh.tvsize = (TextView) v.findViewById(R.id.cart_item_size);
			vh.tvprice = (TextView) v.findViewById(R.id.cart_item_price);
			vh.tvnum = (TextView) v.findViewById(R.id.cart_item_num);
			vh.goodsimg = (ImageView) v.findViewById(R.id.cart_item_goodspng);
			vh.select = (CheckBox) v.findViewById(R.id.cart_item_select);
			vh.butadd = (Button) v.findViewById(R.id.cart_item_add);
			vh.butjian = (Button) v.findViewById(R.id.cart_item_jian);

			v.setTag(vh);
		} else {
			vh = (ViewHolder) v.getTag();
		}
		
		CartBean cart = datas.get(position);
		vh.tvinfo.setText(cart.getGoodsinfo());
		vh.tvcolor.setText(cart.getColor());
		vh.tvsize.setText(cart.getSize());
		vh.tvprice.setText(cart.getPrice() + "");
		vh.tvnum.setText(cart.getNumber() + "");
		ImageUtils.loadSmallImg(vh.goodsimg, cart.getFileUrl());
		number = cart.getNumber();
		vh.select.setChecked(getIsSelected().get(position));
		//���üӼ���Ʒ�İ�ť����ʾ״̬�ͼ���
		if (flag) {
			vh.butadd.setBackgroundResource(R.drawable.addcart);
			vh.butadd.setClickable(true);
			if (cart.getNumber() > 1) {
				vh.butjian.setBackgroundResource(R.drawable.jian);
				vh.butjian.setEnabled(true);
			} else {
				vh.butjian.setBackgroundResource(R.drawable.jianno);
				vh.butjian.setEnabled(false);
			}
			setCartNum(vh.butadd, vh.butjian, vh.tvnum, position);
		} else {
			vh.butadd.setBackgroundResource(R.drawable.addno);
			vh.butadd.setClickable(false);
			vh.butjian.setBackgroundResource(R.drawable.jianno);
			vh.butjian.setClickable(false);
		}

		// CheckBoxѡ��ı������
		vh.select.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getIsSelected().put(position, isChecked);
				CartBean bean = datas.get(position);
				bean.setChoosed(isChecked);
				// ��ȡѡ����Ʒ�ļ۸�
				mHandler.sendMessage(mHandler.obtainMessage(10, getTotalPrice()));
				// ������е���Ʒȫ����ѡ�У���ȫѡ��ťҲĬ�ϱ�ѡ��
				mHandler.sendMessage(mHandler.obtainMessage(11, isAllSelected()));
				// �ж��Ƿ��ֱ�ѡ�е�
				mHandler.sendMessage(mHandler.obtainMessage(12, isSelect()));
			}

		});
		
		return v;
	}

	private void setCartNum(Button butadd, final Button butjian, final TextView tvnum, final int position) {
		
		// ��Ʒ������һ
		butadd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				number++;
				tvnum.setText("" + number);
				datas.get(position).setNumber(number);
				butjian.setBackgroundResource(R.drawable.jian);
				butjian.setEnabled(true);
				butjian.setClickable(true);
				mHandler.sendMessage(mHandler.obtainMessage(10, getTotalPrice()));
			}
		});
		// ��Ʒ������һ
		butjian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				number--;
				datas.get(position).setNumber(number);
				mHandler.sendMessage(mHandler.obtainMessage(10, getTotalPrice()));
				tvnum.setText("" + number);
				if (number == 1) {
					butjian.setBackgroundResource(R.drawable.jianno);
					butjian.setClickable(false);
				}
			}
		});
	}


	class ViewHolder {

		TextView tvinfo, tvcolor, tvsize, tvprice, tvnum;
		Button butjian, butadd;
		ImageView goodsimg;
		CheckBox select;
	}

	@SuppressWarnings("unused")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) { // ������Ʒ����
				((TextView) msg.obj).setText(String.valueOf(number));

				// ������Ʒ������֪ͨActivity������Ҫ���ѵ��ܽ��
				mHandler.sendMessage(mHandler.obtainMessage(10, getTotalPrice()));
			}
		}
	};

	/**
	 * ����ѡ����Ʒ�Ľ��
	 * 
	 * @return ������Ҫ���ѵ��ܽ��
	 */
	private Double getTotalPrice() {
		CartBean bean = null;
		Double totalPrice = 0.0;
		for (int i = 0; i < datas.size(); i++) {
			bean = datas.get(i);
			if (bean.isChoosed()) {
				totalPrice += bean.getNumber() * bean.getPrice();
			}
		}
		return totalPrice;
	}

	/**
	 * �ж��Ƿ��ﳵ�����е���Ʒȫ����ѡ��
	 * 
	 * @return true������Ŀȫ����ѡ�� false������Ŀû�б�ѡ��
	 */
	private boolean isAllSelected() {
		boolean flag = true;
		for (int i = 0; i < datas.size(); i++) {
			if (!getIsSelected().get(i)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * �ж��Ƿ��б�ѡ���
	 * 
	 * @return
	 */
	private boolean isSelect() {
		boolean flag = false;
		for (int i = 0; i < datas.size(); i++) {
			if (getIsSelected().get(i)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
}
