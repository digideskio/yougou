package cn.lgx.yougou;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;

public class InitActivity extends BaseActivity implements BaseInterface {
	
	private ImageView imganim;
	private Animation anim;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_init);
		imganim = imgByid(R.id.act_init_anim);
	}

	@Override
	public void initDatas() {
		anim = AnimationUtils.loadAnimation(getAct(), R.anim.init_alpha);
	}

	@Override
	public void initViewOper() {
		imganim.setAnimation(anim);
		//���ö����ļ���
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			//��������������ת
			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = null;
				intent = new Intent(getAct(), HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}

}
