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
		//得到购物车里的总价格
		cartprice =  (Double) MyApplication.getData("price", true);
		//得到需要结算的购物车对象集合
		datas = (List<CartBean>) MyApplication.getData("cart", true);
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		//设置价格
		tvprice2.setText(""+cartprice);
		//获得默认地址
		getMorenAddress();
		
	}
	
	//获得默认地址的方法
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
		//设置数据源
		lv.setAdapter(new CommitAdapter(datas, getAct()));
	}

	// 选择配送方式
	@OnClick(R.id.act_commitorder_peisimg)
	public void selectSendTimeOnClick(View v) {
		String items[] = { "周一至周五配送", "配送时间不限", "周六日/节假日配送" };
		new AlertDialog.Builder(getAct()).setItems(items, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					tvtime.setText("周一至周五配送");
				} else if (which == 1) {
					tvtime.setText("配送时间不限");
				} else {
					tvtime.setText("周六日/节假日配送");
				}
			}
		}).setCancelable(true).show();
	}

	// 跳转到地址界面选择地址
	@OnClick(R.id.act_commitorder_address)
	public void selectAddressOnClick(View v) {
		startActivity(new Intent(getAct(), AddressShowActivity.class));
	}

	// 提交订单
	@OnClick(R.id.act_commitorder_commit)
	public void CommitOnClick(View v) {
		
		if (address_str==null) {
			toastS("请选择收货地址");
			return;
		}
		DialogUtils.showAlertDialog(getAct(), "提醒：", "您确定您的订单没有问题吗？", true, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//选择哪种方式付款
				String[] items = { "支付宝", "微信", "我的余额" };
				new AlertDialog.Builder(getAct()).setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							//选择支付宝
							selectAliorWeixin(true);
						} else if (which == 1) {
							//选择微信
							selectAliorWeixin(false);
						} else {
							//选择余额
							selectMyyue(2);
						}
					}

				}).setCancelable(true).show();

			}
		});

	}

	// 利用支付宝或微信支付
	private void selectAliorWeixin(boolean flag) {

		DialogUtils.showDialog(getAct(), null, null, true);
		BP.pay(getAct(), "优购商品", null, 0.02, flag, new PListener() {

			private String order;
			
			@Override
			public void unknow() {
				DialogUtils.dismiss();
				toastS("支付结果未知,请稍后手动查询");
			}
			//支付成功
			@Override
			public void succeed() {
				DialogUtils.dismiss();
				//当查询到订单的时候保存订单
				BP.query(getAct(), order, new QListener() {

					@Override
					public void succeed(String arg0) {
						//保存订单
						saveOrder(0);
					}

					@Override
					public void fail(int arg0, String arg1) {
						DialogUtils.dismiss();
						logI("订单查询失败：" + arg1);
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
					toastS("支付已中断");
					DialogUtils.dismiss();
				} else if (arg0 == -3) {
					toastS("支付异常");
				}
			}
		});

	}
	//保存订单的方法
	private void saveOrder(final int i) {
		//设置所有订单字段
		for (int j = 0; j < datas.size(); j++) {
			
			final CartBean cart = datas.get(j);
			final GoodsBean gBean = new GoodsBean();
			//销量增加
			gBean.increment("salesNum", cart.getNumber());
			OrderBean oBean = new OrderBean();
			//设置订单
 			oBean.setAddress(address_str);//设置订单地址
			oBean.setColor(cart.getColor());//设置订单颜色
			oBean.setSize(cart.getSize());//设置订单尺寸
			oBean.setNumber(cart.getNumber());//设置订单数量
			oBean.setGoodsId(cart.getGoodsId());//设置商品ID
			oBean.setFileUrl(cart.getFileUrl());//设置商品图片的Url
			oBean.setOrderinfo(cart.getGoodsinfo());//设置订单详情
			oBean.setUserid(ub.getObjectId());
			oBean.setIspinglun(false);
			oBean.setIsshouhuo(false);
			oBean.setPrice(cart.getPrice()*cart.getNumber());
			//保存订单，上传服务器
			oBean.save(getAct(), new SaveListener() {
			
				@Override
				public void onSuccess() {
					gBean.update(getAct(), cart.getGoodsId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							if (i == 0) {
								toastS("支付成功！");
								deleteCart(datas);
								finish();
							}
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							toastS("支付失败:" + arg1);
						}
					});
					if (i == 2) {
						UserBean uu = new UserBean();
						logI(ub.getYue() + "");
						//服务器余额家去订单的总价格
						uu.setYue(ub.getYue() - cart.getPrice());
						//本地
						ub.setYue(ub.getYue() - cart.getPrice());
						uu.update(getAct(), ub.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								toastS("支付成功！");
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
					logI("订单保存失败" + arg1);
				}
			});
		}
	}
	//订单提交后删除购物车里的商品
	private void deleteCart(List<CartBean> datas) {
		for (int i = 0; i < datas.size(); i++) {
			CartBean cartBean = new CartBean();
			cartBean.setObjectId(datas.get(i).getObjectId());
			//删除服务器中的购物车对象
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
	
	
	// 利用余额支付
	private void selectMyyue(int i) {
		if (ub.getYue()<cartprice) {
			toastS("您的余额不足，请去充值或选用其它方式付款");
			return;
		}
		//保存订单
		saveOrder(i);
	}

	// 返回
	@OnClick(R.id.act_commitorder_back)
	public void backOnClick(View v) {
		finish();
	}

	// 回显进行地址的显示
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
