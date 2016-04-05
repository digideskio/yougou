package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ErrorCodeUtils;
import cn.lgx.yougou.utils.IsTvNull;

public class UpdatePswActivity extends BaseActivity implements BaseInterface {
	@ViewInject(R.id.act_updatepsw_oldpsw)
	private EditText etOldpsw;
	@ViewInject(R.id.act_updatepsw_newpsw1)
	private EditText etNewpsw1;
	@ViewInject(R.id.act_updatepsw_newpsw2)
	private EditText etNewpsw2;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_updatepsw);
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {

	}

	// ȷ���޸�
	public void updateOnClick(View v) {

		String oldpsw_Str = etOldpsw.getText().toString().trim();
		final String newPsw_Str1 = etNewpsw1.getText().toString().trim();
		String newPsw_Str2 = etNewpsw2.getText().toString().trim();

		if (!IsTvNull.isNull(etOldpsw, etNewpsw1, etNewpsw2)) {
			toastS("��Ϣû����д����");
			return;
		}

		if (!newPsw_Str1.equals(newPsw_Str2)) {
			toastS("�������벻һ�£�����������");
			return;
		}
		DialogUtils.showDialog(getAct(), null, "�޸���...", true);
		//����������޸�
		UserBean.updateCurrentUserPassword(getAct(), oldpsw_Str, newPsw_Str1, new UpdateListener() {

			@Override
			public void onSuccess() {
				DialogUtils.dismiss();
				toastS("�����޸ĳɹ�����������������е�¼��");
				MyApplication.putData("uphone", BmobUser.getCurrentUser(getAct(), UserBean.class).getUsername());
				MyApplication.putData("upassword", newPsw_Str1);
				startActivity(new Intent(getAct(), LoginActivity.class));
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				DialogUtils.dismiss();
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}
		});
	}

	// ����
	@OnClick(R.id.act_updatepsw_back)
	public void backOnClick(View v) {
		finish();
	}
}
