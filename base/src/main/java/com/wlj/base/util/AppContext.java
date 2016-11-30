package com.wlj.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;
import java.util.UUID;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * @author wlj
 * @version 1.0
 * @created 2012-3-21
 */
public class AppContext extends Application {
	
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	
	public static final int PAGE_SIZE = 20;//默认分页大小
	private static final int CACHE_TIME = 60*60000;//缓存失效时间
	private static AppContext appContext;
	private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	private String saveImagePath;//保存图片路径
	private RefWatcher refWatcher;

	public static AppContext getAppContext() {
		return appContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        //注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
        appContext = this;
		refWatcher = LeakCanary.install(this);
        init();
	}


	public  RefWatcher getRefWatcher( ) {
		return  refWatcher;
	}

	/**
	 * 初始化
	 */
	protected void init() {

	}
	
	public boolean islogin(){
		
		String property = getProperty(AppConfig.CONF_KEY);
		
		if(!"".equals(property)){
			return true;
		}
		
		return false;
	}

	public void loginOut() {
		setProperty(AppConfig.CONF_KEY,"");
		setProperty(AppConfig.CONF_NAME, "");
	}

	/**
	 * 判断是否有网络
	 */
	public boolean isNetworkAvailable() {
		if (checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
				checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
				) {

			return false;
		} else {
			ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity == null) {
				Logger.w("Utility", "couldn't get connectivity manager");
			} else {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].isAvailable()) {
//							Logger.d( "network is available");
							return true;
						}
					}
				}
			}
		}
		Logger.d("Utility", "network is not available");
		return false;
	}

//	/**
//	 * 检测当前系统声音是否为正常模式
//	 * @return
//	 */
//	public boolean isAudioNormal() {
//		AudioManager mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
//		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
//	}
//
//	/**
//	 * 应用程序是否发出提示音
//	 * @return
//	 */
//	public boolean isAppSound() {
//		return isAudioNormal() && isVoice();
//	}
//
//	/**
//	 * 检测网络是否可用
//	 * @return
//	 */
//	public boolean isNetworkConnected() {
//		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo ni = cm.getActiveNetworkInfo();
//		return ni != null && ni.isConnectedOrConnecting();
//	}
//
//	/**
//	 * 获取当前网络类型
//	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
//	 */
//	public int getNetworkType() {
//		int netType = 0;
//		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//		if (networkInfo == null) {
//			return netType;
//		}
//		int nType = networkInfo.getType();
//		if (nType == ConnectivityManager.TYPE_MOBILE) {
//			String extraInfo = networkInfo.getExtraInfo();
//			if(!StringUtils.isEmpty(extraInfo)){
//				if (extraInfo.toLowerCase().equals("cmnet")) {
//					netType = NETTYPE_CMNET;
//				} else {
//					netType = NETTYPE_CMWAP;
//				}
//			}
//		} else if (nType == ConnectivityManager.TYPE_WIFI) {
//			netType = NETTYPE_WIFI;
//		}
//		return netType;
//	}
	/**
	 * 判断缓存数据是否可读
	 * @param cachefile
	 * @return
	 */
	public boolean isReadDataCache(String cachefile)
	{
		return readObject(cachefile) != null;
	}
//	/**
//	 * 判断当前版本是否兼容目标版本的方法
//	 * @param VersionCode
//	 * @return
//	 */
//	public static boolean isMethodsCompat(int VersionCode) {
//		int currentVersion = Build.VERSION.SDK_INT;
//		return currentVersion >= VersionCode;
//	}
//
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if(info == null) info = new PackageInfo();
		return info;
	}
//
//	/**
//	 * 获取App唯一标识
//	 * @return
//	 */
//	public String getAppId() {
//		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
//		if(StringUtils.isEmpty(uniqueID)){
//			uniqueID = UUID.randomUUID().toString();
//			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
//		}
//		return uniqueID;
//	}
//
//	/**
//	 * 设置是否加载文章图片
//	 * @param b
//	 */
//	public void setConfigLoadimage(boolean b)
//	{
//		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
//	}
//
//	/**
//	 * 是否发出提示音
//	 * @return
//	 */
//	public boolean isVoice()
//	{
//		String perf_voice = getProperty(AppConfig.CONF_VOICE);
//		//默认是开启提示声音
//		if(StringUtils.isEmpty(perf_voice))
//			return true;
//		else
//			return StringUtils.toBool(perf_voice);
//	}
//
//	/**
//	 * 设置是否发出提示音
//	 * @param b
//	 */
//	public void setConfigVoice(boolean b)
//	{
//		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
//	}
//
//	/**
//	 * 是否启动检查更新
//	 * @return
//	 */
//	public boolean isCheckUp()
//	{
//		String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);
//		//默认是开启
//		if(StringUtils.isEmpty(perf_checkup))
//			return true;
//		else
//			return StringUtils.toBool(perf_checkup);
//	}
//
//	/**
//	 * 设置启动检查更新
//	 * @param b
//	 */
//	public void setConfigCheckUp(boolean b)
//	{
//		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
//	}
//
//	/**
//	 * 是否左右滑动
//	 * @return
//	 */
//	public boolean isScroll()
//	{
//		String perf_scroll = getProperty(AppConfig.CONF_SCROLL);
//		//默认是关闭左右滑动
//		if(StringUtils.isEmpty(perf_scroll))
//			return false;
//		else
//			return StringUtils.toBool(perf_scroll);
//	}
//
//	/**
//	 * 设置是否左右滑动
//	 * @param b
//	 */
//	public void setConfigScroll(boolean b)
//	{
//		setProperty(AppConfig.CONF_SCROLL, String.valueOf(b));
//	}
//
//	/**
//	 * 是否Https登录
//	 * @return
//	 */
//	public boolean isHttpsLogin()
//	{
//		String perf_httpslogin = getProperty(AppConfig.CONF_HTTPS_LOGIN);
//		//默认是http
//		if(StringUtils.isEmpty(perf_httpslogin))
//			return false;
//		else
//			return StringUtils.toBool(perf_httpslogin);
//	}
//
//	/**
//	 * 设置是是否Https登录
//	 * @param b
//	 */
//	public void setConfigHttpsLogin(boolean b)
//	{
//		setProperty(AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
//	}
//
//	/**
//	 * 清除保存的缓存
//	 */
//	public void cleanCookie()
//	{
//		removeProperty(AppConfig.CONF_COOKIE);
//	}
//
	/**
	 * 判断缓存是否存在
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile)
	{
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if(data.exists())
			exist = true;
		return exist;
	}
//
//	/**
//	 * 判断缓存是否失效
//	 * @param cachefile
//	 * @return
//	 */
//	public boolean isCacheDataFailure(String cachefile)
//	{
//		boolean failure = false;
//		File data = getFileStreamPath(cachefile);
//		if(data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
//			failure = true;
//		else if(!data.exists())
//			failure = true;
//		return failure;
//	}
//
//	/**
//	 *
//	 * 清除app缓存
//	 */
//	public void clearAppCache()
//	{
//		//清除webview缓存
//		File file = getCacheDir();
//		if (file != null && file.exists() && file.isDirectory()) {
//		    for (File item : file.listFiles()) {
//		    	item.delete();
//		    }
//		    file.delete();
//		}
//		deleteDatabase("webview.db");
//		deleteDatabase("webview.db-shm");
//		deleteDatabase("webview.db-wal");
//		deleteDatabase("webviewCache.db");
//		deleteDatabase("webviewCache.db-shm");
//		deleteDatabase("webviewCache.db-wal");
//		//清除数据缓存
//		clearCacheFolder(getFilesDir(),System.currentTimeMillis());
//		clearCacheFolder(getCacheDir(),System.currentTimeMillis());
//		//2.2版本才有将应用缓存转移到sd卡的功能
//		if(isMethodsCompat(Build.VERSION_CODES.FROYO)){
//			clearCacheFolder(getExternalCacheDir(),System.currentTimeMillis());
//		}
//		//清除编辑器保存的临时内容
//		Properties props = getProperties();
//		for(Object key : props.keySet()) {
//			String _key = key.toString();
//			if(_key.startsWith("temp"))
//				removeProperty(_key);
//		}
//	}
//
//	/**
//	 * 清除缓存目录
//	 * @param dir 目录
//	 * @param numDays 当前系统时间
//	 * @return
//	 */
//	private int clearCacheFolder(File dir, long curTime) {
//	    int deletedFiles = 0;
//	    if (dir!= null && dir.isDirectory()) {
//	        try {
//	            for (File child:dir.listFiles()) {
//	                if (child.isDirectory()) {
//	                    deletedFiles += clearCacheFolder(child, curTime);
//	                }
//	                if (child.lastModified() < curTime) {
//	                    if (child.delete()) {
//	                        deletedFiles++;
//	                    }
//	                }
//	            }
//	        } catch(Exception e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    return deletedFiles;
//	}
//
//	/**
//	 * 将对象保存到内存缓存中
//	 * @param key
//	 * @param value
//	 */
//	public void setMemCache(String key, Object value) {
//		memCacheRegion.put(key, value);
//	}
//
//	/**
//	 * 从内存缓存中获取对象
//	 * @param key
//	 * @return
//	 */
//	public Object getMemCache(String key){
//		return memCacheRegion.get(key);
//	}
//
	/**
	 * 保存磁盘缓存
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try{
			fos = openFileOutput("cache_"+key+".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		}finally{
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
//
//	/**
//	 * 获取磁盘缓存数据
//	 * @param key
//	 * @return
//	 * @throws IOException
//	 */
//	public String getDiskCache(String key) throws IOException {
//		FileInputStream fis = null;
//		try{
//			fis = openFileInput("cache_"+key+".data");
//			byte[] datas = new byte[fis.available()];
//			fis.read(datas);
//			return new String(datas);
//		}finally{
//			try {
//				fis.close();
//			} catch (Exception e) {}
//		}
//	}
//
	/**
	 * 保存对象
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}

	/**
	 * 读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file){
		if(!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}catch(FileNotFoundException e){
		}catch(Exception e){
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if(e instanceof InvalidClassException){
				File data = getFileStreamPath(file);
				data.delete();
			}
		}finally{
			try {
				ois.close();
			} catch (Exception e) {}
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return null;
	}
//
//	public boolean containsProperty(String key){
//		Properties props = getProperties();
//		 return props.containsKey(key);
//	}
//
//	public void setProperties(Properties ps){
//		AppConfig.getAppConfig(this).set(ps);
//	}
//
//	public Properties getProperties(){
//		return AppConfig.getAppConfig(this).get();
//	}
//
	public void setProperty(String key,String value){
		AppConfig.getAppConfig().set(key, value);
	}

	public String getProperty(String key){
		return AppConfig.getAppConfig().get(key);
	}


//	public void removeProperty(String...key){
//		AppConfig.getAppConfig(this).remove(key);
//	}
//
//	/**
//	 * 获取内存中保存图片的路径
//	 * @return
//	 */
//	public String getSaveImagePath() {
//		return saveImagePath;
//	}
//	/**
//	 * 设置内存中保存图片的路径
//	 * @return
//	 */
//	public void setSaveImagePath(String saveImagePath) {
//		this.saveImagePath = saveImagePath;
//	}
//
//	/**
//	 * User-Agent
//	 *
//	 * @return user-agent
//	 */
//	public String getUser_Agent() {
//		String ua = "Android;"
//			+ Build.VERSION.RELEASE + ";"
//			+ Build.BRAND + "-" + Build.MODEL + ";";
//
//		ua = ua + getDevicNO()  + ";" + System.currentTimeMillis() + ";";
//
//		return ua;
//	}
//
//	/**
//	 * @return 手机串号 | MAC地址
//	 */
//	public String getDevicNO() {
//		if(!TextUtils.isEmpty(getDeviceId())) {
//			return getDeviceId();
//		}
//		if(!TextUtils.isEmpty(getMacAddress())) {
//			return getMacAddress();
//		}
//		return " ";
//	}
//	/**
//	 * @return 手机串号:GSM手机的 IMEI 和 CDMA手机的 MEID.
//	 */
//	public String getDeviceId() {
//		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		if(telephonyManager != null ) {
//			return telephonyManager.getDeviceId();
//		}
//
//		return null;
//	}
//	/**
//	 * @return MAC地址
//	 */
//	public String getMacAddress() {
//		// start get mac address
//		WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		if (wifiMan != null) {
//			WifiInfo wifiInf = wifiMan.getConnectionInfo();
//			if (wifiInf != null && wifiInf.getMacAddress() != null) {
//				// 48位，如FA:34:7C:6D:E4:D7
//				return wifiInf.getMacAddress();
//			}
//		}
//		return null;
//	}
	
}
