package cn.lgx.yougou;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.lgx.yougou.adapter.GoodsPingLunAdapter;
import cn.lgx.yougou.application.MyApplication;
import cn.lgx.yougou.base.BaseActivity;
import cn.lgx.yougou.base.BaseInterface;
import cn.lgx.yougou.bean.CartBean;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.bean.PingLunBean;
import cn.lgx.yougou.bean.UserBean;
import cn.lgx.yougou.utils.ImageUtils;

public class GoodsDetailsActivity extends BaseActivity implements BaseInterface, OnClickListener {

	@ViewInject(R.id.act_goodsxq_info)
	private TextView tvinfo;//商品详情
	@ViewInject(R.id.act_goodsxq_price)
	private TextView tvprice;//商品价格
	@ViewInject(R.id.act_goodsxq_brand)
	private TextView tvbrand;//商品品牌
	@ViewInject(R.id.act_goodsxq_num)
	private TextView tvnum;//商品编号
	@ViewInject(R.id.act_goodsxq_sales)
	private TextView tvsales;//商品销量
	@ViewInject(R.id.act_goodsxq_goodspng)
	private ImageView goodsimg;//商品图片
	@ViewInject(R.id.act_goodsxq_love)
	private ImageView loveimg;//可点击的收藏图片
	@ViewInject(R.id.act_goodsxq_lv)
	private ListView lView;
	
	private int tvids[] = { R.id.act_goodsxq_blue, R.id.act_goodsxq_hui, R.id.act_goodsxq_red, R.id.act_goodsxq_write,
			R.id.act_goodsxq_m, R.id.act_goodsxq_l, R.id.act_goodsxq_xl, R.id.act_goodsxq_xxl };
	private TextView tvs[] = new TextView[8];
	private GoodsBean goods;
	private UserBean ub;
	private List<String> lovegoodsId;
	private String color;
	private String size;
	private List<CartBean> datas;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initViews();
		initDatas();
		initViewOper();
	}

	@Override
	public void initViews() {
		setContentView(R.layout.act_goodsxq);
		ViewUtils.inject(getAct());
		//初始化颜色、尺寸控件以及给其设置监听
		for (int i = 0; i < 8; i++) {
			tvs[i] = tvByid(tvids[i]);
			tvs[i].setOnClickListener(this);
		}
	}

	@Override
	public void initDatas() {
		
		goods = (GoodsBean) MyApplication.getData("goods", true);
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		//根据用户Id查看该商品是否被该用户收藏
		if (ub != null) {
			BmobQuery<UserBean> query = new BmobQuery<UserBean>();
			query.addWhereEqualTo("objectId", ub.getObjectId());
			query.findObjects(getAct(), new FindListener<UserBean>() {
				
				@Override
				public void onSuccess(List<UserBean> arg0) {
					lovegoodsId = arg0.get(0).getLovegoodsId();
					//设置不同心的颜色
					if (lovegoodsId.contains(goods.getObjectId())) {
						loveimg.setImageResource(R.drawable.loveon);
					} else {
						loveimg.setImageResource(R.drawable.loveoff);
					}
				}
				
				@Override
				public void onError(int arg0, String arg1) {
				}
			});
		}
		//设置评论的数据
		setPingLunDatas();
		
	}
	
	private void setPingLunDatas() {
		BmobQuery<PingLunBean> query = new BmobQuery<PingLunBean>();
		query.addWhereEqualTo("goodsid", goods.getObjectId());
		query.order("-createdAt");
		//根据商品ID查询该商品的所有评论
		query.findObjects(getAct(), new FindListener<PingLunBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(List<PingLunBean> arg0) {
				if (arg0==null) {
					return;
				}
				//设置数据源
				lView.setAdapter(new GoodsPingLunAdapter(arg0, getAct()));
			}
		});
	}

	@Override
	public void initViewOper() {

		tvinfo.setText(goods.getInfo());
		tvbrand.setText(goods.getBrand());
		tvprice.setText(goods.getPrice() + "");
		tvnum.setText(goods.getNumber() + "");
		tvsales.setText(goods.getSalesNum() + "");
		ImageUtils.loadBigImg(goodsimg, goods.getGoodspng().getFileUrl(getAct()));
	}

	// 收藏
	@OnClick(R.id.act_goodsxq_love)
	public void LoveOnClick(View v) {

		if (ub == null) {
			startActivity(new Intent(getAct(), LoginActivity.class));
			return;
		}

		BmobQuery<UserBean> query = new BmobQuery<UserBean>();
		query.addWhereEqualTo("objectId", ub.getObjectId());
		query.findObjects(getAct(), new FindListener<UserBean>() {
			@Override
			public void onError(int arg0, String arg1) {
			}

			@Override
			public void onSuccess(List<UserBean> arg0) {

				if (!arg0.get(0).getLovegoodsId().contains(goods.getObjectId())) {

					UserBean newub = new UserBean();
					// 服务器增加
					newub.add("lovegoodsId", goods.getObjectId());
					// 本地增加
					ub.getLovegoodsId().add(goods.getObjectId());
					newub.update(getAct(), ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							loveimg.setImageResource(R.drawable.loveon);
							toastS("收藏成功！");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							toastS(arg1);
						}
					});
				} else {
					//取消收藏
					ub.getLovegoodsId().remove(goods.getObjectId());
					UserBean uu = new UserBean();
					List<String> values = new ArrayList<>();
					values.add(goods.getObjectId());
					uu.removeAll("lovegoodsId", values);
					//更新服务器
					uu.update(getAct(), ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							loveimg.setImageResource(R.drawable.loveoff);
							toastS("取消收藏");
						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
				}

			}
		});

	}
	//购买的操作
	@OnClick(R.id.act_goodsxq_buy)
	public void buyOnClick(View v) {
		//若没有用户登录，则跳转到登陆界面
		if (ub==null) {
			startActivity(new Intent(getAct(), LoginActivity.class));
			return;
		}
		
		if (color == null || size == null) {
			toastS("请您选择商品颜色和尺寸");
			return;
		}
		//将商品的字段组成购物车对象加入集合进行传递
		CartBean cart = new CartBean();
		datas = new ArrayList<>();
		//设置不同字段
		cart.setPrice(goods.getPrice());
		cart.setColor(color);
		cart.setSize(size);
		cart.setFileUrl(goods.getGoodspng().getFileUrl(getAct()));
		cart.setGoodsId(goods.getObjectId());
		cart.setNumber(1);
		cart.setGoodsinfo(goods.getInfo());
		cart.setUserid(ub.getObjectId());
		//放入缓存
		datas.add(cart);
		MyApplication.putData("cart", datas);
		MyApplication.putData("price", goods.getPrice());
		startActivity(new Intent(getAct(), CommitOrderActivity.class));
	}
	//加购物车
	@OnClick(R.id.act_goodsxq_addcart)
	public void addcartOnClick(View v){
		if (ub==null) {
			startActivity(new Intent(getAct(), LoginActivity.class));
			return;
		}
		if (color == null || size == null) {
			toastS("请您选择商品颜色和尺寸");
			return;
		}
		//将商品的字段组成购物车对象加入集合进行传递
		CartBean cart = new CartBean();
		cart.setPrice(goods.getPrice());
		cart.setColor(color);
		cart.setSize(size);
		cart.setGoodsId(goods.getObjectId());
		cart.setFileUrl(goods.getGoodspng().getFileUrl(getAct()));
		cart.setNumber(1);
		cart.setGoodsinfo(goods.getInfo());
		cart.setUserid(ub.getObjectId());
		//上传到服务器以便以后查询
		cart.save(getAct(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				toastS("加入购车成功，请去查看");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastS(arg1);
			}
		});
		
	}
	
	
	// 返回
	@OnClick(R.id.act_goodsxq_back)
	public void backOnClick(View v) {
		finish();
	}

	//选择尺寸的和颜色的监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_goodsxq_blue:
			color = "蓝色";
			updateTvColor(0);
			break;
		case R.id.act_goodsxq_hui:
			color = "灰色";
			updateTvColor(1);
			break;
		case R.id.act_goodsxq_red:
			color = "红色";
			updateTvColor(2);
			break;
		case R.id.act_goodsxq_write:
			color = "白色";
			updateTvColor(3);
			break;
		case R.id.act_goodsxq_m:
			size = "M";
			updateTvSizeColor(4);
			break;
		case R.id.act_goodsxq_l:
			size = "L";
			updateTvSizeColor(5);
			break;
		case R.id.act_goodsxq_xl:
			size = "XL";
			updateTvSizeColor(6);
			break;
		case R.id.act_goodsxq_xxl:
			size = "XXL";
			updateTvSizeColor(7);
			break;
		}
	}
	//更改尺寸颜色
	private void updateTvSizeColor(int i) {
		for (int j = 4; j < 8; j++) {
			if (i == j) {
				tvs[j].setTextColor(Color.parseColor("#ff0000"));
			} else {
				tvs[j].setTextColor(Color.parseColor("#000000"));
			}
		}

	}
	//更改商品颜色的TextView
	private void updateTvColor(int i) {
		for (int j = 0; j < 8; j++) {
			if (i == j) {
				tvs[j].setTextColor(Color.parseColor("#ff0000"));
			} else {
				tvs[j].setTextColor(Color.parseColor("#000000"));
			}
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		setPingLunDatas();
		initViewOper();
	}
}
