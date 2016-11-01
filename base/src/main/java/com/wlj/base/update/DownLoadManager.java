package com.wlj.base.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.wlj.base.util.AppConfig;
import com.wlj.base.util.RequestException;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownLoadManager {

	private static boolean cancelUpdate = false;

	public static boolean isCancelUpdate() {
		return cancelUpdate;
	}

	public static void setCancelUpdate(boolean cancelUpdate) {
		DownLoadManager.cancelUpdate = cancelUpdate;
	}

	public static File getFileFromServer(String path, ProgressDialog pd) throws IOException, RequestException  
			  {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
//			http://121.40.177.251:6940/LSYiGou.apk
			
			File file = new File(AppConfig.getAppConfig().getImagePath(),path.substring(path.lastIndexOf("/"),path.length()));
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 
				pd.setProgress(total);
				if (isCancelUpdate()) {
					fos.close();
					bis.close();
					is.close();
					throw new RequestException("取消下载成功");
				}
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		throw new RequestException("没找到sd卡");
	}
}
