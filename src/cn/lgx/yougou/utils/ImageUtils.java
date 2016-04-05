package cn.lgx.yougou.utils;

import android.widget.ImageView;
import cn.lgx.yougou.R;
import cn.lgx.yougou.application.MyApplication;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
/**
 * 加载图片的工具类
 * @author lgx
 *
 */
public class ImageUtils {
	
	private static ImageLoader loader = new ImageLoader(MyApplication.queue, new MyCache());
	
	private static ImageListener listener;
	/**
	 * 加载小图片
	 * @param img 要显示的控件
	 * @param url 图片url
	 */
	public static void loadSmallImg(ImageView img,String url){
		listener = ImageLoader.getImageListener(img, R.drawable.load,R.drawable.load);
		loader.get(url, listener, 200, 200);
	}
	
	/**
	 * 加载大图片
	 * @param img 要显示的控件
	 * @param url 图片url
	 */
	public static void loadBigImg(ImageView img,String url){
		listener = ImageLoader.getImageListener(img, R.drawable.load,R.drawable.load);
		loader.get(url, listener, 500, 500);
	}
}
