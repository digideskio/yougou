package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.adapter.AddressAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.MyAddressBean;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.ErrorCodeUtils;

public class AddressShowActivity extends BaseActivity implements BaseInterface {
	
	@ViewInject(R.id.act_address_lv)
	private ListView lv;
	private AddressAdapter adapter;
	private UserBean ub;
	private List<MyAddressBean> datas;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}
	
	@Override
	public void initViews() {
		setContentView(R.layout.act_address);
		//初始化控件
		ViewUtils.inject(getAct());
	}

	@Override
	public void initDatas() {
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		getMyAddress();
		
	}
	//通过查询得到用户的所有地址
	private void getMyAddress() {
		BmobQuery<MyAddressBean> query = new BmobQuery<MyAddressBean>();
		query.addWhereEqualTo("uid", ub.getObjectId());
		query.findObjects(getAct(), new FindListener<MyAddressBean>() {
			
			@Override
			public void onSuccess(List<MyAddressBean> arg0) {
				if (arg0 == null||arg0.size()==0) {
					toastS("您还没有数据");
					return;
				}
				adapter = new AddressAdapter(arg0, getAct());
				datas = arg0;
				lv.setAdapter(adapter);
				//选择收获地址的操作
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MyApplication.putData("address", datas.get(position));
						finish();
					}
				});
				//删除地址的操作
				lv.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
						DialogUtils.showAlertDialog(getAct(), "警告", "您确定删除该条地址吗？", true, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MyAddressBean bean = new MyAddressBean();
								bean.setObjectId(datas.get(position).getObjectId());
								//删除服务器中的地址
								bean.delete(getAct(), new DeleteListener() {
									
									@Override
									public void onSuccess() {
										toastS("删除成功");
										getMyAddress();
									}
									
									@Override
									public void onFailure(int arg0, String arg1) {
										toastS("删除失败");
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
				toastS(ErrorCodeUtils.getErrorMessage(arg0));
			}
		});
		
	}

	@Override
	public void initViewOper() {
		
	}
	
	//返回
	@OnClick(R.id.act_address_back)
	public void backOnClick(View v){
		finish();
	}
	//增加地址的跳转
	@OnClick(R.id.act_address_add)
	public void addOnClick(View v){
		startActivity(new Intent(getAct(), AddAddressActivity.class));
	}
	//重新获得地址
	@Override
	protected void onRestart() {
		super.onRestart();
		getMyAddress();
	}
}
