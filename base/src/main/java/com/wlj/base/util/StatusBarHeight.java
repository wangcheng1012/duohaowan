package com.wlj.base.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;

/**
 * 获取通知栏高度
 * @author wlj
 *
 */
public class StatusBarHeight {

	  public static int getStatusBarHeight() {
	        return Resources.getSystem().getDimensionPixelSize(
	                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
	    }
	  
	  public static int getStatusBarHeight3(Context context){
	        Class<?> c = null;
	        Object obj = null;
	        Field field = null;
	        int x = 0, statusBarHeight = 0;
	        try {
	            c = Class.forName("com.android.internal.R$dimen");
	            obj = c.newInstance();
	            field = c.getField("status_bar_height");
	            x = Integer.parseInt(field.get(obj).toString());
	            statusBarHeight = context.getResources().getDimensionPixelSize(x);
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	        return statusBarHeight;
	    }
}
