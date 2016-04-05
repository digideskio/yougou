package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.lgx.yougou.adapter.GoodsListAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;

public class FenLeiActivity extends BaseActivity implements BaseInterface {

	private List<GoodsBean> datas;
	@ViewInject(R.id.act_fenlei_lv)
	private ListView lv;
	@ViewInject(R.id.act_fenlei_tvtype)
	private TextView tvtitle;
	private String numberkey;
	private String title;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_fenlei);
		ViewUtils.inject(getAct());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initDatas() {
		Intent intent = getIntent();
		//�õ���ѯ����Ʒ����
		datas = (List<GoodsBean>) MyApplication.getData(intent.getStringExtra("key"), true);
		numberkey = intent.getStringExtra("NumberKey");
		//����numberKey�����ж�
		if (numberkey == null) {
			return;
		}
		if ("10".equals(numberkey)) {
			title = intent.getStringExtra("title_Str");
		}
		settitle(numberkey);

	}
	//���ò�ͬ�ı���
	private void settitle(String numberkey) {
		switch (numberkey) {
		case "1":
			tvtitle.setText("�˶�");
			break;
		case "2":
			tvtitle.setText("����");
			break;
		case "3":
			tvtitle.setText("ŮЬ");
			break;
		case "4":
			tvtitle.setText("��Ь");
			break;
		case "5":
			tvtitle.setText("��װ");
			break;
		case "6":
			tvtitle.setText("Ůװ");
			break;
		case "7":
			tvtitle.setText("ͯװ");
			break;
		case "8":
			tvtitle.setText("���");
			break;
		case "9":
			tvtitle.setText("�Ҿ�");
			break;
		case "10":
			tvtitle.setText("��"+title+"���");
			break;
		case "11":
			tvtitle.setText("�����Ƽ�");
			break;
		}
	}

	@Override
	public void initViewOper() {
		//��������Դ
		lv.setAdapter(new GoodsListAdapter(datas, getAct()));
		// ������Ʒ�������
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyApplication.putData("goods", datas.get(position));
				//��ת
				startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
			}
		});

	}
	
	@OnClick(R.id.act_fenlei_back)
	public void backOnClick(View v){
		finish();
	}
}
