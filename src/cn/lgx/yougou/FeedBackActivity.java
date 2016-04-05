package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.utils.IsTvNull;

public class FeedBackActivity extends BaseActivity implements BaseInterface {
	
	@ViewInject(R.id.act_feedback_content)
	private EditText etcontent;
	@ViewInject(R.id.act_feedback_contact)
	private EditText etcontact;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_feedback);
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {

	}

	@Override
	public void initViewOper() {
		
	}
	//�ύ��������
	public void commitOnClik(View v){
		if (!IsTvNull.isNull(etcontact,etcontent)) {
			toastS("����д������Ϣ���Ա����Ǽ�ʱ�Ľ���");
			return;
		}
		etcontact.setText("");
		etcontent.setText("");
		toastS("���ķ����������յ���лл���İ�����");
		finish();
	}
	//����
	@OnClick(R.id.act_feedback_back)
	public void backOnClick(View v){
		finish();
	}
	
}
