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
 * 全局应用类，用于保存和调用全局配置
 * 
 * @author NikolaQian
 *
 */
public class BaseApplication extends Application{
	//WiFi状态 联通状态 移动状态
	private static int NETTYPE_WIFI = 0x01;
	private static int NETTYPE_CMWAP = 0x02;
	private static int NETTYPE_CMNET = 0x03;
	
	//登陆状态
	private boolean login = false;
	
	//登陆ID
	private int loginID;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	/**
	 * 现在翻转屏是否是横向
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
	 * 是否平板登录
	 */
	public boolean isTablet(){
//		return (getResources().getConfiguration().screenLayout
//                & Configuration.SCREENLAYOUT_SIZE_MASK)
//                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
                
		return getResources().getBoolean(R.bool.isTablet);
	}
	
	/**
	 * 检查网络是否可用
	 */
	public boolean isNetworkConnected(){
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	/**
	 * 检查当前网络类型
	 * 
	 * @return 0：没有网络 1：WiFi网络 2：WAP网络 3：net网络
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
				//如果小写等于cmnet
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
	 * 判断当前版本是否兼容目标版本
	 */
	public static boolean isMethodsCompat(int versionCode){
		int currentVersion = Build.VERSION.SDK_INT;
		return currentVersion >= versionCode;
	}
	
	/**
	 * 获取APP安装包信息
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
	 * 是否发出提示音
	 */
	public boolean isVoice(){
		String perf_voice = getProperty(AppConfig.CONF_VOICE);
		
		//默认是开启提示声音
		if(StringUtil.isEmpty(perf_voice)){
			return true;
		}else {
			return StringUtil.toBool(perf_voice);
		}
	}
	
	/**
	 * 设置提示音
	 */
	public void setConfigVoice(boolean b){
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
	}
	
	/**
	 * 设置IP地址
	 */
	public void setIpAdress(String ipAdress){
		setProperty(AppConfig.CONF_ID_ADRESS, ipAdress);
	}
	
	/**
	 * 是否检查更新
	 */
	public boolean isCheckUp(){
		String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);
		
		//默认是关闭的
		if(StringUtil.isEmpty(perf_checkup)){
			return false;
		}else {
			return StringUtil.toBool(perf_checkup);
		}
	}
	
	/**
	 * 获取IP地址
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
	 * 设置是否启动检查更新
	 */
	public void setConfigCheckUp(boolean b){
		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
	}
	
	/**
	 * 获取APP唯一表示并保存到配置中
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
	 * 初始化用户信息
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
	 * 用户是否登录
	 */
	public boolean isLogin(){
		return login;
	}
	
	/**
	 * 获取登录用户ID
	 */
	public int getLoginid(){
		return this.loginID;
	}
	
	/**
	 * 用户注销
	 */
	public void layout(){
		login = false;
		loginID = 0;
	}

	/**
	 * 清除用户登录信息
	 */
	public void clearLoginInfo(){
		layout();
		removeProperty("user.uid", "user.name", "user.password", "user.isRememberMe");
	}
	
	/**
	 * 获取登录信息
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
	 * 保存用户信息
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
	 * 获取配置集合
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
