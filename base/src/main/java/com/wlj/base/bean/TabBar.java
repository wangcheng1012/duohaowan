package com.wlj.base.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TabBar extends Base {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5261256844506989956L;
	private String title;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Base parse(JSONObject jsonObject) throws JSONException {
		return null;
	}

	 @Override
	public Base parseBean(JSONObject jsonObject) throws JSONException {
		return super.parseBean(jsonObject);
	}
	
}
