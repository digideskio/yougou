package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.bmob.v3.listener.SaveListener;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ErrorCodeUtils;

public class LoginActivity extends BaseActivity implements BaseInterface {

	@ViewInject(R.id.act_login_phone)
	private EditText etPhone;
	@ViewInject(R.id.act_login_password)
	private EditText etPassword;
	private UserBean ub;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_login);
		ViewUtils.inject(this);
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {

	}

	// 返回
	@OnClick(R.id.act_login_back)
	public void BackOnClick(View v) {
		finish();
	}

	// 找回密码
	@OnClick(R.id.act_login_fsw)
	public void FindPswOnClick(View v) {
		startActivity(new Intent(getAct(), FindPswActivity.class));
	}

	// 注册
	@OnClick(R.id.act_login_register)
	public void RegisterOnClick(View v) {
		startActivity(new Intent(getAct(), RegisterActivity.class));
	}

	// 登陆
	public void LoginOnClik(View v) {
		//获取用户信息
		final String phone = etPhone.getText().toString().trim();
		String password = etPassword.getText().toString().trim();
		if ("".equals(phone)||"".equals(password)) {
			toastS("用户名和密码不能为空");
			return;
		}
		DialogUtils.showDialog(getAct(), null, null, true);
		ub = new UserBean();
		ub.setUsername(phone);
		ub.setMobilePhoneNumber(phone);
		ub.setPassword(password);
		//验证登陆
		ub.login(getAct(), new SaveListener() {
			//登陆成功
			@Override
			public void onSuccess() {
				DialogUtils.dismiss();
				MyApplication.putData("uphone", phone);
				finish();
			}
			//登陆失败
			@Override
			public void onFailure(int arg0, String arg1) {
				if (ErrorCodeUtils.getErrorMessage(arg0)!=null) {
					toastS(ErrorCodeUtils.getErrorMessage(arg0));
					DialogUtils.dismiss();
				}
			}
		});
	}
	//注册数据的回显
	@Override
	protected void onStart() {
		super.onStart();
		String phone_Str = (String) MyApplication.getData("uphone", true);
		String password_Str = (String) MyApplication.getData("upassword", true);
		if (phone_Str != null && password_Str != null) {
			etPhone.setText(phone_Str);
			etPassword.setText(password_Str);
		}

	}
}
