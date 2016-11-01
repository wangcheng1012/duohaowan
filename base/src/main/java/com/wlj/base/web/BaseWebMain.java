package com.wlj.base.web;


import java.net.ConnectException;
import java.net.URL;

import com.wlj.base.util.RequestException;

import android.content.res.Resources.NotFoundException;

 /*
  *
 * @author lymava
  *
  */
public class BaseWebMain { 
	public static URL url = null;
	
	public static void main(String[] args) throws Exception {
		String urlString = "http://localhost:8080/chuang/face/userfront/getYuyueList.do";
		
		String key = "e5d50a5bb7a7e23c5441ef29";
    	String name = "facema"; 
    	String json = "{}";
		
		String request_data = request_data(urlString, key, name,json);
		
		System.out.println(request_data);
	} 
	
	public static final Integer client_type_xml = 1;
	public static final Integer client_type_json = 2;
	 
   public static String request_data(String url,String key,String name,String json) throws Exception {
   	
   	String randCode = System.currentTimeMillis()+"";
   	  
   	HttpPost hp = null; 
   	
   	String tempKey = Md5Util.MD5Normal(key+randCode);
		try {
			
			Boolean isEncryption = false;
	    	
	    	hp = new HttpPost(new URL(url));     
	    	hp.addParemeter("name", name);
	    	hp.addParemeter("randCode", randCode);
	    	hp.addParemeter("encrpt", "enAes");
	    	hp.addParemeter("isEncryption",isEncryption.toString());
	    	hp.addParemeter("mode",client_type_json+"");
			
			String reqxml =  json; 
			String enStr = reqxml; 
	    	hp.addParemeter("data", enStr);
	    	String mac = Md5Util.MD5Normal(tempKey+name+ enStr);
	    	hp.addParemeter("mac", mac);
	    	
		}  catch (Exception e) {            
			//缴费或查询失败 如果是缴费 这个当缴费失败处理  
			e.printStackTrace(); 
		}  
		
		String result = null; 
		try {  
			result = hp.getResult(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
   	  return result;
   }
	
	 /** 
	  * 进行一次充值缴费
     * @param args 
	 * @throws Exception  3des 类初始话失败
     */
    public static void request_test(Encrpt td,URL url) throws Exception {
    	
    	String key = "e5d50a5bb7a7e23c5441ef29";
    	String randCode = System.currentTimeMillis()+"";
    	String name = "facema"; 
    	  
    	HttpPost hp = null; 
    	
    	String tempKey = Md5Util.MD5Normal(key+randCode);
    	 
    	
		try {
			
			Boolean isEncryption = false;
	    	
	    	hp = new HttpPost(url);     
	    	hp.addParemeter("name", name);
	    	hp.addParemeter("randCode", randCode);
	    	hp.addParemeter("encrpt", "enAes");
	    	hp.addParemeter("isEncryption",isEncryption.toString());
	    	hp.addParemeter("mode",client_type_json+"");
			
			String reqxml =  "{}"; 
			String enStr = reqxml;
			if(isEncryption){
				enStr = td.encrypt(reqxml,tempKey);
			}
	    	hp.addParemeter("data", enStr);
	    	String mac = Md5Util.MD5Normal(tempKey+name+ enStr);
	    	hp.addParemeter("mac", mac);
	    	
		}  catch (Exception e) {            
			//缴费或查询失败 如果是缴费 这个当缴费失败处理  
			e.printStackTrace();   
		}  
		
		String result; 
		try {  
			result = hp.getResult(); 
			
			if(!result.contains("<") && !result.contains("{")){
				result = td.decrypt(result, tempKey); 
			}
			
			System.out.println(result); 
		} catch (ConnectException e) {
			//链接未打开缴费或查询失败 如果是缴费 这个当缴费失败处理 
			e.printStackTrace();
		} catch (Exception e){
			//解密失败或者通讯失败 如果是缴费两种失败都把这次缴费挂起 处理
			e.printStackTrace();
		}
    	  
    }
} 