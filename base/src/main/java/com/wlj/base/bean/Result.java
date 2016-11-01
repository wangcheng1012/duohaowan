package com.wlj.base.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.wlj.base.util.AppException;
import com.wlj.base.util.StringUtils;

/**
 * 数据操作结果实体类
 * 
 * @author wlj
 * @version 1.0
 * @created 2015-4-10
 */
public class Result extends Base {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2563120909303123638L;
	private int errorCode;
	private String errorMessage;

	public boolean OK() {
		return errorCode == 1;
	}

	/**
	 * 解析调用结果
	 * 
	 * @param stream
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static Result parse(String str) throws AppException {
		Result res = null;
		InputStream stream = new ByteArrayInputStream(str.getBytes());
		// 获得XmlPullParser解析器
		XmlPullParser xmlParser = Xml.newPullParser();
		try {
			xmlParser.setInput(stream , Base.UTF8);
			// 获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。
			int evtType = xmlParser.getEventType();
			// 一直循环，直到文档结束
			while (evtType != XmlPullParser.END_DOCUMENT) {
				String tag = xmlParser.getName();
				switch (evtType) {

				case XmlPullParser.START_TAG:
					// 如果是标签开始，则说明需要实例化对象了
					if (tag.equalsIgnoreCase("result")) {
						res = new Result();
					} else if (res != null) {
						if (tag.equalsIgnoreCase("errorCode")) {
							res.errorCode = StringUtils.toInt(
									xmlParser.nextText(), -1);
						} else if (tag.equalsIgnoreCase("errorMessage")) {
							res.errorMessage = xmlParser.nextText().trim();
						}
					}
					break;
				case XmlPullParser.END_TAG:
					// 如果遇到标签结束，则把对象添加进集合中
					break;
				}
				// 如果xml没有结束，则导航到下一个节点
				evtType = xmlParser.next();
			}

		} catch (XmlPullParserException e) {
			throw AppException.xml(e);
		} catch (IOException e) {
			throw AppException.io(e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw AppException.io(e);
			}
		}
		return res;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return String.format("RESULT: CODE:%d,MSG:%s", errorCode, errorMessage);
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		Result result = new Result();
		result.setKey(jsonObject.optString("key"));
		result.setErrorCode(jsonObject.optInt("statusCode"));
		result.setErrorMessage(jsonObject.optString("message"));
		return result;
	}

}
