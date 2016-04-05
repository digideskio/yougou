package cn.lgx.yougou;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.OrderBean;
import cn.lgx.yougou.bean.PingLunBean;
import cn.lgx.yougou.bean.UserBean;

public class PingLunActivity extends BaseActivity implements BaseInterface {

	private OrderBean order;
	private UserBean ub;

	@ViewInject(R.id.act_pinglun_goodsinfo)
	private TextView tvinfo;
	@ViewInject(R.id.act_pinglun_content)
	private EditText etcontent;
	@ViewInject(R.id.act_pinglun_ratingbar)
	private RatingBar rating;

	private float rating_num = -1f;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_pinglun);
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {
		order = (OrderBean) MyApplication.getData("order", true);
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
	}

	@Override
	public void initViewOper() {
		tvinfo.setText(order.getOrderinfo());
		//ѡ����Ʒ���Ǽ��ļ���
		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_num = rating;//�õ�ѡ����Ǽ�
			}
		});

	}
	//���з������۵Ĳ���
	public void fabiaoOnClik(View v) {
		String content = etcontent.getText().toString().trim();
		if (content == null || "".equals(content)) {
			toastS("������������Ϣ��");
			return;
		}
		if (rating_num == -1.0f) {
			rating_num = 0f;
		}
		//�������۶���ĸ����ֶ�
		PingLunBean plbean = new PingLunBean();
		plbean.setUserid(ub.getObjectId());
		plbean.setGoodsid(order.getGoodsId());
		plbean.setContent(content);
		plbean.setUsername(ub.getUsername());
		plbean.setRating_num(rating_num);
		//�ϴ�������
		plbean.save(getAct(), new SaveListener() {
			//����ɹ�
			@Override
			public void onSuccess() {
				OrderBean norder = new OrderBean();
				norder.setIspinglun(true);
				order.setIspinglun(true);
				//���¶������Ƿ����۵��ֶΣ���Ϊ���۸�Ϊ������
				norder.update(getAct(), order.getObjectId(),new UpdateListener() {
					//���³ɹ�
					@Override
					public void onSuccess() {
						toastS("���۳ɹ���");
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						logI(arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {

			}
		});
	}
	//����
	@OnClick(R.id.act_act_pinlun_back)
	public void backOnClick(View v){
		finish();
	}
}
