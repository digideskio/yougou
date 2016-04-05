package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.adapter.MyOrderAdapter;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.OrderBean;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;

public class MyOrderActivity extends BaseActivity implements BaseInterface {
	@ViewInject(R.id.act_myorder_lv)
	private ListView lv;
	private MyOrderAdapter adapter;
	private UserBean ub;
	private List<OrderBean> orderlist;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_myorder);
		ViewUtils.inject(getAct());
	}
	//得到用户所有的订单
	@Override
	public void initDatas() {
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		BmobQuery<OrderBean> query = new BmobQuery<OrderBean>();
		query.addWhereEqualTo("userid", ub.getObjectId());
		query.order("-createdAt");
		query.findObjects(getAct(), new FindListener<OrderBean>() {
			
			@Override
			public void onSuccess(List<OrderBean> datas) {
				if (datas==null||"".equals(datas)) {
					return;
				}
				orderlist = datas;
				adapter = new MyOrderAdapter(datas, getAct());
				//设置数据源
				lv.setAdapter(adapter);
				//长按进行删除订单的操作
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
						DialogUtils.showAlertDialog(getAct(), "警告！", "您确定要删除您的订单吗？", true, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								OrderBean orderBean = new OrderBean();
								orderBean.setObjectId(orderlist.get(position).getObjectId());
								//更新服务器
								orderBean.delete(getAct(), new DeleteListener() {
									
									@Override
									public void onSuccess() {
										toastS("删除成功！");
										initDatas();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										toastS("删除失败:"+arg1);
									}
								});
							
							}
						});
						
						return true;
					}
				});
				
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
		
		
	}

	@Override
	public void initViewOper() {
		
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		initDatas();
	}
	//返回
	@OnClick(R.id.act_myorder_back)
	public void backOnClick(View v){
		finish();
	}
}
