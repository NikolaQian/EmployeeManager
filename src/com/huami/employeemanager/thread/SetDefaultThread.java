package com.huami.employeemanager.thread;

import android.os.Handler;
import android.os.Message;

import com.huami.employeemanager.activity.MainActivity;

public class SetDefaultThread extends Thread{
	private static long delay = 100;
	private Handler handler;
	
	public SetDefaultThread(Handler handler){
		this.handler = handler;
	}
	
	@Override
	public synchronized void run() {
		
		for(int i=0; i<20; i++){
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(MainActivity.isSame && MainActivity.isClear){
				i = 0;
				MainActivity.isSame = false;
			}
		}
		
		Message msg = new Message();
		msg.what = MainActivity.SET_DEFAULT;
		handler.sendMessage(msg);
	}
	
}
