package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.adapter.GoodsListAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.application.MyApplication.FindDatasListener;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.view.XListView;
import cn.lgx.yougou.view.XListView.IXListViewListener;

public class ShowGoodsListActivity extends BaseActivity implements BaseInterface {

	private GoodsListAdapter adapter;
	private XListView lv;
	private List<GoodsBean> goodsdatas;
	@ViewInject(R.id.act_goodslist_tvtype)
	private TextView tvtype;
	private List<GoodsBean> datas;
	private int numkey = 0;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initViews();
		getDatas();
		changeTvtype(numkey);
	}
	//获取不同的数据源
	@SuppressWarnings("unchecked")
	private void getDatas() {
		//检索的数据源
		datas = (List<GoodsBean>) MyApplication.getData(getIntent().getStringExtra("key"), true);
		if (datas != null) {
			intent = getIntent();
			numkey = intent.getIntExtra("NumberKey", -1);
			adapter = new GoodsListAdapter(datas, getAct());
			lv.setAdapter(adapter);
			// 进入商品详情界面
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					try {
						MyApplication.putData("goods", datas.get(position - 1));
						startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
					} catch (Exception e) {
						toastS("请检查网络！");
					}
				}
			});
			return;
		} else {
			//首页来的数据源
			((MyApplication) (getApplication())).initData();
			getGoodsBean();
			numkey = (int) MyApplication.getData("type", true);
		}
	}

	@SuppressWarnings("unchecked")
	private void getGoodsBean() {
		goodsdatas = (List<GoodsBean>) MyApplication.getData("findDatasKey", true);
		((MyApplication) getApplication()).setFinddataslistener(new FindDatasListener() {

			@Override
			public void onSuccess(List<GoodsBean> datas) {
				if (datas == null) {
					goodsdatas = (List<GoodsBean>) MyApplication.getData("skidDatasKey", true);
					lv.stopLoadMore();//停止向下加载
					if (adapter == null) {
						initDatas();
						initViewOper();
					}
					adapter.addData(goodsdatas);//设置数据源
				} else {
					goodsdatas = datas;
					if (adapter == null) {
						initDatas();
						initViewOper();
					} else {
						adapter.setData(goodsdatas);
						lv.stopRefresh();//停止上拉刷新
					}
				}
			}
		});
		if (goodsdatas != null) {
			initDatas();
			initViewOper();
		}
	}
	//初始控件
	@Override
	public void initViews() {
		setContentView(R.layout.act_goodslist);
		ViewUtils.inject(getAct());
		lv = (XListView) findViewById(R.id.act_goodslist_lv);
	}
	//改变标题
	private void changeTvtype(int numkey) {
		switch (numkey) {
		case 1:
			tvtype.setText("当季新品");
			break;
		case 2:
			tvtype.setText("聚优购");
			break;
		case 3:
			tvtype.setText("折扣专区");
			break;
		case 4:
			tvtype.setText("专柜同款");
			break;
		case 5:
			tvtype.setText("首尔精选");
			break;
		case 6:
			tvtype.setText("今日大牌");
			break;
		case 7:
			tvtype.setText("手气不错");
			break;
		case 8:
			tvtype.setText("人气单品");
			break;
		case 9:
			tvtype.setText("与"+intent.getStringExtra("title_Str")+"相关");
			break;
		case 10:
			tvtype.setText("商品列表");
			break;
		}

	}

	@Override
	public void initDatas() {
		adapter = new GoodsListAdapter(goodsdatas, getAct());
	}

	@Override
	public void initViewOper() {
		//设置数据源
		lv.setAdapter(adapter);
		//设置可向下加载
		lv.setPullLoadEnable(true);
		lv.setXListViewListener(new IXListViewListener() {
			// 上拉刷新操作
			@Override
			public void onRefresh() {
				((MyApplication) getApplication()).initData();
			}

			// 向下加载数据
			@Override
			public void onLoadMore() {
				((MyApplication) getApplication()).onLoadMore();
			}
		});
		// 进入商品详情界面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				try {
					MyApplication.putData("goods", goodsdatas.get(position - 1));
					startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
				} catch (Exception e) {
					toastS("请检查网络！");
				}
			}
		});

	}

	// 返回
	@OnClick(R.id.act_goodslist_back)
	public void backOnClick(View v) {
		tvtype.setText("商品列表");
		finish();
	}
}
