package cn.lgx.yougou.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import cn.lgx.yougou.R;

/**
 * 操作dialog的工具类
 * 
 * @author lgx
 *
 */
public class DialogUtils {

	private static ProgressDialog progressDialog;

	/**
	 * 显示dialog
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            dialog标题
	 * @param msg
	 *            dialog内容
	 * @param isCancelable
	 *            是否可以取消
	 */
	public static void showDialog(Context context, String title, String msg, boolean isCancelable) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(isCancelable);
		if (title != null) {
			progressDialog.setTitle(title);
		}
		if (msg != null) {
			progressDialog.setMessage(msg);
		}
		progressDialog = new ProgressDialog(context);
		progressDialog.show();
		// 注意此处要放在show之后 否则会报异常
		progressDialog.setContentView(R.layout.progress_dialog);
	}

	/**
	 * 取消dialog
	 */
	public static void dismiss() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
	}
	private static AlertDialog alertDialog;
	 
	public static void showAlertDialog(Context context, String title, String msg, boolean isCancelable,DialogInterface.OnClickListener listener) {
		alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).create();
		alertDialog.show();
	}
}
