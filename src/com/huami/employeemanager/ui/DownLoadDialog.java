package com.huami.employeemanager.ui;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huami.employeemanager.activity.MainActivity;
import com.huami.employeemanager.activity.R;
import com.huami.employeemanager.base.AppManager;
import com.huami.employeemanager.base.BaseActivity;
import com.huami.employeemanager.base.BaseApplication;
import com.huami.employeemanager.common.UIHelper;
import com.huami.employeemanager.database.DBManager;
import com.huami.employeemanager.thread.DownLoadThread;
import com.huami.employeemanager.thread.SocketReceiveThread;

public class DownLoadDialog extends BaseActivity{
		// 下载失败状态
		public final static int DOWNLOAD_FAILL = -1;
		// 下载成功状态
		public final static int DOWNLOAD_SUCCESS = 1;
		// 下载更新状态
		public final static int DOWNLOAD_UPDATE = 2;
		// 已有文件
		public final static int DOWNLOAD_EXITS = 3;
		// 是否是突然暂停的下载
		private boolean isStop = true;
		
		private ProgressBar progress;
		
		private TextView fileNameText, progressText;
		
		private Handler handler;
		
		private DownLoadThread runnable;
		
		private BaseApplication application;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setTitle("下载数据库");
			setContentView(R.layout.download_dialog);
			
			application = (BaseApplication)getApplication();
			
			initView();
			
			downLoad();
		}
		
		private void initView(){
			progress = (ProgressBar)findViewById(R.id.download_progress);
			fileNameText = (TextView)findViewById(R.id.download_filename);
			progressText = (TextView)findViewById(R.id.download_progress_text);
			
			fileNameText.setText(DBManager.DB_NAME);
		}
		
		private void downLoad(){
			
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == DOWNLOAD_FAILL) {
						UIHelper.ToastMessage(DownLoadDialog.this, R.string.download_error);

						isStop = false;
					}else if (msg.what == DOWNLOAD_UPDATE) {

						Map<String, Object> map = (HashMap<String, Object>) msg.obj;

						progress.setProgress((Integer) map.get("progress"));
						
						isStop = true;
						
						progressText.setText(map.get("currentSize") + "/"
								+ map.get("fileSize"));

					}else if (msg.what == DOWNLOAD_SUCCESS){
						progressText.setText(R.string.download_success);
						
						isStop = false;
						
						startActivity(MainActivity.class);
						onDestroy();
					}
					
				}
			};
			
			Intent intent = getIntent();
			runnable = new DownLoadThread(handler, intent.getExtras().getLong("fileLength"), 
											DBManager.DB_PATH, DBManager.DB_NAME, application);
			
			runnable.start();
			
		}
		
		/**
		 * 停止下载线程
		 */
		public void stopDownThread() {

			if (isStop) {
				runnable.setStart(false);
//				Toast.makeText(DownLoadActivity.this, "线程停止", Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		protected void onDestroy() {
			stopDownThread();
			AppManager.getAppManager().finishActivity(this);
			super.onDestroy();
		}	
}
