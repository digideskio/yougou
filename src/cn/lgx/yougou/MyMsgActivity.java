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
		//初始化控件
		ViewUtils.inject(this);
	}

	@Override
	public void initDatas() {
		//获取当前缓存对象
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		//如果已经登陆，进行信息的显示
		tvzhanghao.setText(ub.getUsername());
		//显示生日
		if (ub.getBrithday() != null) {
			tvbrithday.setText(ub.getBrithday());
		}
		//显示年龄
		if (ub.getAge() != null) {
			etage.setText(ub.getAge() + "");
		}
		//显示性别
		if (ub.getSex()!=null) {
			tvSex.setText(ub.getSex());
		}
		
	}
	
	//选择生日
	@OnClick(R.id.act_mymsg_brithday)
	public void getBrithdayOnClick(View v){
		DatePickerDialog date = new DatePickerDialog(getAct(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				//显示选择的生日日期
				tvbrithday.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			}
		}, 1990, 0, 1);
		date.show();
	}
	
	//选择性别
	@OnClick(R.id.act_mymsg_sex)
	public void getSexOnClick(View v){
		String [] items = {"男","女","保密"};
		new AlertDialog.Builder(getAct()).setItems(items, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which==0) {
					tvSex.setText("男");
				}else if (which==1) {
					tvSex.setText("女");
				}else {
					tvSex.setText("保密");
				}
			}
		}).setCancelable(true).show();
		
	}
	//提交资料
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
				toastS("资料修改成功");
		    	finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastS("资料修改失败");
				logI(arg1);
			}
		});
	}
	//返回
	@OnClick(R.id.act_mymsg_back)
	public void backOnClick(View v){
		finish();
	}
}
