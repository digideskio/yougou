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

		// ��ȡ��֤��
		getCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ȡ�ֻ���
				phone = etPhone.getText().toString().trim();
				// ��֤�ֻ����Ƿ�����
				if (!phone.matches("^(17[0|7]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
					toastS("�����ֻ�������������");
					return;
				}
				// ��ȡ��֤�����е���ʱ����
				getCode.setClickable(false);
				getCode.setBackgroundResource(R.drawable.getcode_selector2);
				getCode.setTextColor(Color.parseColor("#000000"));
				CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
					// ����UI
					@Override
					public void onTick(long millisUntilFinished) {
						getCode.setText(millisUntilFinished / 1000 + "s");
					}

					// ����ʱ���
					@Override
					public void onFinish() {
						getCode.setClickable(true);
						getCode.setBackgroundResource(R.drawable.getcode_selector);
						getCode.setText("��ȡ��֤��");
						getCode.setTextColor(Color.parseColor("#ff0000"));
					}
				};
				// ��������ʱ
				timer.start();
				// ��������֤��
				BmobSMS.requestSMSCode(getAct(), phone, "�Ź���֤��", new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {// ������֤������֤�ɹ�
							logI("���ŷ��ͳɹ�������id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
						} else {
							logI("��֤ʧ�ܣ�code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
						}
					}
				});
			}
		});
	}

	public void RegisterOnClick(View view) {
		// ��ȡע���û����û���������
		password = etPassword1.getText().toString().trim();
		password2 = etPassword2.getText().toString().trim();

		// ��ȡ�ֻ��ź���֤��
		phone = etPhone.getText().toString().trim();
		code = etCode.getText().toString().trim();
		if ("".equals(phone) || "".equals(password)) {
			toastS("�û��������벻��Ϊ��");
			return;
		}
		if (!password.equals(password2)) {
			toastS("�������벻һ�£�����������");
			return;
		}
		DialogUtils.showDialog(getAct(), null, null, true);
		// ��֤��֤���Ƿ���ȷ
		BmobSMS.verifySmsCode(getAct(), phone, code, new VerifySMSCodeListener() {
			//��֤���
			@Override
			public void done(BmobException ex) {
				if (ex == null) {//��֤�ɹ�
					UserBean ub = new UserBean();
					//�����û��ֶ�
					ub.setUsername(phone);
					ub.setPassword(password);
					ub.setMobilePhoneNumber(phone);
					ub.setMobilePhoneNumberVerified(true);
					//ע��
					ub.signUp(getAct(), new SaveListener() {
						//ע��ɹ�
						@Override
						public void onSuccess() {
							DialogUtils.dismiss();
							toastS("��ϲ��ע��ɹ�����ȥ��½");
							//ע��ɹ������ݻ���
							MyApplication.putData("uphone", phone);
							MyApplication.putData("upassword", password);
							finish();
						}
						//ע��ʧ��
						@Override
						public void onFailure(int arg0, String arg1) {
							logI(arg1);
							toastS(ErrorCodeUtils.getErrorMessage(arg0));
							DialogUtils.dismiss();
						}
					});

				} else {
					// ������֤ʧ��
					//toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
					logI(ex.getLocalizedMessage());
					DialogUtils.dismiss();
				}
			}
		});
	}
	//����
	@OnClick(R.id.act_register_back)
	public void backOnClick(View v){
		finish();
	}
}
