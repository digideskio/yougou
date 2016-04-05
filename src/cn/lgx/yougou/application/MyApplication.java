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
		// 初始化Bmob
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
				initData();// 递归
			}
		}, true);

	}

	// 向下加载更多的方法
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
	 * 用于监听MyApplication中 对于数据的下载
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
		// 初始化 Bmob SDK
		// 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
		Bmob.initialize(this, "97cf79579ea67689a26f4751d3fa7541");
		BmobSMS.initialize(this, "97cf79579ea67689a26f4751d3fa7541");
		// 初始化
		BP.init(this, "97cf79579ea67689a26f4751d3fa7541");
	}

	public static void putData(String key, Object value) {
		map.put(key, value);
	}

	/**
	 * Application中有缓存区域，调用此方法可以获取putData()方法里存储的数据
	 * 
	 * @param key
	 *            要获取数据的key
	 * @param isDelete
	 *            获取完毕是否从缓存空间中删除这个数据
	 * @return 返回获得的数据
	 */
	public static Object getData(String key, boolean isDelete) {
		Object object = map.get(key);
		if (isDelete) {
			map.remove(key);
		}
		return object;

	}
}
