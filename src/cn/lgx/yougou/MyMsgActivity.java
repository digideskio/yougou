package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.UserBean;

public class MyMsgActivity extends BaseActivity implements BaseInterface {
	@ViewInject(R.id.act_mymsg_brithday)
	private TextView tvbrithday;
	@ViewInject(R.id.act_mymsg_age)
	private EditText etage;
	@ViewInject(R.id.act_mymsg_zhao)
	private TextView tvzhanghao;
	@ViewInject(R.id.act_mymsg_sex)
	private TextView tvSex;
	private String sex;
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
		setContentView(R.layout.act_mymsg);
		//��ʼ���ؼ�
		ViewUtils.inject(this);
	}

	@Override
	public void initDatas() {
		//��ȡ��ǰ�������
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		//����Ѿ���½��������Ϣ����ʾ
		tvzhanghao.setText(ub.getUsername());
		//��ʾ����
		if (ub.getBrithday() != null) {
			tvbrithday.setText(ub.getBrithday());
		}
		//��ʾ����
		if (ub.getAge() != null) {
			etage.setText(ub.getAge() + "");
		}
		//��ʾ�Ա�
		if (ub.getSex()!=null) {
			tvSex.setText(ub.getSex());
		}
		
	}
	
	//ѡ������
	@OnClick(R.id.act_mymsg_brithday)
	public void getBrithdayOnClick(View v){
		DatePickerDialog date = new DatePickerDialog(getAct(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				//��ʾѡ�����������
				tvbrithday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			}
		}, 1990, 0, 1);
		date.show();
	}
	
	//ѡ���Ա�
	@OnClick(R.id.act_mymsg_sex)
	public void getSexOnClick(View v){
		String [] items = {"��","Ů","����"};
		new AlertDialog.Builder(getAct()).setItems(items, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which==0) {
					tvSex.setText("��");
				}else if (which==1) {
					tvSex.setText("Ů");
				}else {
					tvSex.setText("����");
				}
			}
		}).setCancelable(true).show();
		
	}
	//�ύ����
	public void referOnClick(View v){
		UserBean newuser = new UserBean();
		
		String age = etage.getText().toString().trim();
		sex = tvSex.getText().toString().trim();
		
		if (age!=null&&!"".equals(age)) {
			newuser.setAge(Integer.parseInt(age));
		}
		newuser.setSex(sex);
		newuser.setBrithday(tvbrithday.getText().toString().trim());
		newuser.update(getAct(), ub.getObjectId(), new UpdateListener() {
			
			@Override
			public void onSuccess() {
				toastS("�����޸ĳɹ�");
		    	finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastS("�����޸�ʧ��");
				logI(arg1);
			}
		});
	}
	//����
	@OnClick(R.id.act_mymsg_back)
	public void backOnClick(View v){
		finish();
	}
}
