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
 * 应用程序Activity基类
 * 
 * @author NikolaQian
 *
 */
public class BaseActivity extends FragmentActivity {
	//是否允许全屏
	private boolean allowFullScreen = true;
	
	//是否允许被销毁
	private boolean allowDistroy = true;
	
	private View view;
	
	//屏幕宽度
	private int screenWidth;
	
	//屏幕高度
	private int screenHeight;
	
	//屏幕密度
	private float density;
	
	//所有异步任务
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
	 * 添加异步任务到数组中
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
	 * 设置是否全屏
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen){
		this.allowFullScreen = allowFullScreen;
		
		if(allowFullScreen){
			requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏
		}else {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置非全屏
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
	 * 获取屏幕参数
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
	 * 清除所有异步任务
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
	 * 通过Class跳转界面
	 */
	protected void  startActivity(Class<?> cls) {
		startActivity(cls, null);
	}
	
	/**
	 * 含有Bundle通过Class跳转界面
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
