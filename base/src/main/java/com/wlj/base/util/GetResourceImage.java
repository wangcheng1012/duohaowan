package com.wlj.base.util;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class GetResourceImage {

	public static Bitmap get(Context context,int resId){
		
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		
		Bitmap bitmap = null;
		
		bitmap = BitmapFactory.decodeStream(is,null, opt);
		
		try {
			is.close();
		} catch (IOException e) {
			if(Log.LOG)e.printStackTrace();
		}
		return bitmap;
	}
	public static Drawable getDrawable(Context context,int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		//获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		
		Drawable drawable = Drawable.createFromStream(is, "src");
		
		try {
			is.close();
		} catch (IOException e) {
			if(Log.LOG)e.printStackTrace();
		}
		return drawable;
	
	}
	
	//没明白为什么不可以
//	public static Drawable getDrawable2(Context context,int resId){
//		
//		BitmapFactory.Options opt = new BitmapFactory.Options();
//
//		opt.inPreferredConfig = Bitmap.Config.RGB_565;
//
//		opt.inPurgeable = true;
//
//		opt.inInputShareable = true;
//
//		//获取资源图片
//
//		AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
//		
//		Bitmap bitmap = null;
////		FileInputStream in = null;
//		try {
//			FileDescriptor fileDescriptor = afd.getFileDescriptor();
////			in = afd.createInputStream();
//			bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor,null, opt);
//		}finally{
//			try {
////				in.close();
//				afd.close();
//				
//			} catch (IOException e) {
//				if(Log.LOG)e.printStackTrace();
//			}
//		}
//		return new BitmapDrawable(context.getResources(),bitmap);
//	}
}
