package cn.lgx.yougou.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.CommitOrderActivity;
import cn.lgx.yougou.R;
import cn.lgx.yougou.adapter.CartAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseFragment;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.CartBean;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;

public class CartFragment extends BaseFragment implements BaseInterface, OnClickListener {

	private UserBean ub;
	private ListView lv;
	private TextView tvPrice, tvedit, tvdelete ,tvjiesuan,tvcommit;
	private boolean flag = true;// ȫѡ��ȫȡ��
	private CheckBox allselect;// ȫѡ
	private List<CartBean> datas;
	private CartAdapter adapter;
	private List<CartBean> selectedcart;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) { // ����ѡ����Ʒ���ܼ۸�
				Double price = (Double) msg.obj;
				if (price > 0) {
					tvPrice.setText(price + "");
				}
			} else if (msg.what == 11) {
				// �����б��е���Ʒȫ����ѡ�У���ȫѡ��ťҲ��ѡ��
				// flag��¼�Ƿ�ȫ��ѡ��
				flag = !(Boolean) msg.obj;
				allselect.setChecked((Boolean) msg.obj);
			}else if(msg.what == 12){
				if ((Boolean)msg.obj) {
					settextClick();
				}else {
					tvjiesuan.setClickable(false);
					tvjiesuan.setBackgroundColor(Color.parseColor("#828282"));
					tvPrice.setText("0.00");
				}
			}
		}

	};

	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_cart, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);
		if (ub==null) {
			logI("return");
			return;
		}
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initDatas() {
		//�����û�ID��ѯ���û����ﳵ�����Ʒ
		BmobQuery<CartBean> query = new BmobQuery<CartBean>();
		query.addWhereEqualTo("userid", ub.getObjectId());
		query.order("-createdAt");
		query.findObjects(getActivity(), new FindListener<CartBean>() {

			@Override
			public void onSuccess(List<CartBean> arg0) {
				datas = arg0;
				
				adapter = new CartAdapter(arg0, getActivity(), handler,false);
				lv.setAdapter(adapter);//��������Դ
			}

			@Override
			public void onError(int arg0, String arg1) {
				logI("CartFragment--" + arg0 + "--" + arg1);
			}
		});
	}
	
	
	@Override
	public void initViewOper() {
		
	}

	@Override
	public void initViews() {
		lv = (ListView) findViewById(R.id.fragment_cart_lv);
		tvedit = tvByid(R.id.fragment_cart_edit);
		tvPrice = tvByid(R.id.fragment_cart_allprice);
		tvdelete = tvByid(R.id.fragment_cart_delete);
		allselect = (CheckBox) findViewById(R.id.fragment_cart_select);
		tvjiesuan = tvByid(R.id.fragment_cart_jiesuan);
		tvcommit = tvByid(R.id.fragment_cart_wancheng);
		tvedit.setOnClickListener(this);
		allselect.setOnClickListener(this);
		tvdelete.setOnClickListener(this);
		tvcommit.setOnClickListener(this);
	}
	
	private void settextClick() {
		tvjiesuan.setClickable(true);
		tvjiesuan.setBackgroundColor(Color.parseColor("#ff0000"));
		tvjiesuan.setOnClickListener(this);			
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_cart_select:
			selectedAll();// ȫѡ
			break;
		case R.id.fragment_cart_edit://�༭����
			adapter = new CartAdapter(datas, getActivity(), handler,true);
			lv.setAdapter(adapter);
			allselect.setChecked(false);
			tvedit.setVisibility(View.INVISIBLE);
			tvdelete.setVisibility(View.VISIBLE);
			tvcommit.setVisibility(View.VISIBLE);
			break;
		case R.id.fragment_cart_delete:
			//ɾ�����ﳵ�����Ʒ
			deleteCart();
			break;
		case R.id.fragment_cart_wancheng://��ɼ���
			adapter = new CartAdapter(datas, getActivity(), handler,false);
			lv.setAdapter(adapter);
			allselect.setChecked(false);
			tvedit.setVisibility(View.VISIBLE);
			tvdelete.setVisibility(View.INVISIBLE);
			tvcommit.setVisibility(View.INVISIBLE);
			break;
		case R.id.fragment_cart_jiesuan:
			//����
			selectedcart = getSelected();
			MyApplication.putData("cart", selectedcart);
			MyApplication.putData("price", Double.parseDouble(tvPrice.getText().toString().trim()));
			startActivity(new Intent(getActivity(), CommitOrderActivity.class));
			break;
		}
	}
	
	
	//ɾ�����ﳵ�����Ʒ
	private void deleteCart() {
		selectedcart = getSelected();
		if (selectedcart.size()==0) {
			toastS("��ѡ����Ҫɾ������Ʒ");
			return;
		}
		DialogUtils.showAlertDialog(getContext(), "���棡", "��ȷ��ɾ����ѡ��Ʒ��", true, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				for (int i = 0; i < selectedcart.size(); i++) {
					CartBean cartBean = new CartBean();
					cartBean.setObjectId(selectedcart.get(i).getObjectId());
					//���·�����
					cartBean.delete(getActivity(), new DeleteListener() {
						
						@Override
						public void onSuccess() {
							toastS("ɾ���ɹ���");
							initDatas();
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							logI(arg1);
						}
					});
				
				}
			}
		});
	}
	//�õ���ѡ�����Ʒ�ļ���
	private List<CartBean> getSelected() {
		List<CartBean> selects = new ArrayList<>();
		for (int i = 0; i < datas.size(); i++) {
			if (CartAdapter.getIsSelected().get(i)) {
				selects.add(datas.get(i));
			}
		}
		return selects;
	}

	//ȫѡ��Ʒ
	private void selectedAll() {
		for (int i = 0; i < datas.size(); i++) {
			CartAdapter.getIsSelected().put(i, flag);
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(ub == null){
			return;
		}
		initDatas();
		allselect.setChecked(false);
		tvPrice.setText("0.00");
		tvjiesuan.setBackgroundColor(Color.parseColor("#828282"));
		tvjiesuan.setClickable(false);
	}
}
