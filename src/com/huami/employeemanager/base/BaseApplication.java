package com.huami.employeemanager.base;

import java.util.Properties;
import java.util.UUID;

import com.huami.employeemanager.bean.User;
import com.huami.employeemanager.common.StringUtil;
import com.huami.employeemanager.activity.R;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * ȫ��Ӧ���࣬���ڱ���͵���ȫ������
 * 
 * @author NikolaQian
 *
 */
public class BaseApplication extends Application{
	//WiFi״̬ ��ͨ״̬ �ƶ�״̬
	private static int NETTYPE_WIFI = 0x01;
	private static int NETTYPE_CMWAP = 0x02;
	private static int NETTYPE_CMNET = 0x03;
	
	//��½״̬
	private boolean login = false;
	
	//��½ID
	private int loginID;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	/**
	 * ���ڷ�ת���Ƿ��Ǻ���
	 */
	public boolean isLandscape(){
		Configuration config = getResources().getConfiguration();
		
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
			return false;
		}else if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
			return true;
		}
		
		return false;
	}
	
	/**
	 * �Ƿ�ƽ���¼
	 */
	public boolean isTablet(){
//		return (getResources().getConfiguration().screenLayout
//                & Configuration.SCREENLAYOUT_SIZE_MASK)
//                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
                
		return getResources().getBoolean(R.bool.isTablet);
	}
	
	/**
	 * ��������Ƿ����
	 */
	public boolean isNetworkConnected(){
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	/**
	 * ��鵱ǰ��������
	 * 
	 * @return 0��û������ 1��WiFi���� 2��WAP���� 3��net����
	 */
	public int getNetworkType(){
		int netType = 0;
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo ni = cm.getActiveNetworkInfo();
		
		if(ni == null){
			return netType;
		}
		
		int nType = ni.getType();
		
		if(nType == ConnectivityManager.TYPE_MOBILE){
			String extraInfo = ni.getExtraInfo();
			if(!StringUtil.isEmpty(extraInfo)){
				//���Сд����cmnet
				if(extraInfo.toLowerCase().equals("cmnet")){
					netType = NETTYPE_CMNET;
				}else {
					netType = NETTYPE_CMWAP;
				}
			}
		}else if(nType == ConnectivityManager.TYPE_WIFI){
			netType = NETTYPE_WIFI;
		}
		
		return netType;
	}
	
	/**
	 * �жϵ�ǰ�汾�Ƿ����Ŀ��汾
	 */
	public static boolean isMethodsCompat(int versionCode){
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= versionCode;
	}
	
	/**
	 * ��ȡAPP��װ����Ϣ
	 */
	public PackageInfo getPackageInfo(){
		PackageInfo info = null;
		
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		if(info == null){
			info = new PackageInfo();
		}
		
		return info;
	}
	
	/**
	 * �Ƿ񷢳���ʾ��
	 */
	public boolean isVoice(){
		String perf_voice = getProperty(AppConfig.CONF_VOICE);
		
		//Ĭ���ǿ�����ʾ����
		if(StringUtil.isEmpty(perf_voice)){
			return true;
		}else {
			return StringUtil.toBool(perf_voice);
		}
	}
	
	/**
	 * ������ʾ��
	 */
	public void setConfigVoice(boolean b){
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
	}
	
	/**
	 * ����IP��ַ
	 */
	public void setIpAdress(String ipAdress){
		setProperty(AppConfig.CONF_ID_ADRESS, ipAdress);
	}
	
	/**
	 * �Ƿ������
	 */
	public boolean isCheckUp(){
		String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);
		
		//Ĭ���ǹرյ�
		if(StringUtil.isEmpty(perf_checkup)){
			return false;
		}else {
			return StringUtil.toBool(perf_checkup);
		}
	}
	
	/**
	 * ��ȡIP��ַ
	 */
	public String getIpAdress(){
		String ip_adress = getProperty(AppConfig.CONF_ID_ADRESS);
		
		if(StringUtil.isEmpty(ip_adress)){
			return "0.0.0.0";
		}else {
			return ip_adress;
		}
	}
	
	/**
	 * �����Ƿ�����������
	 */
	public void setConfigCheckUp(boolean b){
		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
	}
	
	/**
	 * ��ȡAPPΨһ��ʾ�����浽������
	 */
	public String getAppId(){
		String uniqeuId = getProperty(AppConfig.CON_APP_UNIQUEID);
		
		if(StringUtil.isEmpty(uniqeuId)){
			uniqeuId = UUID.randomUUID().toString();
			setProperty(AppConfig.CON_APP_UNIQUEID, uniqeuId);
		}
		
		return uniqeuId;
	}
	
	/**
	 * ��ʼ���û���Ϣ
	 */
	public void initLoginInfo(){
		User user = getLoginInfo();
		if(user != null && user.getUid() > 0 && user.isRememberMe()){
			this.login = true;
			this.loginID = user.getUid();
		}else {
			this.layout();
		}
		
	}
	
	/**
	 * �û��Ƿ��¼
	 */
	public boolean isLogin(){
		return login;
	}
	
	/**
	 * ��ȡ��¼�û�ID
	 */
	public int getLoginid(){
		return this.loginID;
	}
	
	/**
	 * �û�ע��
	 */
	public void layout(){
		login = false;
		loginID = 0;
	}

	/**
	 * ����û���¼��Ϣ
	 */
	public void clearLoginInfo(){
		layout();
		removeProperty("user.uid", "user.name", "user.password", "user.isRememberMe");
	}
	
	/**
	 * ��ȡ��¼��Ϣ
	 */
	public User getLoginInfo(){
		User user = new User();
		user.setUid(StringUtil.toInt(getProperty("user.uid"), 0));
		user.setName(getProperty("user.name"));
		user.setRememberMe(StringUtil.toBool(getProperty("user.isRememberMe")));
		user.setPassword(getProperty("user.password"));
		return user;
	}
	
	/**
	 * �����û���Ϣ
	 */
	public void saveLoginInfo(final User user){
		this.loginID = user.getUid();
		this.login = true;
		setProperties(new Properties(){
			{
				setProperty("user.uid", String.valueOf(user.getUid()));
				setProperty("user.name", user.getName());
				setProperty("user.password", user.getPassword());
				setProperty("user.isRememberMe", String.valueOf(user.isRememberMe()));
			}
		});
	}
	
	public boolean containsProperty(String key){
		Properties properties = getProperties();
		return properties.containsKey(key);
	}
	
	
	/**
	 * ��ȡ���ü���
	 */
	public Properties getProperties(){
		return AppConfig.getAppConfig(this).get();
	}
	
	public void setProperties(Properties properties){
		AppConfig.getAppConfig(this).set(properties);
	}
	
	public void setProperty(String key, String value){
		AppConfig.getAppConfig(this).set(key, value);
	}
	
	public String getProperty(String key){
		return AppConfig.getAppConfig(this).get(key);
	}
	
	public void removeProperty(String... key){
		AppConfig.getAppConfig(this).remove(key);
	}
	
	
	
}
