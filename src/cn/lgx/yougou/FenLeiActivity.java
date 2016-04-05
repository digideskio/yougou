package cn.lgx.yougou;

import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.lgx.yougou.adapter.GoodsListAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;

public class FenLeiActivity extends BaseActivity implements BaseInterface {

	private List<GoodsBean> datas;
	@ViewInject(R.id.act_fenlei_lv)
	private ListView lv;
	@ViewInject(R.id.act_fenlei_tvtype)
	private TextView tvtitle;
	private String numberkey;
	private String title;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_fenlei);
		ViewUtils.inject(getAct());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initDatas() {
		Intent intent = getIntent();
		//得到查询的商品集合
		datas = (List<GoodsBean>) MyApplication.getData(intent.getStringExtra("key"), true);
		numberkey = intent.getStringExtra("NumberKey");
		//根据numberKey进行判断
		if (numberkey == null) {
			return;
		}
		if ("10".equals(numberkey)) {
			title = intent.getStringExtra("title_Str");
		}
		settitle(numberkey);

	}
	//设置不同的标题
	private void settitle(String numberkey) {
		switch (numberkey) {
		case "1":
			tvtitle.setText("运动");
			break;
		case "2":
			tvtitle.setText("户外");
			break;
		case "3":
			tvtitle.setText("女鞋");
			break;
		case "4":
			tvtitle.setText("男鞋");
			break;
		case "5":
			tvtitle.setText("男装");
			break;
		case "6":
			tvtitle.setText("女装");
			break;
		case "7":
			tvtitle.setText("童装");
			break;
		case "8":
			tvtitle.setText("箱包");
			break;
		case "9":
			tvtitle.setText("家居");
			break;
		case "10":
			tvtitle.setText("与"+title+"相关");
			break;
		case "11":
			tvtitle.setText("热门推荐");
			break;
		}
	}

	@Override
	public void initViewOper() {
		//设置数据源
		lv.setAdapter(new GoodsListAdapter(datas, getAct()));
		// 进入商品详情界面
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyApplication.putData("goods", datas.get(position));
				//跳转
				startActivity(new Intent(getAct(), GoodsDetailsActivity.class));
			}
		});

	}
	
	@OnClick(R.id.act_fenlei_back)
	public void backOnClick(View v){
		finish();
	}
}
