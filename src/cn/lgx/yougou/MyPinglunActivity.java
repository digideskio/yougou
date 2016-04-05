package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.adapter.GoodsPingLunAdapter;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.PingLunBean;
import cn.lgx.yougou.bean.UserBean;

public class MyPinglunActivity extends BaseActivity implements BaseInterface {
	
	@ViewInject(R.id.act_mypinglun_lv)
	private ListView lv;
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
		setContentView(R.layout.act_mypinglun);
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		
		BmobQuery<PingLunBean> query = new BmobQuery<PingLunBean>();
		query.addWhereEqualTo("userid", ub.getObjectId());
		query.order("-createdAt");
		query.findObjects(getAct(), new FindListener<PingLunBean>() {
			
			@Override
			public void onSuccess(List<PingLunBean> arg0) {
				if (arg0==null) {
					toastS("您还没有评论信息");
					return;
				}
				lv.setAdapter(new GoodsPingLunAdapter(arg0, getAct()));
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
		
	}

	@Override
	public void initViewOper() {

	}
	//返回
	@OnClick(R.id.act_mypinglun_back)
	public void backOnClick(View v){
		finish();
	}
}
