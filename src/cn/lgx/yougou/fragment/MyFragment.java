package cn.lgx.yougou.fragment;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.AddressShowActivity;
import cn.lgx.yougou.FeedBackActivity;
import cn.lgx.yougou.LoginActivity;
import cn.lgx.yougou.MyLoveActivity;
import cn.lgx.yougou.MyMsgActivity;
import cn.lgx.yougou.MyOrderActivity;
import cn.lgx.yougou.MyPinglunActivity;
import cn.lgx.yougou.R;
import cn.lgx.yougou.UpdatePswActivity;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseFragment;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;

public class MyFragment extends BaseFragment implements BaseInterface, OnClickListener {
	@ViewInject(R.id.fragment_my_zhanghao)
	private TextView tvzhanghao;
	@ViewInject(R.id.fragment_my_yue)
	private TextView tvyue;
	@ViewInject(R.id.fragment_my_etjine)
	private EditText etjine;
	@ViewInject(R.id.fragment_my_login)
	private TextView tvlogin;
	@ViewInject(R.id.fragment_my_exit)
	private TextView tvexit;
	private UserBean ub;
	private LinearLayout lin[] = new LinearLayout[6];
	private int[] linIds = { R.id.fragment_my_dingdlin, R.id.fragment_my_addresslin, R.id.fragment_my_msglin,
			R.id.fragment_my_lovelin,R.id.act_fragment_my_fkuilin,R.id.fragment_my_pinlunlin };

	private Double jine;// ��ֵ���

	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_my, null);
		ViewUtils.inject(this, v);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();

	}

	@Override
	public void initDatas() {
		// ��û������
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		// ����л�����������˺���
		if (ub != null) {
			// ��ʾ�˺�
			tvzhanghao.setText(ub.getUsername());
			tvlogin.setVisibility(View.INVISIBLE);
			// ��ʾ���
			getYue();
		}else {
			tvexit.setVisibility(View.INVISIBLE);
		}
	}

	// ��ѯ��ǰ�û���������ʾ
	private void getYue() {
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", ub.getObjectId());
		query.findObjects(getActivity(), new FindListener<UserBean>() {

			@Override
			public void onSuccess(List<UserBean> arg0) {
				if (arg0.get(0).getYue() == null) {
					tvyue.setText(0 + "Ԫ");
				} else {
					tvyue.setText(arg0.get(0).getYue() + "Ԫ");
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}

	@Override
	public void initViews() {
		for (int i = 0; i < 6; i++) {
			lin[i] = linByid(linIds[i]);
			lin[i].setOnClickListener(this);
		}

	}

	// ��½����
	@OnClick(R.id.fragment_my_login)
	public void LoginOnClick(View v) {
		startActivity(new Intent(getActivity(), LoginActivity.class));
	}

	// ��½�ɹ��Ժ�����ݻ���
	@Override
	public void onStart() {
		super.onStart();
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);
		String zhanghao_Str = (String) MyApplication.getData("uphone", true);
		if (zhanghao_Str != null) {
			tvzhanghao.setText(zhanghao_Str);
		}
		if (ub!=null) {
			getYue();
			tvlogin.setVisibility(View.INVISIBLE);
			tvexit.setVisibility(View.VISIBLE);
		}
		
	}

	// �޸�����
	@OnClick(R.id.fragment_my_updatepsw)
	public void updatepswOnClick(View v) {
		if (ub != null) {
			startActivity(new Intent(getActivity(), UpdatePswActivity.class));
		}
	}

	// ��ֵ�Ĳ���
	@OnClick(R.id.act_mymsg_chongzhi)
	public void chongzhiOnClick(View v) {
		if (ub==null) {
			startActivity(new Intent(getActivity(), LoginActivity.class));
		}
		
		if (etjine.getVisibility() == View.INVISIBLE) {
			etjine.setVisibility(View.VISIBLE);
			return;
		}
		
		
		String jine_Str = etjine.getText().toString().trim();
		if (jine_Str == null || "".equals(jine_Str)) {
			toastL("�������ֵ���");
			return;
		}
		jine = Double.parseDouble(jine_Str);
		String[] items = { "֧����", "΢��" };
		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							payByAliorWeixin(true);
						} else if (which == 1) {
							payByAliorWeixin(false);
						}
					}
				}).setCancelable(true).setTitle("��ѡ��").show();

	}

	// ���г�ֵ
	protected void payByAliorWeixin(boolean flag) {

		DialogUtils.showDialog(getActivity(), null, null, true);
		BP.pay(getActivity(), "�Ź���ֵ", "��ֵ", 0.02, flag, new PListener() {

			private String orderId;

			// ��Ϊ�����ԭ��,֧�����δ֪(С�����¼�),���ڱ�������Ժ��ֶ���ѯ
			@Override
			public void unknow() {
				toastS("��ֵ���δ֪,���Ժ��ֶ���ѯ");
				DialogUtils.dismiss();
			}

			// ֧���ɹ�,������ϴ����ֶ���ѯȷ��
			@Override
			public void succeed() {
				DialogUtils.dismiss();

				BP.query(getActivity(), orderId, new QListener() {

					@Override
					public void succeed(String arg0) {
						logI("�������𣿣���������");
						UserBean uuBean = new UserBean();

						uuBean.setYue(ub.getYue() + jine);
						ub.setYue(ub.getYue()+jine);
						uuBean.update(getActivity(), ub.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								toastS("��ֵ�ɹ���");
								getYue();
								etjine.setVisibility(View.INVISIBLE);
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								toastS(arg0 + "-" + arg1);
							}
						});
					}

					@Override
					public void fail(int arg0, String arg1) {
						logI(arg1);
						toastS(arg1);
					}
				});
			}

			// ���۳ɹ����,���ض�����
			@Override
			public void orderId(String orderId) {
				this.orderId = orderId;
				logI(orderId);
			}

			// ֧��ʧ��,ԭ��������û��ж�֧������,Ҳ����������ԭ��
			@Override
			public void fail(int code, String reason) {
				if (code == 6001 || code == -2) {
					toastS("֧�����ж�");
					DialogUtils.dismiss();
				} else if (code == -3) {
					toastS("��⵽����δ��װ֧�����,�޷�����΢��֧��,��ѡ��װ���(�Ѵ���ڱ���,����������)");
					DialogUtils.dismiss();
				}
			}
		});
	}

	// ��������ѡ��Ĳ���
	@Override
	public void onClick(View v) {
		//����ǰ�û�Ϊnull����ת����½����
		if (ub == null) {
			startActivity(new Intent(getActivity(), LoginActivity.class));
			return;
		}
		switch (v.getId()) {
		//��ת���ҵĶ�������
		case R.id.fragment_my_dingdlin:
			startActivity(new Intent(getActivity(), MyOrderActivity.class));
			break;
		//��ת���ҵĵ�ַ����
		case R.id.fragment_my_addresslin:
			startActivity(new Intent(getActivity(), AddressShowActivity.class));
			break;
		// ��ת����������
		case R.id.fragment_my_msglin:
			startActivity(new Intent(getActivity(), MyMsgActivity.class));
			break;
		//��ת���ҵ��ղؽ���
		case R.id.fragment_my_lovelin:
			startActivity(new Intent(getActivity(), MyLoveActivity.class));
			break;
		//��ս����������Ľ���
		case R.id.act_fragment_my_fkuilin:
			startActivity(new Intent(getActivity(), FeedBackActivity.class));
			break;
		//��ת���ҵ����۽���
		case R.id.fragment_my_pinlunlin:
			startActivity(new Intent(getActivity(), MyPinglunActivity.class));
			break;
		}
	}

	// �˳�
	@OnClick(R.id.fragment_my_exit)
	public void exitOnClick(View v) {
		DialogUtils.showAlertDialog(getActivity(), "���棡", "��ȷ��Ҫ�˳���¼��", true, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BmobUser.logOut(getActivity());// ��������û�����
				getActivity().finish();
			}
		});

	}
}
