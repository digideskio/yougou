package cn.lgx.yougou.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import c.b.BP;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.FindListener;
import cn.lgx.yougou.bean.GoodsBean;
import cn.lgx.yougou.utils.FindDatasUtils;

public class MyApplication extends Application {

	private static Map<String, Object> map;
	private FindDatasListener finddataslistener;
	private int count = 0;
	public static RequestQueue queue;
	@Override
	public void onCreate() {
		super.onCreate();
		map = new HashMap<>();
		queue = Volley.newRequestQueue(this);
		// ��ʼ��Bmob
		initBmob();
		initData();
	}

	public void initData() {
		FindDatasUtils.findDatas(10, this, new FindListener<GoodsBean>() {

			@Override
			public void onSuccess(List<GoodsBean> arg0) {
				putData("findDatasKey", arg0);
				count = arg0.size();
				if (finddataslistener != null) {
					finddataslistener.onSuccess(arg0);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				initData();// �ݹ�
			}
		}, true);

	}

	// ���¼��ظ���ķ���
	public void onLoadMore() {
		FindDatasUtils.findDatas(count, 10, this, new FindListener<GoodsBean>() {

			@Override
			public void onSuccess(List<GoodsBean> datas) {
				count += datas.size();
				putData("skidDatasKey", datas);
				if (finddataslistener != null) {
					finddataslistener.onSuccess(null);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		}, true);
	}

	/**
	 * ���ڼ���MyApplication�� �������ݵ�����
	 * 
	 * @author lgx
	 *
	 */
	public interface FindDatasListener {
		void onSuccess(List<GoodsBean> datas);
	}

	public FindDatasListener getFinddataslistener() {
		return finddataslistener;
	}

	public void setFinddataslistener(FindDatasListener finddataslistener) {
		this.finddataslistener = finddataslistener;
	}

	private void initBmob() {
		// ��ʼ�� Bmob SDK
		// ʹ��ʱ�뽫�ڶ�������Application ID�滻������Bmob�������˴�����Application ID
		Bmob.initialize(this, "97cf79579ea67689a26f4751d3fa7541");
		BmobSMS.initialize(this, "97cf79579ea67689a26f4751d3fa7541");
		// ��ʼ��
		BP.init(this, "97cf79579ea67689a26f4751d3fa7541");
	}

	public static void putData(String key, Object value) {
		map.put(key, value);
	}

	/**
	 * Application���л������򣬵��ô˷������Ի�ȡputData()������洢������
	 * 
	 * @param key
	 *            Ҫ��ȡ���ݵ�key
	 * @param isDelete
	 *            ��ȡ����Ƿ�ӻ���ռ���ɾ���������
	 * @return ���ػ�õ�����
	 */
	public static Object getData(String key, boolean isDelete) {
		Object object = map.get(key);
		if (isDelete) {
			map.remove(key);
		}
		return object;

	}
}
