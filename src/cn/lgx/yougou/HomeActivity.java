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
			// 给LinerLayout设置监听切换界面
			linears[i].setOnClickListener(this);
		}

	}

	@Override
	public void initDatas() {
		// 准备fragment数据源
		frags = new ArrayList<>();
		frags.add(new HomeFragment());
		frags.add(new FenLeiFragment());
		frags.add(new CartFragment());
		frags.add(new MyFragment());

		// 获取Fragment的管理工具
		manager = getSupportFragmentManager();
		// 开启事务
		beginTransaction = manager.beginTransaction();
		// 添加一个默认的fragment到Fargment上
		beginTransaction.add(R.id.act_home_frame, frags.get(0));
		// 提交事务;
		beginTransaction.commit();

	}

	@Override
	public void initViewOper() {

	}

	@Override
	public void onClick(View v) {
		// 开启事务
		beginTransaction = manager.beginTransaction();
		switch (v.getId()) {
		case R.id.act_home_linhome:
			// 改变字体颜色和图标
			updateView(0);
			// 替换界面
			beginTransaction.replace(R.id.act_home_frame, frags.get(0));
			break;
		case R.id.act_home_linfenlei:
			// 改变字体颜色和图标
			updateView(1);
			// 替换界面
			beginTransaction.replace(R.id.act_home_frame, frags.get(1));
			break;
		case R.id.act_home_lingwuc:
			// 改变字体颜色和图标
			updateView(2);
			// 替换界面
			beginTransaction.replace(R.id.act_home_frame, frags.get(2));
			break;
		case R.id.act_home_linmy:
			// 改变字体颜色和图标
			updateView(3);
			// 替换界面
			beginTransaction.replace(R.id.act_home_frame, frags.get(3));
			break;
		}
		// 提交事务
		beginTransaction.commit();
	}

	// 改变字体颜色和图标
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

	//监听返回键退出应用
		@Override
		public void onBackPressed() {
				
			if (flag ) {
				super.onBackPressed();
				
			}else {
				toastS("再按一次返回键到桌面");
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
