package cn.lgx.yougou.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.R;
import cn.lgx.yougou.ShowGoodsListActivity;
import cn.lgx.yougou.adapter.HomeVpAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseFragment;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.utils.TextUtils;

public class HomeFragment extends BaseFragment implements BaseInterface {

	private int[] imgResIDs = new int[] { R.drawable.good1, R.drawable.good2, R.drawable.good3, R.drawable.good4,
			R.drawable.good5 };
	private int[] radioButtonID = new int[] { R.id.fragment_home_radio0, R.id.fragment_home_radio1,
			R.id.fragment_home_radio2, R.id.fragment_home_radio3, R.id.fragment_home_radio4 };
	@ViewInject(R.id.fragment_home_tuijian)
	private ViewPager pager;
	@ViewInject(R.id.fragment_home_rdiaogroup)
	private RadioGroup mGroup;

	private ArrayList<View> items = new ArrayList<View>();
	private Runnable runnable;
	private int mCurrentItem = 0;
	private int mItem;
	private Runnable mPagerAction;
	private boolean isFrist = true;
	private int numberkey = -1;
	@ViewInject(R.id.fragment_home_et)
	private EditText etcontent;
	
	@Override
	public View getfragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		ViewUtils.inject(this,view);
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
	public void initViews() {
		// 初始化Viewpager的所有item
		for (int i = 0; i < imgResIDs.length; i++) {
			items.add(initPagerItem(imgResIDs[i]));
		}
		mItem = items.size();
		
	}

	@Override
	public void initDatas() {
		
	}

	@Override
	public void initViewOper() {
		
		pager.setAdapter(new HomeVpAdapter(items, imgResIDs, pager));
		//图片轮播的监听
		pager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int arg0) {
				mCurrentItem = arg0 % items.size();
				pager.setCurrentItem(mCurrentItem);
				mGroup.check(radioButtonID[mCurrentItem]);
				items.get(arg0).findViewById(R.id.tuijian_header_img).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						numberkey = 10;
						MyApplication.putData("type", numberkey);
						startActivity(new Intent(getContext(), ShowGoodsListActivity.class));
					}
				});
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mPagerAction = new Runnable() {
			@Override
			public void run() {

				if (mItem != 0) {
					if (isFrist == true) {
						mCurrentItem = 0;
						isFrist = false;
					} else {
						if (mCurrentItem == items.size() - 1) {
							mCurrentItem = 0;
						} else {
							mCurrentItem++;
						}
					}
					pager.setCurrentItem(mCurrentItem);
				}
				pager.postDelayed(mPagerAction, 2500);
				// mGroup.check(radioButtonID[mCurrentItem]);
			}
		};
		pager.postDelayed(mPagerAction, 100);
		
	}

	private View initPagerItem(int resID) {

		View layout1 = getActivity().getLayoutInflater().inflate(R.layout.tuijian_header, null);
		ImageView imageView1 = (ImageView) layout1.findViewById(R.id.tuijian_header_img);
		imageView1.setImageResource(resID);
		return layout1;

	}
	//首页不同类型的跳转
	@OnClick({R.id.fragment_home_djxpin,R.id.fragment_home_jygou,R.id.fragment_home_zhekou,R.id.fragment_home_zhuangui,R.id.fragment_home_shouer,
		R.id.fragment_home_dapai,R.id.fragment_home_shouqi,R.id.fragment_home_renqi,R.id.fragment_home_sousuo,
		R.id.fragment_home_shoujizx,R.id.fragment_home_fengxiang,R.id.fragment_home_miaosha})
	public void OnClick(View v){
		
		switch (v.getId()) {
		case R.id.fragment_home_djxpin:
			numberkey=1;
			break;
		case R.id.fragment_home_jygou:
			numberkey=2;
			break;
		case R.id.fragment_home_zhekou:
			numberkey=2;
			break;
		case R.id.fragment_home_zhuangui:
			numberkey=4;
			break;
		case R.id.fragment_home_shouer:
			numberkey=5;
			break;
		case R.id.fragment_home_dapai:
			numberkey=6;
			break;
		case R.id.fragment_home_shouqi:
			numberkey=7;
			break;
		case R.id.fragment_home_renqi:
			numberkey=8;
			break;
		case R.id.fragment_home_sousuo:
			numberkey = 9;
			sousuoOnClick();
			break;
		case R.id.fragment_home_fengxiang:
		case R.id.fragment_home_shoujizx:
		case R.id.fragment_home_miaosha:	
		    numberkey = 10;
			break;
		}
		if (numberkey!=9) {
			MyApplication.putData("type", numberkey);
			startActivity(new Intent(getContext(), ShowGoodsListActivity.class));
		}
	}
	
	//搜索相关内容
	public void sousuoOnClick(){
		
		final String et_Str = etcontent.getText().toString().trim();
		if (et_Str==null||"".equals(et_Str)) {
			toastS("请输入检索数据");
			return;
		}
		//利用复合查询进行搜索，商品编号和商品部分信息
		BmobQuery<GoodsBean> eq1 = new BmobQuery<GoodsBean>();
		eq1.addWhereEqualTo("number", et_Str);
		BmobQuery<GoodsBean> eq2 = new BmobQuery<GoodsBean>();
		eq2.addWhereContains("info", et_Str);
		List<BmobQuery<GoodsBean>> queries = new ArrayList<BmobQuery<GoodsBean>>();
		queries.add(eq1);
		queries.add(eq2);
		BmobQuery<GoodsBean> query = new BmobQuery<GoodsBean>();
		query.or(queries);
		//进行查询
		query.findObjects(getActivity(), new FindListener<GoodsBean>() {
			
			@Override
			public void onSuccess(List<GoodsBean> data) {
				if (data != null && data.size() > 0) {
					String key = "findGoodsBean";
					// 检索数据
					MyApplication.putData(key, data);
					// 跳转界面
					Intent intent = new Intent(getActivity(), ShowGoodsListActivity.class);
					intent.putExtra("key", key);
					intent.putExtra("NumberKey", numberkey);
					if (numberkey==9) {
						intent.putExtra("title_Str", TextUtils.textLengthMax(et_Str));
					}
					etcontent.setText("");
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
}
