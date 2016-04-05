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
		//选择商品的星级的监听
		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				rating_num = rating;//得到选择的星级
			}
		});

	}
	//进行发表评论的操作
	public void fabiaoOnClik(View v) {
		String content = etcontent.getText().toString().trim();
		if (content == null || "".equals(content)) {
			toastS("请输入评价信息！");
			return;
		}
		if (rating_num == -1.0f) {
			rating_num = 0f;
		}
		//设置评论对象的各种字段
		PingLunBean plbean = new PingLunBean();
		plbean.setUserid(ub.getObjectId());
		plbean.setGoodsid(order.getGoodsId());
		plbean.setContent(content);
		plbean.setUsername(ub.getUsername());
		plbean.setRating_num(rating_num);
		//上传服务器
		plbean.save(getAct(), new SaveListener() {
			//保存成功
			@Override
			public void onSuccess() {
				OrderBean norder = new OrderBean();
				norder.setIspinglun(true);
				order.setIspinglun(true);
				//更新订单中是否评论的字段，将为评论改为已评论
				norder.update(getAct(), order.getObjectId(),new UpdateListener() {
					//更新成功
					@Override
					public void onSuccess() {
						toastS("评论成功！");
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
	//返回
	@OnClick(R.id.act_act_pinlun_back)
	public void backOnClick(View v){
		finish();
	}
}
