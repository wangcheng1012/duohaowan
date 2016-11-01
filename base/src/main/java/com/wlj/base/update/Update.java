package com.wlj.base.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.wlj.base.util.ExecutorServices;
import com.wlj.base.util.RequestException;

public class Update  {
	private UpdateInfo info = new UpdateInfo();

	private Context mContext;
	private String path;
	private NextStep nextStep;
	
	public static final int no_update = 5;
	public static final int version_exception = 1;
	public static final int service_timeout = -1;
	public static final int download_failed = -2;

	private boolean comeFromService;
	
	
	public void setComeFromService(boolean comeFromService) {
		this.comeFromService = comeFromService;
	}

	public enum state {
		no_update ,
		version_exception,
		service_timeout,
		download_failed,
		have_update
	}
	/**
	 * 
	 * @param mContext 
	 * @param path 服务器地址
	 * @param nextStep 后续操作
	 */
	public Update(Context mContext,String path,NextStep nextStep){
		this.mContext = mContext;
		this.path = path;
		this.nextStep = nextStep;
	}
	
	/**
	 * 获取当前程序的版本号
	 */
	public String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
		return packInfo.versionName;
	}
	/*
	 * 进入程序的主界面
	 */
	public interface NextStep{
		
		public void next(state what);
	}
	/**
	 *只检查是否有更新 就返回
	 */
	public void onlyCheck(){
		onlyCheck = true;
		ExecutorServices.getExecutorService().execute(new CheckVersionTask());
	}
	/**
	 * 检查更新 并弹出处更新提示框
	 */
	public void check(){
		
		ExecutorServices.getExecutorService().execute(new CheckVersionTask());
	}
	/*
	 * 从服务器获取xml解析并进行比对版本号
	 */
	private class CheckVersionTask implements Runnable {

		public void run() {
			try {
				
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				info.parse(is);

				UpdateInfo.setServerVersion(info.getVersion());
				UpdateInfo.setLocalVersion(getVersionName());
				
				String serviceversion = info.getVersion();
				if (serviceversion.equals(getVersionName())) {
					// Log.i(TAG,"版本号相同无需升级");
					Message msg = new Message();
					msg.what = no_update;
					handler.sendMessage(msg);
				} else {
					// Log.i(TAG,"版本号不同 ,提示用户升级 ");
					Message msg = new Message();
					msg.what = version_exception;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 连接服务器超时
				Message msg = new Message();
				msg.what = service_timeout;
				handler.sendMessage(msg);
			}
		}
	}
	/**
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		Builder builer = new Builder(mContext);
		builer.setTitle("版本升级");
		builer.setMessage(info.getDescription());
		builer.setCancelable(false);
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Log.i(TAG,"下载apk,更新");
				downLoadApk();
			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Message msg = new Message();
				msg.what = download_failed;
				msg.obj  =  null;
				handler.sendMessage(msg);
			}
		});
		AlertDialog dialog = builer.create();
		if(comeFromService){
			dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		dialog.show();
	}

	ProgressDialog pd;

	/**
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		// 进度条对话框
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		// 更新时候的启动按钮
		pd.setButton(DialogInterface.BUTTON_POSITIVE,"取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownLoadManager.setCancelUpdate(true);
			}
		});
		pd.setMessage("正在下载...");
		if(comeFromService){
			pd.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		pd.show();

		ExecutorServices.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
					Thread.sleep(3000);
					if (!DownLoadManager.isCancelUpdate()) {
						pd.dismiss(); // 结束掉进度条对话框
						installApk(file);
					}
				} catch (Exception e) {
					msg.what = download_failed;
					msg.obj = e;
				}
				pd.dismiss(); // 结束掉进度条对话框
				handler.sendMessage(msg);
			}
		});
	}

	// 安装apk
	protected void installApk(File file) {
		// System.out.println("开始安装");
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); 
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	private Handler handler = new Handler(Looper.myLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {

			// 不需要升级直接下一步
			case no_update:
				nextStep.next(state.no_update);
				break;

			// 对话框通知用户升级程序
			case version_exception:
				try {
					UpdateInfo.setServerVersion(info.getVersion());
					UpdateInfo.setLocalVersion(getVersionName());
					System.out.println("服务器上的版本号为1:----->" + info.getVersion());
					System.out.println("本地的版本号为1:----->" + getVersionName());

					if (!info.getVersion().equals("")&& Double.parseDouble(info.getVersion()) > Double.parseDouble(getVersionName())) {
						if(onlyCheck){
							nextStep.next(state.have_update);
						}else{
							showUpdataDialog();
						}
					} else {
						nextStep.next(state.version_exception);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mContext,"版本号异常" + UpdateInfo.getLocalVersion()+ "=ServiceVersion ：" + info.getVersion(),
							Toast.LENGTH_LONG).show();
					nextStep.next(state.version_exception);
				}
				
				break;

			// 服务器超时
			case service_timeout:
				Toast.makeText(mContext,"获取服务器更新信息超时",Toast.LENGTH_SHORT).show();
				nextStep.next(state.service_timeout);
				break;

			// 下载apk失败
			case download_failed:
				Object obj = msg.obj;
				
				if(obj instanceof RequestException){
					Exception e = (Exception)obj;
					Toast.makeText(mContext,e.getMessage()+"",Toast.LENGTH_SHORT).show();
				}else if(obj instanceof IOException ){
					Toast.makeText(mContext,"下载失败",Toast.LENGTH_SHORT).show();
				}
				nextStep.next(state.download_failed);
				break;
			}
		}
	};

	private boolean onlyCheck;

	public static Update parse(InputStream http_get) {
		// TODO Auto-generated method stub
		return null;
	}
}
