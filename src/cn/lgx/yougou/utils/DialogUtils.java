package cn.lgx.yougou.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import cn.lgx.yougou.R;

/**
 * ����dialog�Ĺ�����
 * 
 * @author lgx
 *
 */
public class DialogUtils {

	private static ProgressDialog progressDialog;

	/**
	 * ��ʾdialog
	 * 
	 * @param context
	 *            ������
	 * @param title
	 *            dialog����
	 * @param msg
	 *            dialog����
	 * @param isCancelable
	 *            �Ƿ����ȡ��
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
		// ע��˴�Ҫ����show֮�� ����ᱨ�쳣
		progressDialog.setContentView(R.layout.progress_dialog);
	}

	/**
	 * ȡ��dialog
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
		alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("ȷ��", listener)
				.setNegativeButton("ȡ��", null).create();
		alertDialog.show();
	}
}
