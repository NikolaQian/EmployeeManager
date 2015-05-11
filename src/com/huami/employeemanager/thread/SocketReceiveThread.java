package com.huami.employeemanager.thread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import com.huami.employeemanager.activity.MainActivity;
import com.huami.employeemanager.activity.WelcomeActivity;
import com.huami.employeemanager.base.AppManager;
import com.huami.employeemanager.common.UIHelper;
import com.huami.employeemanager.net.URL;

import android.app.Activity;
import android.app.Notification.Action;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SocketReceiveThread extends Thread {
	 //IP地址
    private  String ipAdress;

    private Handler handler;
	
	public static Socket socket;
	
	private Activity activity;
	//等待连接的时间（0表示无限等待）
	private static final int TIME_OUT = 0;
	
	private static final int BUFFER_SIZE = 1024;
	
	public static boolean isStop = false;
	
	public SocketReceiveThread(Activity activity, Handler handler, String ipAdress){
		this.handler = handler;
		this.ipAdress = ipAdress;
	}
	
	@Override
	public synchronized void run() {
		int connectTime = 0;
		isStop = false;
		
		while(true && !isStop){			
			try {
				socket = new Socket();
				connectTime = 0;
				
				socket.connect(new InetSocketAddress(ipAdress, URL.PC_PORT), TIME_OUT);
				connectTime++;
				
				Message message3 = new Message();
    			message3.what = MainActivity.CONNECT_SUCCEED;
            	handler.sendMessage(message3);
            	
            	//向服务器发送信息
				BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				final String s = "{\"req\":\"auth\",\"oa\":\"a086c64e4859\",\"ds\":[]}";
				final int len = s.getBytes().length;
				Log.d("guyan", "len: " + len);
				final byte[] a = new byte[s.getBytes().length + 2];
				a[0] = (byte)(len & 0xff);
				a[1] = (byte)((len >> 8) & 0xff);
				for (int i = 0; i < len; i++) {
					a[2 + i] = s.getBytes()[i];
				}
				writer.write(new String(a));writer.flush();
            	
        		while(true && !isStop){
        			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            		
            		int count = 0;
            		char[] buffer = new char[BUFFER_SIZE];

            		if ((count = in.read(buffer)) > 0){                     	
                    	
                    	String msg = getInfoBuff(buffer, count);
                    	
                    	if(msg.contains("CMD:DownLoad")){
                    		
                    		Message message = new Message();
                    		message.what = MainActivity.DOWNLOAD_DB;
                    		message.obj = msg;
                        	handler.sendMessage(message); 
                        	
                        	isStop = true;
                        	in.close();
                        	socket.close();
                        	Thread.sleep(100);
                        	
                        	break;
                    	}else if(msg.contains("CMD:UpdateApplication")){
                    		Message message = new Message();
                    		message.what = MainActivity.UPDATE_APK;
                    		message.obj = msg;
                        	handler.sendMessage(message); 
                        	
                        	isStop = true;
                        	in.close();
                        	socket.close();
                        	Thread.sleep(100);
                        	
                        	break;
                    	}else if(msg.equals("exit")){
                    		in.close();
                    		if (null != socket){  
                                Message message = new Message();
                                message.what = MainActivity.TOAST_MESSAGE;
                                message.obj = "EXIT,即将关闭";
            					Thread.sleep(1000);
            					handler.sendMessage(message);
            					AppManager.getAppManager().finishActivity(activity);
            					socket.close();
                            }
                    	}else {
                    		
                    		int code = (int) ((new JSONObject(msg).get("code")));
                			if(code == 0){
                				Log.v("msg", msg);
                        		new JSONObject(msg).getJSONArray("das");
                        		Message message = new Message();
                        		message.what = MainActivity.SELECT_EMPLOYEE;
                        		message.obj = msg;
                            	handler.sendMessage(message);                    	
                            	Thread.sleep(1);
                			}
                    		
                    	}
                    	
                    }
        		}
			} catch (IOException e) {
				
//				if(connectTime == 0){
//					Message message = new Message();
//					message.what = MainActivity.TOAST_MESSAGE;
//					message.obj = "等待连接超时,即将关闭";
//					
//					try {
//						Thread.sleep(1000);
//						socket.close();
//					} catch (InterruptedException e1) {
//						e1.printStackTrace();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					
//					handler.sendMessage(message);
//					AppManager.getAppManager().finishActivity(activity);					
//					break;
//				}else {
//					Message message = new Message();
//					message.what = MainActivity.TOAST_MESSAGE;
//					message.obj = "连接断开,等待连接";
//	            	handler.sendMessage(message);
//				}
				
				if(!isStop){
					Message message = new Message();
					message.what = MainActivity.DISCONNECTED;
	            	handler.sendMessage(message);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				Log.v("wrong JSON", "wrong JSON");
				e.printStackTrace();
			}
			
			if(isStop){
				break;
			}
		}
		
	}
	
	private String getInfoBuff(char[] buff, int count)  
    {  
        char[] temp = new char[count];  
        for(int i=0; i<count; i++)  
        {  
            temp[i] = buff[i];  
        }  
        return new String(temp);  
    }
	
}
