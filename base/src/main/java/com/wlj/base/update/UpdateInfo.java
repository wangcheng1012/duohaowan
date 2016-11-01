package com.wlj.base.update;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.wlj.base.bean.Base;
import com.wlj.base.util.AppException;

public class UpdateInfo extends Base {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1253578920138561513L;
	private String version;
	private String url;
	private String description;
	private static String content;
	private static String serverVersion;
	private static String localVersion;

	public static String getServerVersion() {
		return serverVersion;
	}

	public static void setServerVersion(String serverVersion) {
		UpdateInfo.serverVersion = serverVersion;
	}

	public static String getLocalVersion() {
		return localVersion;
	}

	public static void setLocalVersion(String localVersion) {
		UpdateInfo.localVersion = localVersion;
	}

	public static String getContent() {
		return content;
	}

	public static void setContent(String content) {
		UpdateInfo.content = content;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
	 */
	public UpdateInfo parse(InputStream is) throws AppException {
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "utf-8");
		// 设置解析的数据源
		int type = parser.getEventType();
		
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_TAG:
				if ("version".equals(parser.getName())) {
					setVersion(parser.nextText()); // 获取版本号
				} else if ("url".equals(parser.getName())) {
					setUrl(parser.nextText()); // 获取要升级的APK文件
				} else if ("description".equals(parser.getName())) {
					setDescription(parser.nextText()); // 获取该文件的信息
				} else if ("content".equals(parser.getName())) {
					UpdateInfo.setContent(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		} catch (XmlPullParserException e) {
			throw AppException.xml(e);
		} catch (IOException e) {
			throw AppException.io(e);
		}
		return this;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		
		return null;
		}


}
