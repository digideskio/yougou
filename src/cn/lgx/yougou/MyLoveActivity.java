package cn.lgx.yougou;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.adapter.GoodsListAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.bean.UserBean;

public class MyLoveActivity extends BaseActivity implements BaseInterface {
	
	@ViewInject(R.id.act_mylove_lv)
	private ListView lv;
	private UserBean ub;
	private List<String> LovegoodsId;
	private String goodsid;
	private List<GoodsBean> goodsdata;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_mylove);
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {
		//根据用户ID查询用户收藏的商品
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", ub.getObjectId());
		
		query.findObjects(getAct(), new FindListener<UserBean>() {
			
			@Override
			public void onSuccess(List<UserBean> arg0) {
				logI(arg0.get(0).getUsername());
				LovegoodsId = arg0.get(0).getLovegoodsId();
				if (LovegoodsId==null) {
					toastS("您当前没有收藏");
					return;
				}
				
				//复合查询
				List<BmobQuery<GoodsBean>> queries = new ArrayList<BmobQuery<GoodsBean>>();
				//填充条件
				for (int i = 0; i < LovegoodsId.size(); i++) {
					goodsid = LovegoodsId.get(i);
					BmobQuery<GoodsBean> eq = new BmobQuery<GoodsBean>();
					eq.addWhereEqualTo("objectId", goodsid);
					queries.add(eq);
				}
				//得到用户所有收藏的商品
				BmobQuery<GoodsBean> mainquery = new BmobQuery<GoodsBean>();
				mainquery.or(queries);
				mainquery.findObjects(getAct(), new FindListener<GoodsBean>() {

					@Override
					public void onError(int arg0, String arg1) {
						
					}

					@Override
					public void onSuccess(List<GoodsBean> arg0) {
						
						goodsdata = arg0;
						lv.setAdapter(new GoodsListAdapter(arg0, getAct()));
						// 进入商品详情界面
						lv.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								
								MyApplication.putData("goods", goodsdata.get(position));
								startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
							}
						});
					
					}
				});
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				logI(arg1);
			}
		});
		
	}

	@Override
	public void initViewOper() {
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initDatas();
	}
	
	@OnClick(R.id.act_mylove_back)
	public void backOnCick(View v){
		finish();
	}
}
