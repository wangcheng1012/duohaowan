package com.wlj.base.web;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.wlj.base.bean.Base;
import com.wlj.base.bean.BaseList;
import com.wlj.base.util.Log;
import com.wlj.base.util.RequestException;


/**
 * 
 * 添加请求参数 Map<String, Object> params 处理返回参数
 * 
 * @author wlj
 * 
 */
public class RequestWebClient {

	private static RequestWebClient requestWebClient;
	public static String key_page = "page";
	public static String key_pageSize = "pageSize";
	
	
	public static RequestWebClient getInstall() {
		if (requestWebClient == null) {
			requestWebClient = new RequestWebClient();
		}
		return requestWebClient;
	}

//	public User Login(User u) throws Exception {
//
//		String pwd = u.getPsw();
//
//		String randCode = u.getRandCode();
//
//		HttpPost hp = new HttpPost(new URL(URLs.login ));
//
//		if (randCode != null && !"".equals(randCode)) {// 验证码登录
//			hp.addParemeter("randCode", randCode);
//			hp.addParemeter("username", u.getName());
//			String psw = u.getPsw();
//			if(!"".equals(psw)){
//				hp.addParemeter("pwd_change", psw);
//			}
//			
//		} else {
//			String rand = System.currentTimeMillis() + "";
//			hp.addParemeter("username", u.getName());
//			hp.addParemeter("rand", rand);
//			hp.addParemeter("pwd",Md5Util.MD5Normal(Md5Util.MD5Normal(pwd) + rand));
//		}
//
//		String result = hp.getResult();
//		Log.e("Login", result);
//		JSONObject jSONObject = new JSONObject(result);
//		String state = jSONObject.getString("state");
//		// {"type":3,"key":"de3572e4391c92db60ad4ab4","state":2}
//		if (MsgContext.request_success.equals(state)) {
//
//			return (User) u.parse(jSONObject);
//		} else if (MsgContext.request_false.equals(state)
//				|| MsgContext.request_system_error.equals(state)) {
//
//			throw new RequestException(jSONObject.getString("description"));
//		}
//		throw new RequestException("获取数据失败");
//	}

//	public String getLoginRand(String phone) throws Exception {
//
//		HttpPost hp = new HttpPost(new URL( URLs.getLoginRand));
//
//		hp.addParemeter("username", phone);
//
//		String result = hp.getResult();
//		Log.e("getLoginRand", result);
//		JSONObject jSONObject = new JSONObject(result);
//
//		String state = jSONObject.getString("state");
//
//		if (MsgContext.request_success.equals(state)) {
//
//			return jSONObject.getString("description");
//		} else if (MsgContext.request_false.equals(state)
//				|| MsgContext.request_system_error.equals(state)) {
//
//			throw new RequestException(jSONObject.getString("description"));
//		}
//		throw new RequestException("获取数据失败");
//	}
	private JSONObject request(Map<String, Object> map,String key,String name) throws Exception {
		String request_data = null;
		
		if (map == null)
			throw new RequestException("参数为空");
		Object url = map.get("url");
		map.remove("url");
		if (url == null)
			throw new RequestException("地址为空");

		//是否需要加密
		if (map.containsKey("bujiami")) {
			// map 有不加密 key
			HttpPost hp = new HttpPost(new URL(url + ""));

			Set<String> set = map.keySet();
			for (String string : set) {
				hp.addParemeter(string, map.get(string)+"");
			}
			
			if (map.get(key_page) != null) {
				hp.addParemeter(key_page, map.get(key_page)+"");

				if (map.get(key_pageSize) == null) {
					hp.addParemeter(key_pageSize, MsgContext.pageSize + "");
				}
			}
			
//			if (name != null) {
				String login_rand = "" + System.currentTimeMillis();

				String login_mac = Md5Util.MD5Normal(key + login_rand);
				hp.addParemeter("login_name", name);
				hp.addParemeter("login_rand", login_rand);
				hp.addParemeter("login_mac", login_mac);
//			}
			request_data = hp.getResult();

		} else {
			// map 没有不加密 key，不管vaule是什么，都认为需要加密
			JSONObject json = new JSONObject();

			Set<String> set = map.keySet();
			for (String string : set) {
				json.put(string, map.get(string));
			}

			if (map.get(key_page) != null) {
				json.put(key_page, map.get(key_page));

				if (map.get(key_pageSize) == null) {
					json.put(key_pageSize, MsgContext.pageSize + "");
				}
			}

			request_data = BaseWebMain.request_data(url + "", key, name,
					json.toString());
		}

		if (request_data == null) {
			throw new RequestException("返回数据为空");
		}
		Log.w(url.toString().substring(url.toString().lastIndexOf("/")),request_data);

		return new JSONObject(request_data);
	}

	/**
	 * 自己定义返回
	 * 
	 * @param base 为null 则反回null
	 * @param map
	 * @throws Exception
	 */
	public BaseList Request(Map<String, Object> map, Base cla,String key,String name)throws Exception {
		JSONObject jsonObject = null;
		try {
			 jsonObject = request(map, key, name);
		} catch (Exception e) {
			if(e instanceof FileNotFoundException){
				throw new RequestException("地址错误(FileNotFoundException)");
			}else{
				throw e;
			}
		}
//		{"state":"3","description":"系统错误","message":"系统错误"}
			int state = jsonObject.optInt("statusCode");
			if(state ==  0){
				state = jsonObject.optInt("state");
			}
			if (MsgContext.request_success == state || MsgContext.request_success2 == state) {
				if (cla == null) {
					return null;
				}
				BaseList projectList = new BaseList();
				long millis = System.currentTimeMillis();

				projectList.parse(jsonObject, cla);
				Log.d("dd", (System.currentTimeMillis() - millis) + "  parse");
				return projectList;
			} else 
				if (MsgContext.request_false == state
					|| MsgContext.request_system_error == state 
					|| MsgContext.request_system_error2 == state) 
				{
				String optString = jsonObject.optString("message");
				if("".equals(optString)){
					 optString = jsonObject.optString("description");
				}
				throw new RequestException(optString);
			}
		
		throw new RequestException("数据解析失败");	
		
	}
}
