package cn.lgx.yougou.utils;

import android.app.Dialog;
import android.content.Context;
import cn.lgx.yougou.R;

public class ProgressDialog extends Dialog{

	public ProgressDialog(Context context) {
		
		super(context,R.style.progressDialog);
	}

	public void setMessage(String msg) {
		
	}
	
}
