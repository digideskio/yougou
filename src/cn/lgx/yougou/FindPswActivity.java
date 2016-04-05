package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ErrorCodeUtils;

public class FindPswActivity extends BaseActivity implements BaseInterface {
	
	@ViewInject(R.id.act_findpsw_password1)
	private EditText etpsw1;
	@ViewInject(R.id.act_findpsw_password2)
	private EditText etpsw2;
	@ViewInject(R.id.act_findpsw_code)
	private EditText code;
	@ViewInject(R.id.act_findpsw_phone)
	private EditText phone;
	@ViewInject(R.id.act_findpsw_getcode)
	private Button getCodeBut;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_findpsw);
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {

	}
	
	@OnClick(R.id.act_findpsw_getcode)
	public void getcodeOnClick(View v){
		
		// 获取手机号
		String phone_str = phone.getText().toString().trim();
		// 验证手机号是否有误
		if (!phone_str.matches("^(17[0|7]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
			toastS("您的手机号码有误，请检查");
			return;
		}
		// 获取验证码后进行倒计时操作
		getCodeBut.setClickable(false);
		getCodeBut.setBackgroundResource(R.drawable.getcode_selector);
		getCodeBut.setTextColor(Color.parseColor("#000000"));
		CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
			// 更新UI
			@Override
			public void onTick(long millisUntilFinished) {
				getCodeBut.setText(millisUntilFinished / 1000 + "s");
			}

			// 倒计时完成
			@Override
			public void onFinish() {
				getCodeBut.setClickable(true);
				getCodeBut.setBackgroundResource(R.drawable.getcode_selector2);
				getCodeBut.setText("获取验证码");
				getCodeBut.setTextColor(Color.parseColor("#2adddd"));
			}
		};
		// 开启倒计时
		timer.start();
		// 请求发送验证码
		BmobSMS.requestSMSCode(getAct(), phone_str, "优购验证码", new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId, BmobException ex) {
				if (ex == null) {// 验证码发送成功
					Log.i("smile", "短信id：" + smsId);// 用于查询本次短信发送详情
				}
			}
		});
	}
	//找回密码
	public void findpswOnClick(View v){
		String password = etpsw1.getText().toString().trim();
		String password2 = etpsw2.getText().toString().trim();

		// 获取手机号和验证码
		@SuppressWarnings("unused")
		String phone_str = phone.getText().toString().trim();
		String code_str = code.getText().toString().trim();
		
		if (!password.equals(password2)) {
			toastS("两次密码不一致，请重新输入！");
			return;
		}
		DialogUtils.showDialog(getAct(), null, null, true);
		//对密码进行重置
		UserBean.resetPasswordBySMSCode(getAct(), code_str, password, new ResetPasswordByCodeListener() {
			//重置完成
			@Override
			public void done(cn.bmob.v3.exception.BmobException ex) {
				 if(ex==null){
			            DialogUtils.dismiss();
			            toastS("密码重置成功");
			            finish();
			        }else{
			        	toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
			        	DialogUtils.dismiss();
			        }
			}
		});
	}
	//返回
	@OnClick(R.id.act_findpsw_back)
	public void backOnClick(View v){
		finish();
	}
}