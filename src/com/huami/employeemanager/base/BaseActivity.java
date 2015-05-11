package com.huami.employeemanager.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Ӧ�ó���Activity����
 * 
 * @author NikolaQian
 *
 */
public class BaseActivity extends FragmentActivity {
	//�Ƿ�����ȫ��
	private boolean allowFullScreen = true;
	
	//�Ƿ���������
	private boolean allowDistroy = true;
	
	private View view;
	
	//��Ļ���
	private int screenWidth;
	
	//��Ļ�߶�
	private int screenHeight;
	
	//��Ļ�ܶ�
	private float density;
	
	//�����첽����
	protected List<AsyncTask<Void, Void, Boolean>> myAsyncTasks = new ArrayList<AsyncTask<Void,Void,Boolean>>();
	
	protected BaseApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		allowFullScreen = true;
		
		AppManager.getAppManager().addActivity(this);
		
		application = (BaseApplication)getApplication();
		
		DisplayMetrics metrics = new DisplayMetrics();
		
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		screenWidth = metrics.widthPixels;
		
		screenHeight = metrics.heightPixels;
		
		density = metrics.density;
	}
	
	/**
	 * ����첽����������
	 * 
	 * @param asyncTask
	 */
	public void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask){
		myAsyncTasks.add(asyncTask);
	}
	
	public boolean isAllowFullScreen(){
		return allowFullScreen;
	}
	
	/**
	 * �����Ƿ�ȫ��
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen){
		this.allowFullScreen = allowFullScreen;
		
		if(allowFullScreen){
			requestWindowFeature(Window.FEATURE_NO_TITLE); //�����ޱ���
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //����ȫ��
		}else {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //���÷�ȫ��
		}
		
	}

	public void setAllowDistroy(boolean allowDistroy) {
		this.allowDistroy = allowDistroy;
	}
	
	public void setAllowDistroy(boolean allowDistroy, View view) {
		this.allowDistroy = allowDistroy;
		this.view = view;
	}
	
	/**
	 * ��ȡ��Ļ����
	 */
	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public float getDensity() {
		return density;
	}
	
	/**
	 * ��������첽����
	 */
	public void clearAsyncTask(){
		Iterator<AsyncTask<Void, Void, Boolean>> iterator = myAsyncTasks.iterator();
		
		while(iterator.hasNext()){
			AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
			
			if(asyncTask != null && !asyncTask.isCancelled()){
				asyncTask.cancel(true);
			}
		}
		
		myAsyncTasks.clear();
	}
	
	/**
	 * ͨ��Class��ת����
	 */
	protected void  startActivity(Class<?> cls) {
		startActivity(cls, null);
	}
	
	/**
	 * ����Bundleͨ��Class��ת����
	 */
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		
		intent.setClass(this, cls);
		
		if(bundle != null){
			intent.putExtras(bundle);
		}
		
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		clearAsyncTask();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && view != null){
			view.onKeyDown(keyCode, event);
			
			if(!allowDistroy){
				return false;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	
}
