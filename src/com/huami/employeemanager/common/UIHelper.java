package com.huami.employeemanager.common;

import com.huami.employeemanager.ui.DownLoadDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

/**
 * 应用程序UI工具包：封装UI相关的一些操作
 * 
 * @author NikolaQian
 * 
 */
public class UIHelper {
	/**
	 * 弹出toast消息
	 * 
	 * @param context
	 * @param msg
	 */
	public static void ToastMessage(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
		
	public static void ToastMessage(Context context, int msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 显示下载界面
	 * 
	 * @param context
	 */
	public static void showDownLoadDialog(Context context) {
		Intent intent = new Intent(context, DownLoadDialog.class);
		context.startActivity(intent);
	}

	/**
	 * 点击返回监听事件
	 * 
	 * @param activity
	 * @return
	 */
	public static View.OnClickListener finish(final Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				activity.finish();
			}
		};
	}
}
