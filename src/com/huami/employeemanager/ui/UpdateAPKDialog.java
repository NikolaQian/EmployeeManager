package com.huami.employeemanager.ui;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.huami.employeemanager.activity.R;
import com.huami.employeemanager.base.AppManager;
import com.huami.employeemanager.base.BaseActivity;
import com.huami.employeemanager.base.BaseApplication;
import com.huami.employeemanager.common.UIHelper;
import com.huami.employeemanager.thread.DownLoadThread;

public class UpdateAPKDialog extends BaseActivity {
	//下载路径
	public final static String DOWNLOAD_APK_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/";
	//apk名称
	public final static String DOWNLOAD_APK_NAME = "EmployeeManager.apk";
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
	
	private TextView title, fileNameText, progressText;
	
	private Handler handler;
	
	private DownLoadThread runnable;
	
	private BaseApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_dialog);
		application = (BaseApplication)getApplication();
		
		initView();
		
		update();
	}
	
	private void initView(){
		title = (TextView)findViewById(R.id.download_title);
		progress = (ProgressBar)findViewById(R.id.download_progress);
		fileNameText = (TextView)findViewById(R.id.download_filename);
		progressText = (TextView)findViewById(R.id.download_progress_text);
		
		title.setText(R.string.update_apk_dialog_title);
		fileNameText.setText(R.string.app_name);
	}
	
	private void update(){
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == DOWNLOAD_FAILL) {
					UIHelper.ToastMessage(UpdateAPKDialog.this, R.string.download_error);

					isStop = false;
				}else if (msg.what == DOWNLOAD_UPDATE) {

					Map<String, Object> map = (HashMap<String, Object>) msg.obj;

					progress.setProgress((Integer) map.get("progress"));
					
					isStop = true;
					
					progressText.setText(map.get("currentSize") + "/"
							+ map.get("fileSize"));

				}else if (msg.what == DOWNLOAD_SUCCESS){
					progressText.setText(R.string.download_success);
					
					File file = new File(DOWNLOAD_APK_PATH + DOWNLOAD_APK_NAME);
					
					openAPKFile(file);
					
					isStop = false;
					
					onDestroy();
				}
				
			}
		};
		
		Intent intent = getIntent();
		runnable = new DownLoadThread(handler, intent.getExtras().getLong("fileLength"),
										DOWNLOAD_APK_PATH, DOWNLOAD_APK_NAME, application);
		
		runnable.start();
		
	}
	
	/**
	 * 打开APK文件
	 */
	private void openAPKFile(File file){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		this.startActivity(intent);
	}
	
	/**
	 * 停止下载线程
	 */
	public void stopDownThread() {

		if (isStop) {
			runnable.setStart(false);
//			Toast.makeText(DownLoadActivity.this, "线程停止", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		stopDownThread();
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();
	}
}
