package cn.lgx.yougou.utils;

import android.widget.ImageView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.application.MyApplication;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
/**
 * ����ͼƬ�Ĺ�����
 * @author lgx
 *
 */
public class ImageUtils {
	
	private static ImageLoader loader = new ImageLoader(MyApplication.queue, new MyCache());
	
	private static ImageListener listener;
	/**
	 * ����СͼƬ
	 * @param img Ҫ��ʾ�Ŀؼ�
	 * @param url ͼƬurl
	 */
	public static void loadSmallImg(ImageView img,String url){
		listener = ImageLoader.getImageListener(img, R.drawable.load,R.drawable.load);
		loader.get(url, listener, 200, 200);
	}
	
	/**
	 * ���ش�ͼƬ
	 * @param img Ҫ��ʾ�Ŀؼ�
	 * @param url ͼƬurl
	 */
	public static void loadBigImg(ImageView img,String url){
		listener = ImageLoader.getImageListener(img, R.drawable.load,R.drawable.load);
		loader.get(url, listener, 500, 500);
	}
}
