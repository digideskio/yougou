package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.adapter.CommitAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.CartBean;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.bean.MyAddressBean;
import cn.lgx.yougou.bean.OrderBean;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ListViewForScrollView;

public class CommitOrderActivity extends BaseActivity implements BaseInterface {

	@ViewInject(R.id.act_commitorder_address)
	private TextView tvaddress;
	@ViewInject(R.id.act_commitorder_peistime)
	private TextView tvtime;
	@ViewInject(R.id.act_commitorder_price)
	private TextView tvprice2;
	private ListViewForScrollView lv;
	private UserBean ub;
	private String address_str;
	private List<CartBean> datas;
	private Double cartprice;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_commitorder);
		ViewUtils.inject(getAct());
		lv = (ListViewForScrollView) findViewById(R.id.act_commitorder_lv);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initDatas() {
		//�õ����ﳵ����ܼ۸�
		cartprice =  (Double) MyApplication.getData("price", true);
		//�õ���Ҫ����Ĺ��ﳵ���󼯺�
		datas = (List<CartBean>) MyApplication.getData("cart", true);
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		//���ü۸�
		tvprice2.setText(""+cartprice);
		//���Ĭ�ϵ�ַ
		getMorenAddress();
		
	}
	
	//���Ĭ�ϵ�ַ�ķ���
	private void getMorenAddress() {
		BmobQuery<MyAddressBean> query = new BmobQuery<MyAddressBean>();
		query.addWhereEqualTo("uid", ub.getObjectId());
		query.findObjects(getAct(), new FindListener<MyAddressBean>() {
			
			@Override
			public void onSuccess(List<MyAddressBean> arg0) {
				String address_str = arg0.get(0).getInfo() + "\n\r\t" + arg0.get(0).getName() + "\t\t" + arg0.get(0).getPhone();
				tvaddress.setText(address_str);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
		
	}

	@Override
	public void initViewOper() {
		//��������Դ
		lv.setAdapter(new CommitAdapter(datas, getAct()));
	}

	// ѡ�����ͷ�ʽ
	@OnClick(R.id.act_commitorder_peisimg)
	public void selectSendTimeOnClick(View v) {
		String items[] = { "��һ����������", "����ʱ�䲻��", "������/�ڼ�������" };
		new AlertDialog.Builder(getAct()).setItems(items, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					tvtime.setText("��һ����������");
				} else if (which == 1) {
					tvtime.setText("����ʱ�䲻��");
				} else {
					tvtime.setText("������/�ڼ�������");
				}
			}
		}).setCancelable(true).show();
	}

	// ��ת����ַ����ѡ���ַ
	@OnClick(R.id.act_commitorder_address)
	public void selectAddressOnClick(View v) {
		startActivity(new Intent(getAct(), AddressShowActivity.class));
	}

	// �ύ����
	@OnClick(R.id.act_commitorder_commit)
	public void CommitOnClick(View v) {
		
		if (address_str==null) {
			toastS("��ѡ���ջ���ַ");
			return;
		}
		DialogUtils.showAlertDialog(getAct(), "���ѣ�", "��ȷ�����Ķ���û��������", true, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ѡ�����ַ�ʽ����
				String[] items = { "֧����", "΢��", "�ҵ����" };
				new AlertDialog.Builder(getAct()).setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							//ѡ��֧����
							selectAliorWeixin(true);
						} else if (which == 1) {
							//ѡ��΢��
							selectAliorWeixin(false);
						} else {
							//ѡ�����
							selectMyyue(2);
						}
					}

				}).setCancelable(true).show();

			}
		});

	}

	// ����֧������΢��֧��
	private void selectAliorWeixin(boolean flag) {

		DialogUtils.showDialog(getAct(), null, null, true);
		BP.pay(getAct(), "�Ź���Ʒ", null, 0.02, flag, new PListener() {

			private String order;
			
			@Override
			public void unknow() {
				DialogUtils.dismiss();
				toastS("֧�����δ֪,���Ժ��ֶ���ѯ");
			}
			//֧���ɹ�
			@Override
			public void succeed() {
				DialogUtils.dismiss();
				//����ѯ��������ʱ�򱣴涩��
				BP.query(getAct(), order, new QListener() {

					@Override
					public void succeed(String arg0) {
						//���涩��
						saveOrder(0);
					}

					@Override
					public void fail(int arg0, String arg1) {
						DialogUtils.dismiss();
						logI("������ѯʧ�ܣ�" + arg1);
					}
				});
			}

			@Override
			public void orderId(String order) {
				this.order = order;
			}

			@Override
			public void fail(int arg0, String arg1) {
				DialogUtils.dismiss();
				if (arg0 == 6001 || arg0 == -2) {
					toastS("֧�����ж�");
					DialogUtils.dismiss();
				} else if (arg0 == -3) {
					toastS("֧���쳣");
				}
			}
		});

	}
	//���涩���ķ���
	private void saveOrder(final int i) {
		//�������ж����ֶ�
		for (int j = 0; j < datas.size(); j++) {
			
			final CartBean cart = datas.get(j);
			final GoodsBean gBean = new GoodsBean();
			//��������
			gBean.increment("salesNum", cart.getNumber());
			OrderBean oBean = new OrderBean();
			//���ö���
 			oBean.setAddress(address_str);//���ö�����ַ
			oBean.setColor(cart.getColor());//���ö�����ɫ
			oBean.setSize(cart.getSize());//���ö����ߴ�
			oBean.setNumber(cart.getNumber());//���ö�������
			oBean.setGoodsId(cart.getGoodsId());//������ƷID
			oBean.setFileUrl(cart.getFileUrl());//������ƷͼƬ��Url
			oBean.setOrderinfo(cart.getGoodsinfo());//���ö�������
			oBean.setUserid(ub.getObjectId());
			oBean.setIspinglun(false);
			oBean.setIsshouhuo(false);
			oBean.setPrice(cart.getPrice()*cart.getNumber());
			//���涩�����ϴ�������
			oBean.save(getAct(), new SaveListener() {
			
				@Override
				public void onSuccess() {
					gBean.update(getAct(), cart.getGoodsId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							if (i == 0) {
								toastS("֧���ɹ���");
								deleteCart(datas);
								finish();
							}
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							toastS("֧��ʧ��:" + arg1);
						}
					});
					if (i == 2) {
						UserBean uu = new UserBean();
						logI(ub.getYue() + "");
						//����������ȥ�������ܼ۸�
						uu.setYue(ub.getYue() - cart.getPrice());
						//����
						ub.setYue(ub.getYue() - cart.getPrice());
						uu.update(getAct(), ub.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								toastS("֧���ɹ���");
								deleteCart(datas);
								finish();
							}


							@Override
							public void onFailure(int arg0, String arg1) {

							}
						});

					}
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					logI("��������ʧ��" + arg1);
				}
			});
		}
	}
	//�����ύ��ɾ�����ﳵ�����Ʒ
	private void deleteCart(List<CartBean> datas) {
		for (int i = 0; i < datas.size(); i++) {
			CartBean cartBean = new CartBean();
			cartBean.setObjectId(datas.get(i).getObjectId());
			//ɾ���������еĹ��ﳵ����
			cartBean.delete(getAct(), new DeleteListener() {
				
				@Override
				public void onSuccess() {
					
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					logI(arg1);
				}
			});
		
		}
	}
	
	
	// �������֧��
	private void selectMyyue(int i) {
		if (ub.getYue()<cartprice) {
			toastS("�������㣬��ȥ��ֵ��ѡ��������ʽ����");
			return;
		}
		//���涩��
		saveOrder(i);
	}

	// ����
	@OnClick(R.id.act_commitorder_back)
	public void backOnClick(View v) {
		finish();
	}

	// ���Խ��е�ַ����ʾ
	@Override
	protected void onStart() {
		super.onStart();
		MyAddressBean address = (MyAddressBean) MyApplication.getData("address", true);
		if (address != null) {
			address_str = address.getInfo() + "\n\r\t" + address.getName() + "\t\t" + address.getPhone();
			tvaddress.setText(address_str);
		}

	}
}
