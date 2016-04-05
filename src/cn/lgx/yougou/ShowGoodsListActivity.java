package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.adapter.GoodsListAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.application.MyApplication.FindDatasListener;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.view.XListView;
import cn.lgx.yougou.view.XListView.IXListViewListener;

public class ShowGoodsListActivity extends BaseActivity implements BaseInterface {

	private GoodsListAdapter adapter;
	private XListView lv;
	private List<GoodsBean> goodsdatas;
	@ViewInject(R.id.act_goodslist_tvtype)
	private TextView tvtype;
	private List<GoodsBean> datas;
	private int numkey = 0;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		getDatas();
		changeTvtype(numkey);
	}
	//��ȡ��ͬ������Դ
	@SuppressWarnings("unchecked")
	private void getDatas() {
		//����������Դ
		datas = (List<GoodsBean>) MyApplication.getData(getIntent().getStringExtra("key"), true);
		if (datas != null) {
			intent = getIntent();
			numkey = intent.getIntExtra("NumberKey", -1);
			adapter = new GoodsListAdapter(datas, getAct());
			lv.setAdapter(adapter);
			// ������Ʒ�������
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					try {
						MyApplication.putData("goods", datas.get(position - 1));
						startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
					} catch (Exception e) {
						toastS("�������磡");
					}
				}
			});
			return;
		} else {
			//��ҳ��������Դ
			((MyApplication) (getApplication())).initData();
			getGoodsBean();
			numkey = (int) MyApplication.getData("type", true);
		}
	}

	@SuppressWarnings("unchecked")
	private void getGoodsBean() {
		goodsdatas = (List<GoodsBean>) MyApplication.getData("findDatasKey", true);
		((MyApplication) getApplication()).setFinddataslistener(new FindDatasListener() {

			@Override
			public void onSuccess(List<GoodsBean> datas) {
				if (datas == null) {
					goodsdatas = (List<GoodsBean>) MyApplication.getData("skidDatasKey", true);
					lv.stopLoadMore();//ֹͣ���¼���
					if (adapter == null) {
						initDatas();
						initViewOper();
					}
					adapter.addData(goodsdatas);//��������Դ
				} else {
					goodsdatas = datas;
					if (adapter == null) {
						initDatas();
						initViewOper();
					} else {
						adapter.setData(goodsdatas);
						lv.stopRefresh();//ֹͣ����ˢ��
					}
				}
			}
		});
		if (goodsdatas != null) {
			initDatas();
			initViewOper();
		}
	}
	//��ʼ�ؼ�
	@Override
	public void initViews() {
		setContentView(R.layout.act_goodslist);
		ViewUtils.inject(getAct());
		lv = (XListView) findViewById(R.id.act_goodslist_lv);
	}
	//�ı����
	private void changeTvtype(int numkey) {
		switch (numkey) {
		case 1:
			tvtype.setText("������Ʒ");
			break;
		case 2:
			tvtype.setText("���Ź�");
			break;
		case 3:
			tvtype.setText("�ۿ�ר��");
			break;
		case 4:
			tvtype.setText("ר��ͬ��");
			break;
		case 5:
			tvtype.setText("�׶���ѡ");
			break;
		case 6:
			tvtype.setText("���մ���");
			break;
		case 7:
			tvtype.setText("��������");
			break;
		case 8:
			tvtype.setText("������Ʒ");
			break;
		case 9:
			tvtype.setText("��"+intent.getStringExtra("title_Str")+"���");
			break;
		case 10:
			tvtype.setText("��Ʒ�б�");
			break;
		}

	}

	@Override
	public void initDatas() {
		adapter = new GoodsListAdapter(goodsdatas, getAct());
	}

	@Override
	public void initViewOper() {
		//��������Դ
		lv.setAdapter(adapter);
		//���ÿ����¼���
		lv.setPullLoadEnable(true);
		lv.setXListViewListener(new IXListViewListener() {
			// ����ˢ�²���
			@Override
			public void onRefresh() {
				((MyApplication) getApplication()).initData();
			}

			// ���¼�������
			@Override
			public void onLoadMore() {
				((MyApplication) getApplication()).onLoadMore();
			}
		});
		// ������Ʒ�������
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				try {
					MyApplication.putData("goods", goodsdatas.get(position - 1));
					startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
				} catch (Exception e) {
					toastS("�������磡");
				}
			}
		});

	}

	// ����
	@OnClick(R.id.act_goodslist_back)
	public void backOnClick(View v) {
		tvtype.setText("��Ʒ�б�");
		finish();
	}
}
