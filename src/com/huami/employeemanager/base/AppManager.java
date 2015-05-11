package com.huami.employeemanager.base;

import java.util.Stack;
import android.app.Activity;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 
 * @author NikolaQian
 *
 */
public class AppManager {
	private static AppManager instance;
	
	//Activity堆栈
	private static Stack<Activity> activityStack;
	
	/**
	 * 单例
	 */
	public static AppManager getAppManager(){
		if(instance == null){
			instance = new AppManager();
		}
		
		return instance;
	}
	
	/**
	 * 添加Activity到堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity){
		if(activityStack == null){
			activityStack = new Stack<Activity>();
		}
		
		activityStack.add(activity);
	}
	
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity != null){
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * 结束当前的activity(堆栈中最后一个压入的activity)
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();

		finishActivity(activity);
	}
}
