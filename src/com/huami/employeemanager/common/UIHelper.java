package com.huami.employeemanager.common;

import com.huami.employeemanager.ui.DownLoadDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

/**
 * Ӧ�ó���UI���߰�����װUI��ص�һЩ����
 * 
 * @author NikolaQian
 * 
 */
public class UIHelper {
	/**
	 * ����toast��Ϣ
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
	 * ��ʾ���ؽ���
	 * 
	 * @param context
	 */
	public static void showDownLoadDialog(Context context) {
		Intent intent = new Intent(context, DownLoadDialog.class);
		context.startActivity(intent);
	}

	/**
	 * ������ؼ����¼�
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
