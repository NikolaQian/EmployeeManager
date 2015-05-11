package com.huami.employeemanager.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;

/**
 * �����û������Ϣ��������Ϣ
 * 
 * @author NikolaQian
 *
 */
public class AppConfig {
	public final static String APP_CONFIG = "config";
	
	public final static String CON_APP_UNIQUEID = "APP_UNIQUEID";
	
	public final static String CONF_CHECKUP = "per_checkup";
	
	public final static String CONF_VOICE = "perf_voice";
	
	public final static String CONF_ID_ADRESS = "perf_id_adress";
	
	private Context context;
	
	private static AppConfig appConfig;
	
	/**
	 * ������ȡAppConfig�Ķ���
	 */
	public static AppConfig getAppConfig(Context context){
		if(appConfig == null){
			appConfig = new AppConfig();
			appConfig.context = context;
		}
		
		return appConfig;
	}
	
	/**
	 * ���浽���ü��϶���
	 */
	public void set(String key, String value){
		Properties properties = get();
		properties.setProperty(key, value);
		setProps(properties);
		
	}
	
	public void set(Properties ps){
		Properties properties = get();
		properties.putAll(ps);
		setProps(properties);
	}
	
	public void remove(String... key) {
		Properties properties = get();
		for(String k : key){
			properties.remove(k);
		}
		setProps(properties);
	}
	
	/**
	 * keyȡvalueֵ
	 */
	public String get(String key){
		Properties properties = get();
		
		return (properties != null) ? properties.getProperty(key) : null;
	}
	
	/**
	 * ��ȡ���ü��϶���
	 */
	public Properties get(){
		FileInputStream fis = null;
		Properties properties = new Properties();
		try{
			//��ȡapp_configĿ¼�µ�config
			File dirConf = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			
			fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);
			properties.load(fis);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return properties;
		
	}
	
	/**
	 * �������ü��϶���
	 */
	public Properties setProps(Properties properties){
		FileOutputStream fos = null;
		
		try {
			//��config���ڣ��Զ��壩app_config��Ŀ¼��
			File dirConf = context.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			
			File conf = new File(dirConf, APP_CONFIG);
			
			fos = new FileOutputStream(conf);
			
			properties.store(fos, null);
			
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return properties;
	}
}
