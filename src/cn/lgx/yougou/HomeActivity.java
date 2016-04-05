package cn.lgx.yougou;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.fragment.CartFragment;
import cn.lgx.yougou.fragment.FenLeiFragment;
import cn.lgx.yougou.fragment.HomeFragment;
import cn.lgx.yougou.fragment.MyFragment;

public class HomeActivity extends BaseActivity implements BaseInterface, OnClickListener {

	private List<Fragment> frags;
	private LinearLayout[] linears = new LinearLayout[4];
	private int[] lineIds = { R.id.act_home_linhome, R.id.act_home_linfenlei, R.id.act_home_lingwuc,
			R.id.act_home_linmy };
	private ImageView[] imgs = new ImageView[4];
	private int[] imgIds = { R.id.act_home_home, R.id.act_home_fenlei, R.id.act_home_gowuche, R.id.act_home_my };
	private TextView[] tvs = new TextView[4];
	private int[] tvIds = { R.id.act_home_tvhome, R.id.act_home_tvfenlei, R.id.act_home_tvgwc, R.id.act_home_tvmy };

	private int[] imgon = { R.drawable.home, R.drawable.fenlei, R.drawable.gowuche, R.drawable.my };
	private int[] imgoff = { R.drawable.home1, R.drawable.fenlei1, R.drawable.gowuche1, R.drawable.my1 };
	private FragmentManager manager;
	private FragmentTransaction beginTransaction;
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_home);

		for (int i = 0; i < 4; i++) {
			linears[i] = linByid(lineIds[i]);
			imgs[i] = imgByid(imgIds[i]);
			tvs[i] = tvByid(tvIds[i]);
			// ��LinerLayout���ü����л�����
			linears[i].setOnClickListener(this);
		}

	}

	@Override
	public void initDatas() {
		// ׼��fragment����Դ
		frags = new ArrayList<>();
		frags.add(new HomeFragment());
		frags.add(new FenLeiFragment());
		frags.add(new CartFragment());
		frags.add(new MyFragment());

		// ��ȡFragment�Ĺ�����
		manager = getSupportFragmentManager();
		// ��������
		beginTransaction = manager.beginTransaction();
		// ���һ��Ĭ�ϵ�fragment��Fargment��
		beginTransaction.add(R.id.act_home_frame, frags.get(0));
		// �ύ����;
		beginTransaction.commit();

	}

	@Override
	public void initViewOper() {

	}

	@Override
	public void onClick(View v) {
		// ��������
		beginTransaction = manager.beginTransaction();
		switch (v.getId()) {
		case R.id.act_home_linhome:
			// �ı�������ɫ��ͼ��
			updateView(0);
			// �滻����
			beginTransaction.replace(R.id.act_home_frame, frags.get(0));
			break;
		case R.id.act_home_linfenlei:
			// �ı�������ɫ��ͼ��
			updateView(1);
			// �滻����
			beginTransaction.replace(R.id.act_home_frame, frags.get(1));
			break;
		case R.id.act_home_lingwuc:
			// �ı�������ɫ��ͼ��
			updateView(2);
			// �滻����
			beginTransaction.replace(R.id.act_home_frame, frags.get(2));
			break;
		case R.id.act_home_linmy:
			// �ı�������ɫ��ͼ��
			updateView(3);
			// �滻����
			beginTransaction.replace(R.id.act_home_frame, frags.get(3));
			break;
		}
		// �ύ����
		beginTransaction.commit();
	}

	// �ı�������ɫ��ͼ��
	private void updateView(int i) {
		for (int j = 0; j < 4; j++) {
			if (j == i) {
				imgs[j].setImageResource(imgon[j]);
				tvs[j].setTextColor(Color.parseColor("#ff0000"));
			} else {
				imgs[j].setImageResource(imgoff[j]);
				tvs[j].setTextColor(Color.parseColor("#828282"));
			}
		}
	}

	//�������ؼ��˳�Ӧ��
		@Override
		public void onBackPressed() {
				
			if (flag ) {
				super.onBackPressed();
				
			}else {
				toastS("�ٰ�һ�η��ؼ�������");
				flag = true;
				CountDownTimer timer = new CountDownTimer(3000,1000) {
					
					@Override
					public void onTick(long millisUntilFinished) {
					}
					@Override
					public void onFinish() {
						flag = false;
					}
				};
				timer.start();
			}
		}
	
}
