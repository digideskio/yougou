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

	private Double jine;// 充值金额

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
		// 获得缓存对象
		ub = BmobUser.getCurrentUser(getActivity(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		// 如果有缓存对象，设置账号名
		if (ub != null) {
			// 显示账号
			tvzhanghao.setText(ub.getUsername());
			tvlogin.setVisibility(View.INVISIBLE);
			// 显示余额
			getYue();
		}else {
			tvexit.setVisibility(View.INVISIBLE);
		}
	}

	// 查询当前用户余额进行显示
	private void getYue() {
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", ub.getObjectId());
		query.findObjects(getActivity(), new FindListener<UserBean>() {

			@Override
			public void onSuccess(List<UserBean> arg0) {
				if (arg0.get(0).getYue() == null) {
					tvyue.setText(0 + "元");
				} else {
					tvyue.setText(arg0.get(0).getYue() + "元");
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

	// 登陆操作
	@OnClick(R.id.fragment_my_login)
	public void LoginOnClick(View v) {
		startActivity(new Intent(getActivity(), LoginActivity.class));
	}

	// 登陆成功以后的数据回显
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

	// 修改密码
	@OnClick(R.id.fragment_my_updatepsw)
	public void updatepswOnClick(View v) {
		if (ub != null) {
			startActivity(new Intent(getActivity(), UpdatePswActivity.class));
		}
	}

	// 充值的操作
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
			toastL("请输入充值金额");
			return;
		}
		jine = Double.parseDouble(jine_Str);
		String[] items = { "支付宝", "微信" };
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
				}).setCancelable(true).setTitle("请选择").show();

	}

	// 进行充值
	protected void payByAliorWeixin(boolean flag) {

		DialogUtils.showDialog(getActivity(), null, null, true);
		BP.pay(getActivity(), "优购充值", "充值", 0.02, flag, new PListener() {

			private String orderId;

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				toastS("充值结果未知,请稍后手动查询");
				DialogUtils.dismiss();
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				DialogUtils.dismiss();

				BP.query(getActivity(), orderId, new QListener() {

					@Override
					public void succeed(String arg0) {
						logI("进来了吗？？？？？？");
						UserBean uuBean = new UserBean();

						uuBean.setYue(ub.getYue() + jine);
						ub.setYue(ub.getYue()+jine);
						uuBean.update(getActivity(), ub.getObjectId(), new UpdateListener() {

							@Override
							public void onSuccess() {
								toastS("充值成功！");
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

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				this.orderId = orderId;
				logI(orderId);
			}

			// 支付失败,原因可能是用户中断支付操作,也可能是网络原因
			@Override
			public void fail(int code, String reason) {
				if (code == 6001 || code == -2) {
					toastS("支付已中断");
					DialogUtils.dismiss();
				} else if (code == -3) {
					toastS("监测到你尚未安装支付插件,无法进行微信支付,请选择安装插件(已打包在本地,无流量消耗)");
					DialogUtils.dismiss();
				}
			}
		});
	}

	// 个人中心选项的操作
	@Override
	public void onClick(View v) {
		//若当前用户为null则跳转到登陆界面
		if (ub == null) {
			startActivity(new Intent(getActivity(), LoginActivity.class));
			return;
		}
		switch (v.getId()) {
		//跳转到我的订单界面
		case R.id.fragment_my_dingdlin:
			startActivity(new Intent(getActivity(), MyOrderActivity.class));
			break;
		//跳转到我的地址界面
		case R.id.fragment_my_addresslin:
			startActivity(new Intent(getActivity(), AddressShowActivity.class));
			break;
		// 跳转到个人资料
		case R.id.fragment_my_msglin:
			startActivity(new Intent(getActivity(), MyMsgActivity.class));
			break;
		//跳转到我的收藏界面
		case R.id.fragment_my_lovelin:
			startActivity(new Intent(getActivity(), MyLoveActivity.class));
			break;
		//挑战到反馈建议的界面
		case R.id.act_fragment_my_fkuilin:
			startActivity(new Intent(getActivity(), FeedBackActivity.class));
			break;
		//跳转到我的评论界面
		case R.id.fragment_my_pinlunlin:
			startActivity(new Intent(getActivity(), MyPinglunActivity.class));
			break;
		}
	}

	// 退出
	@OnClick(R.id.fragment_my_exit)
	public void exitOnClick(View v) {
		DialogUtils.showAlertDialog(getActivity(), "警告！", "您确定要退出登录吗？", true, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BmobUser.logOut(getActivity());// 清除缓存用户对象
				getActivity().finish();
			}
		});

	}
}
