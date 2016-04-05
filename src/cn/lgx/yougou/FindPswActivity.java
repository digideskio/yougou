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
		
		// ��ȡ�ֻ���
		String phone_str = phone.getText().toString().trim();
		// ��֤�ֻ����Ƿ�����
		if (!phone_str.matches("^(17[0|7]|13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")) {
			toastS("�����ֻ�������������");
			return;
		}
		// ��ȡ��֤�����е���ʱ����
		getCodeBut.setClickable(false);
		getCodeBut.setBackgroundResource(R.drawable.getcode_selector);
		getCodeBut.setTextColor(Color.parseColor("#000000"));
		CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
			// ����UI
			@Override
			public void onTick(long millisUntilFinished) {
				getCodeBut.setText(millisUntilFinished / 1000 + "s");
			}

			// ����ʱ���
			@Override
			public void onFinish() {
				getCodeBut.setClickable(true);
				getCodeBut.setBackgroundResource(R.drawable.getcode_selector2);
				getCodeBut.setText("��ȡ��֤��");
				getCodeBut.setTextColor(Color.parseColor("#2adddd"));
			}
		};
		// ��������ʱ
		timer.start();
		// ��������֤��
		BmobSMS.requestSMSCode(getAct(), phone_str, "�Ź���֤��", new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId, BmobException ex) {
				if (ex == null) {// ��֤�뷢�ͳɹ�
					Log.i("smile", "����id��" + smsId);// ���ڲ�ѯ���ζ��ŷ�������
				}
			}
		});
	}
	//�һ�����
	public void findpswOnClick(View v){
		String password = etpsw1.getText().toString().trim();
		String password2 = etpsw2.getText().toString().trim();

		// ��ȡ�ֻ��ź���֤��
		@SuppressWarnings("unused")
		String phone_str = phone.getText().toString().trim();
		String code_str = code.getText().toString().trim();
		
		if (!password.equals(password2)) {
			toastS("�������벻һ�£����������룡");
			return;
		}
		DialogUtils.showDialog(getAct(), null, null, true);
		//�������������
		UserBean.resetPasswordBySMSCode(getAct(), code_str, password, new ResetPasswordByCodeListener() {
			//�������
			@Override
			public void done(cn.bmob.v3.exception.BmobException ex) {
				 if(ex==null){
			            DialogUtils.dismiss();
			            toastS("�������óɹ�");
			            finish();
			        }else{
			        	toastS(ErrorCodeUtils.getErrorMessage(ex.getErrorCode()));
			        	DialogUtils.dismiss();
			        }
			}
		});
	}
	//����
	@OnClick(R.id.act_findpsw_back)
	public void backOnClick(View v){
		finish();
	}
}