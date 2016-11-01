package com.wlj.base.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
/**
 * 
 * @author ly
 *
 */
public class HttpGet{
	
	private String urlStr ;
	private StringBuilder paremeter;
	private String charset = null;
	/**
	 * 
	 * @param urlStr url地址
	 * @throws IOException 如果这个报错就是连接根本没打开 
	 */
	public HttpGet(String urlStr) throws IOException{
		charset = "utf-8";
    	this.urlStr = urlStr;
    	paremeter = new StringBuilder();
	}
	/**
	 * 
	 * @param urlStr url地址
	 * @throws IOException 如果这个报错就是连接根本没打开 
	 */
	public HttpGet(String urlStr,String charset) throws IOException{
		if(charset == null){
			charset = "utf-8";
		}else{
			this.charset = charset; 
		}
		this.urlStr = urlStr;
		paremeter = new StringBuilder();
	}
	
	public String getResult() throws IOException{
		if(paremeter != null && paremeter.length()>1){
			paremeter = paremeter.deleteCharAt(paremeter.length()-1);
			this.urlStr = this.urlStr +"?"+paremeter;
		}
		
		URL url = new URL(this.urlStr);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		openConnection.setConnectTimeout(10000); 
    	openConnection.setReadTimeout(10000); 
		
		InputStream inputStream = null;
		ByteArrayOutputStream outByteArray = new ByteArrayOutputStream();
		inputStream = openConnection.getInputStream();
		int b;
		while (inputStream != null && (b = inputStream.read()) != -1) {
			outByteArray.write(b);
		} 
		byte[] returnByte = outByteArray.toByteArray();
		return new String(returnByte,charset);
	}
	
	public void addParemeter(String name,String value){ 
		paremeter.append(name).append("=").append(value).append("&");
	} 
	public void addParemeterEncoder(String name,String value){ 
		paremeter.append(name).append("=").append(URLEncoder.encode(value)).append("&");
	} 
}
