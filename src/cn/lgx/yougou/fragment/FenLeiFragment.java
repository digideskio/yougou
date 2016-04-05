package cn.lgx.yougou.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.FenLeiActivity;
import cn.lgx.yougou.R;
import cn.lgx.yougou.ShowGoodsListActivity;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseFragment;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.utils.DialogUtils;
import cn.lgx.yougou.utils.TextUtils;

public class FenLeiFragment extends BaseFragment implements BaseInterface {
	
	private String numberkey ="";
	@ViewInject(R.id.fragment_fenlei_et)
	private EditText etinfo;
	
	@SuppressLint("InflateParams")
	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_fenlei, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		initDatas();
		initViewOper();

	}

	@Override
	public void initDatas() {
		
	}

	@Override
	public void initViewOper() {
		
	}
	
	//搜索操作 复合查询，可根据商品编号和商品相关内容搜索
	@OnClick(R.id.fragment_fenlei_sousuo)
	public void searchInfoOnClick(View v){
		
		final String info = etinfo.getText().toString().trim();
		if (info==null||"".equals(info)) {
			toastS("请输入检索数据");
			return;
		}
		numberkey = "10";
		BmobQuery<GoodsBean> eq1 = new BmobQuery<GoodsBean>();
		eq1.addWhereEqualTo("number", info);
		BmobQuery<GoodsBean> eq2 = new BmobQuery<GoodsBean>();
		eq2.addWhereContains("info", info);
		List<BmobQuery<GoodsBean>> queries = new ArrayList<BmobQuery<GoodsBean>>();
		queries.add(eq1);
		queries.add(eq2);
		BmobQuery<GoodsBean> query = new BmobQuery<GoodsBean>();
		query.or(queries);
		query.findObjects(getActivity(), new FindListener<GoodsBean>() {
			
			@Override
			public void onSuccess(List<GoodsBean> data) {
				if (data != null && data.size() > 0) {
					String key = "findGoodsBean";
					// 检索数据
					MyApplication.putData(key, data);
					// 跳转界面
					Intent intent = new Intent(getActivity(), FenLeiActivity.class);
					
					intent.putExtra("key", key);
					intent.putExtra("NumberKey", numberkey);
					intent.putExtra("title_Str", TextUtils.textLengthMax(info));
					
					etinfo.setText("");
					startActivity(intent);
				} else {
					toastS("对不起，没有相关内容");
					return;
				}
			}
		
			@Override
			public void onError(int arg0, String arg1) {
				
			}
		});
		
	}
	
	@Override
	public void initViews() {

	}
	//不同分类的监听
	@OnClick({ R.id.fragment_fenlei_yudong, R.id.fragment_fenlei_huwai, R.id.fragment_fenlei_nvxie,
			R.id.fragment_fenlei_nanxie, R.id.fragment_fenlei_nanzhuang, R.id.fragment_fenlei_nvzhuang,
			R.id.fragment_fenlei_tongz, R.id.fragment_fenlei_xiangb, R.id.fragment_fenlei_jiaju ,R.id.fragment_fenlei_tuijian})
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_fenlei_yudong:
			numberkey = "1";
			getdatas("运动");
			break;
		case R.id.fragment_fenlei_huwai:
			numberkey = "2";
			getdatas("户外");
			break;
		case R.id.fragment_fenlei_nvxie:
			numberkey = "3";
			getdatas("女鞋");
			break;
		case R.id.fragment_fenlei_nanxie:
			numberkey = "4";
			getdatas("男鞋");
			break;
		case R.id.fragment_fenlei_nanzhuang:
			numberkey = "5";
			getdatas("男装");
			break;
		case R.id.fragment_fenlei_nvzhuang:
			numberkey = "6";
			getdatas("女装");
			break;
		case R.id.fragment_fenlei_tongz:
			numberkey = "7";
			getdatas("童装");
			break;
		case R.id.fragment_fenlei_xiangb:
			numberkey = "8";
			getdatas("箱包");
			break;
		case R.id.fragment_fenlei_jiaju:
			numberkey = "9";
			getdatas("家居");
			break;
		case R.id.fragment_fenlei_tuijian:
			numberkey = "11";
			getdatas("热门推荐");
			break;
		}
	}
	//得到搜索商品的数据源
	private void getdatas(String string) {
		
		BmobQuery<GoodsBean> query = new BmobQuery<GoodsBean>();
		query.addWhereEqualTo("type", string);
		DialogUtils.showDialog(getActivity(), null, null, true);
		query.findObjects(getActivity(), new FindListener<GoodsBean>() {
			
			@Override
			public void onSuccess(List<GoodsBean> arg0) {
				DialogUtils.dismiss();
				if (arg0 != null && arg0.size() > 0) {
					String key = "findGoodsBean";
					// 检索数据
					MyApplication.putData(key, arg0);
					// 跳转界面
					Intent intent = new Intent(getActivity(), FenLeiActivity.class);
					intent.putExtra("key", key);
					intent.putExtra("NumberKey", numberkey);
					startActivity(intent);
				} else {
					toastS("对不起，没有相关内容");
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
					DialogUtils.dismiss();
			}
		});
	}

}
