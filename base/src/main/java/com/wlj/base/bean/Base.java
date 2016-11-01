package com.wlj.base.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import com.wlj.base.util.AppContext;
import com.wlj.base.util.StringUtils;

/**
 * 实体基类：实现序列化
 * 
 * @author wlj
 * @version 1.0
 * @created 2012-3-21
 */
public abstract class Base implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6547736466184868569L;

	public final static String UTF8 = "UTF-8";
	private String id;
	private String key;
	private String resultStr;

	public Base(){}
	public Base(JSONObject paramJSONObject) {
		resultStr = paramJSONObject.toString();
	}


	public abstract Base parse(JSONObject jsonObject) throws JSONException;

	public List<Base> parseList(JSONArray data) throws JSONException {
		
		List<Base> list = new ArrayList<Base>();
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject tmp = data.optJSONObject(i);
			list.add(parse(tmp));
		}
		return list;
	};
	public void getThisfromid(final AppContext mContext, final Handler handle){ };
	/**
	 * <li>jsonObject 的数据没在 data里面，就按照他自己的格式单独去解析,默认直接掉用parse解析</li>
	 * <li>这个方法是对特殊jsonObject解析用</li>
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public  Base parseBean(JSONObject jsonObject)throws JSONException{
		
		return parse(jsonObject);
	};
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getResultStr() {
		return resultStr;
	}

	public JSONObject getResultJsonObject() {
		if(!StringUtils.isEmpty(resultStr)){

			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(resultStr);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonObject;
		}
		return null;
	}


}
