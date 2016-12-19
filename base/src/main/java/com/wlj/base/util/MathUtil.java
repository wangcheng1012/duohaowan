package com.wlj.base.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MathUtil {
	
	public static float parseFloat(String s){
		if(s==null){
			return 0f;
		}
		Float r = null;
		try{
			r = Float.parseFloat(s);
		}catch(NumberFormatException e){
			r=0f;
		}
		return r;
	}
	public static double parseDouble(String s){
		if(s==null){
			return 0d;
		}
		double r = 0d;
		try{
			r = Double.parseDouble(s);
		}catch(NumberFormatException e){
			r=0d;
		}
		return r;
	}
	public static long parseLong(String s){
		Long r = null;
		try{
			r = Long.parseLong(s);
		}catch(NumberFormatException e){
			r=0l;
		}
		return r;
	}
	/**
	 * return null if throw NumberFormatException  else return number of this string
	 * @param s
	 * @return 
	 */
	public static int parseInteger(String s){
		Integer r = null;
		try{
			r = Integer.parseInt(s);
		}catch(NumberFormatException e){
			r=0;
		}
		return r;
	}
	/**
	 * 去掉浮点数最后的0  比如浮点数  1024  转换为string 后就是 1024.0  了 需要把他转换成 1024 否者接口的money  如果传入1024.0 就会出现签名
	 * 验证失败 必须整成1024
	 * @param str
	 * @return
	 */
	public static String trimShu(String str) {
		if (str.indexOf(".") != -1 && str.charAt(str.length() - 1) == '0') {
			return trimShu(str.substring(0, str.length() - 1));
		} else {
			return str.charAt(str.length() - 1) == '.' ? str.substring(0, str.length() - 1) : str;
		}

	}
	
	/**
	 * obj1 +  obj2
	 * @param obj1
	 * @param obj2
	 * @return  相加结果
	 */
	public static BigDecimal add(Object obj1,Object obj2){
		if(obj1 == null){
			obj1 = "0";
		}
		if(obj2 == null){
			obj2 = "0";
		}
		BigDecimal bd1 = null;
		BigDecimal bd2 = null;
		if(obj1 instanceof BigDecimal){
			bd1 = (BigDecimal)obj1;
		}else{
			bd1 = new BigDecimal(obj1.toString());
		}
		if(obj2 instanceof BigDecimal){
			bd2 = (BigDecimal)obj2;
		}else{
			bd2 = new BigDecimal(obj2.toString());
		}
		return bd1.add(bd2);
	}
	/**
	 * obj1 - obj2
	 * @param obj1
	 * @param obj2
	 * @return 相减结果
	 */
	public static BigDecimal subtract(Object obj1,Object obj2){
		if(obj1 == null){
			obj1 = "0";
		}
		if(obj2 == null){
			obj2 = "0";
		}
		
		BigDecimal bd1 = null;
		BigDecimal bd2 = null;
		if(obj1 instanceof BigDecimal){
			bd1 = (BigDecimal)obj1;
		}else{
			bd1 = new BigDecimal(obj1.toString());
		}
		if(obj2 instanceof BigDecimal){
			bd2 = (BigDecimal)obj2;
		}else{
			bd2 = new BigDecimal(obj2.toString());
		}
		return bd1.subtract(bd2);
	}
	/**
	 * obj1 * obj2
	 * @param obj1
	 * @param obj2
	 * @return 相乘结果
	 */
	public static BigDecimal multiply(Object obj1,Object obj2){
		if(obj1 == null){
			obj1 = "0";
		}
		if(obj2 == null){
			obj2 = "0";
		}
		
		BigDecimal bd1 = null;
		BigDecimal bd2 = null;
		if(obj1 instanceof BigDecimal){
			bd1 = (BigDecimal)obj1;
		}else{
			bd1 = new BigDecimal(obj1.toString());
		}
		if(obj2 instanceof BigDecimal){
			bd2 = (BigDecimal)obj2;
		}else{
			bd2 = new BigDecimal(obj2.toString());
		}
		return bd1.multiply(bd2);
	}
	/**
	 * obj1 / obj2
	 * @param obj1
	 * @param obj2
	 * @param  limt 小数位数
	 * @return 相除保留4位 4舍5入
	 */
	public static BigDecimal divide(Object obj1, Object obj2, int limt) {
		if (StringUtils.isEmpty(obj1 + "")) {
			obj1 = "0";
		}
		if (StringUtils.isEmpty(obj2 + "")) {
			obj2 = "0";
		}
		
		BigDecimal bd1 = null;
		BigDecimal bd2 = null;
		if(obj1 instanceof BigDecimal){
			bd1 = (BigDecimal)obj1;
		}else{
			bd1 = new BigDecimal(obj1.toString());
		}
		if(obj2 instanceof BigDecimal){
			bd2 = (BigDecimal)obj2;
		}else{
			bd2 = new BigDecimal(obj2.toString());
		}
		return bd1.divide(bd2, limt, RoundingMode.HALF_EVEN);
	}

	/**
	 * obj1 / obj2
	 * @param obj1
	 * @param obj2
	 * @return  相除舍弃 余数
	 */
	public static BigDecimal divideDwon(Object obj1,Object obj2){
		if(obj1 == null){
			obj1 = "0";
		}
		if(obj2 == null){
			obj2 = "0";
		}

		BigDecimal bd1 = null;
		BigDecimal bd2 = null;
		if(obj1 instanceof BigDecimal){
			bd1 = (BigDecimal)obj1;
		}else{
			bd1 = new BigDecimal(obj1.toString());
		}
		if(obj2 instanceof BigDecimal){
			bd2 = (BigDecimal)obj2;
		}else{
			bd2 = new BigDecimal(obj2.toString());
		}
		return bd1.divide(bd2,0,RoundingMode.DOWN);
	}
}
