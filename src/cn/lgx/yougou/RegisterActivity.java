package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ErrorCodeUtils;

public class RegisterActivity extends BaseActivity implements BaseInterface {
	@ViewInject(R.id.act_register_phone)
	private EditText etPhone;
	@ViewInject(R.id.act_register_password1)
	private EditText etPassword1;
	@ViewInject(R.id.act_register_password2)
	private EditText etPassword2;
	@ViewInject(R.id.act_register_code)
	private EditText etCode;
	@ViewInject(R.id.act_register_getcode)
	private Button getCode;
	private String password, password2, code, phone;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_register);
		ViewUtils.inject(this);
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {

		// 获取验证码
		getCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 获取手机号
				phone = etPhone.getText().toString().trim();
				// 验证手机号是否有误
				if (!phone.matches("^(17[0|7]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastS("您的手机号码有误，请检查");
					return;
				}
				// 获取验证码后进行倒计时操作
				getCode.setClickable(false);
				getCode.setBackgroundResource(R.drawable.getcode_selector2);
				getCode.setTextColor(Color.parseColor("#000000"));
				CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
					// 更新UI
					@Override
					public void onTick(long millisUntilFinished) {
						getCode.setText(millisUntilFinished / 1000 + "s");
					}

					// 倒计时完成
					@Override
					public void onFinish() {
						getCode.setClickable(true);
						getCode.setBackgroundResource(R.drawable.getcode_selector);
						getCode.setText("获取验证码");
						getCode.setTextColor(Color.parseColor("#ff0000"));
					}
				};
				// 开启倒计时
				timer.start();
				// 请求发送验证码
				BmobSMS.requestSMSCode(getAct(), phone, "优购验证码", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// 短信验证码已验证成功
							logI("短信发送成功，短信id：" + smsId);// 用于查询本次短信发送详情
						} else {
							logI("验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
						}
					}
				});
			}
		});
	}

	public void RegisterOnClick(View view) {
		// 获取注册用户的用户名和密码
		password = etPassword1.getText().toString().trim();
		password2 = etPassword2.getText().toString().trim();

		// 获取手机号和验证码
		phone = etPhone.getText().toString().trim();
		code = etCode.getText().toString().trim();
		if ("".equals(phone) || "".equals(password)) {
			toastS("用户名和密码不能为空");
			return;
		}
		if (!password.equals(password2)) {
			toastS("两次密码不一致，请重新输入");
			return;
		}
		DialogUtils.showDialog(getAct(), null, null, true);
		// 验证验证码是否正确
		BmobSMS.verifySmsCode(getAct(), phone, code, new VerifySMSCodeListener() {
			//验证完成
			@Override
			public void done(BmobException ex) {
				if (ex == null) {//验证成功
					UserBean ub = new UserBean();
					//设置用户字段
					ub.setUsername(phone);
					ub.setPassword(password);
					ub.setMobilePhoneNumber(phone);
					ub.setMobilePhoneNumberVerified(true);
					//注册
					ub.signUp(getAct(), new SaveListener() {
						//注册成功
						@Override
						public void onSuccess() {
							DialogUtils.dismiss();
							toastS("恭喜你注册成功，请去登陆");
							//注册成功的数据回显
							MyApplication.putData("uphone", phone);
							MyApplication.putData("upassword", password);
							finish();
						}
						//注册失败
						@Override
						public void onFailure(int arg0, String arg1) {
							logI(arg1);
							toastS(ErrorCodeUtils.getErrorMessage(arg0));
							DialogUtils.dismiss();
						}
					});

				} else {
					// 短信验证失败
					//toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
					logI(ex.getLocalizedMessage());
					DialogUtils.dismiss();
				}
			}
		});
	}
	//返回
	@OnClick(R.id.act_register_back)
	public void backOnClick(View v){
		finish();
	}
}
