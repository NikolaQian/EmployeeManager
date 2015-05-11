package com.huami.employeemanager.base;

import java.util.Stack;
import android.app.Activity;

/**
 * Ӧ�ó���Activity�����ࣺ����Activity�����Ӧ�ó����˳�
 * 
 * @author NikolaQian
 *
 */
public class AppManager {
	private static AppManager instance;
	
	//Activity��ջ
	private static Stack<Activity> activityStack;
	
	/**
	 * ����
	 */
	public static AppManager getAppManager(){
		if(instance == null){
			instance = new AppManager();
		}
		
		return instance;
	}
	
	/**
	 * ���Activity����ջ
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
	 * ����ָ����Activity
	 */
	public void finishActivity(Activity activity){
		if(activity != null){
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * ������ǰ��activity(��ջ�����һ��ѹ���activity)
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();

		finishActivity(activity);
	}
}
