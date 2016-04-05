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
	private TextView tvinfo;//��Ʒ����
	@ViewInject(R.id.act_goodsxq_price)
	private TextView tvprice;//��Ʒ�۸�
	@ViewInject(R.id.act_goodsxq_brand)
	private TextView tvbrand;//��ƷƷ��
	@ViewInject(R.id.act_goodsxq_num)
	private TextView tvnum;//��Ʒ���
	@ViewInject(R.id.act_goodsxq_sales)
	private TextView tvsales;//��Ʒ����
	@ViewInject(R.id.act_goodsxq_goodspng)
	private ImageView goodsimg;//��ƷͼƬ
	@ViewInject(R.id.act_goodsxq_love)
	private ImageView loveimg;//�ɵ�����ղ�ͼƬ
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
		//��ʼ����ɫ���ߴ�ؼ��Լ��������ü���
		for (int i = 0; i < 8; i++) {
			tvs[i] = tvByid(tvids[i]);
			tvs[i].setOnClickListener(this);
		}
	}

	@Override
	public void initDatas() {
		
		goods = (GoodsBean) MyApplication.getData("goods", true);
		ub = BmobUser.getCurrentUser(getAct(), UserBean.class);
		//�����û�Id�鿴����Ʒ�Ƿ񱻸��û��ղ�
		if (ub != null) {
			BmobQuery<UserBean> query = new BmobQuery<UserBean>();
			query.addWhereEqualTo("objectId", ub.getObjectId());
			query.findObjects(getAct(), new FindListener<UserBean>() {
				
				@Override
				public void onSuccess(List<UserBean> arg0) {
					lovegoodsId = arg0.get(0).getLovegoodsId();
					//���ò�ͬ�ĵ���ɫ
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
		//�������۵�����
		setPingLunDatas();
		
	}
	
	private void setPingLunDatas() {
		BmobQuery<PingLunBean> query = new BmobQuery<PingLunBean>();
		query.addWhereEqualTo("goodsid", goods.getObjectId());
		query.order("-createdAt");
		//������ƷID��ѯ����Ʒ����������
		query.findObjects(getAct(), new FindListener<PingLunBean>() {

			@Override
			public void onError(int arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(List<PingLunBean> arg0) {
				if (arg0==null) {
					return;
				}
				//��������Դ
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

	// �ղ�
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
					// ����������
					newub.add("lovegoodsId", goods.getObjectId());
					// ��������
					ub.getLovegoodsId().add(goods.getObjectId());
					newub.update(getAct(), ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							loveimg.setImageResource(R.drawable.loveon);
							toastS("�ղسɹ���");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							toastS(arg1);
						}
					});
				} else {
					//ȡ���ղ�
					ub.getLovegoodsId().remove(goods.getObjectId());
					UserBean uu = new UserBean();
					List<String> values = new ArrayList<>();
					values.add(goods.getObjectId());
					uu.removeAll("lovegoodsId", values);
					//���·�����
					uu.update(getAct(), ub.getObjectId(), new UpdateListener() {

						@Override
						public void onSuccess() {
							loveimg.setImageResource(R.drawable.loveoff);
							toastS("ȡ���ղ�");
						}

						@Override
						public void onFailure(int arg0, String arg1) {

						}
					});
				}

			}
		});

	}
	//����Ĳ���
	@OnClick(R.id.act_goodsxq_buy)
	public void buyOnClick(View v) {
		//��û���û���¼������ת����½����
		if (ub==null) {
			startActivity(new Intent(getAct(), LoginActivity.class));
			return;
		}
		
		if (color == null || size == null) {
			toastS("����ѡ����Ʒ��ɫ�ͳߴ�");
			return;
		}
		//����Ʒ���ֶ���ɹ��ﳵ������뼯�Ͻ��д���
		CartBean cart = new CartBean();
		datas = new ArrayList<>();
		//���ò�ͬ�ֶ�
		cart.setPrice(goods.getPrice());
		cart.setColor(color);
		cart.setSize(size);
		cart.setFileUrl(goods.getGoodspng().getFileUrl(getAct()));
		cart.setGoodsId(goods.getObjectId());
		cart.setNumber(1);
		cart.setGoodsinfo(goods.getInfo());
		cart.setUserid(ub.getObjectId());
		//���뻺��
		datas.add(cart);
		MyApplication.putData("cart", datas);
		MyApplication.putData("price", goods.getPrice());
		startActivity(new Intent(getAct(), CommitOrderActivity.class));
	}
	//�ӹ��ﳵ
	@OnClick(R.id.act_goodsxq_addcart)
	public void addcartOnClick(View v){
		if (ub==null) {
			startActivity(new Intent(getAct(), LoginActivity.class));
			return;
		}
		if (color == null || size == null) {
			toastS("����ѡ����Ʒ��ɫ�ͳߴ�");
			return;
		}
		//����Ʒ���ֶ���ɹ��ﳵ������뼯�Ͻ��д���
		CartBean cart = new CartBean();
		cart.setPrice(goods.getPrice());
		cart.setColor(color);
		cart.setSize(size);
		cart.setGoodsId(goods.getObjectId());
		cart.setFileUrl(goods.getGoodspng().getFileUrl(getAct()));
		cart.setNumber(1);
		cart.setGoodsinfo(goods.getInfo());
		cart.setUserid(ub.getObjectId());
		//�ϴ����������Ա��Ժ��ѯ
		cart.save(getAct(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				toastS("���빺���ɹ�����ȥ�鿴");
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				toastS(arg1);
			}
		});
		
	}
	
	
	// ����
	@OnClick(R.id.act_goodsxq_back)
	public void backOnClick(View v) {
		finish();
	}

	//ѡ��ߴ�ĺ���ɫ�ļ���
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.act_goodsxq_blue:
			color = "��ɫ";
			updateTvColor(0);
			break;
		case R.id.act_goodsxq_hui:
			color = "��ɫ";
			updateTvColor(1);
			break;
		case R.id.act_goodsxq_red:
			color = "��ɫ";
			updateTvColor(2);
			break;
		case R.id.act_goodsxq_write:
			color = "��ɫ";
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
	//���ĳߴ���ɫ
	private void updateTvSizeColor(int i) {
		for (int j = 4; j < 8; j++) {
			if (i == j) {
				tvs[j].setTextColor(Color.parseColor("#ff0000"));
			} else {
				tvs[j].setTextColor(Color.parseColor("#000000"));
			}
		}

	}
	//������Ʒ��ɫ��TextView
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
