package com.wlj.base.util;

import android.content.Context;
import android.util.TypedValue;
/**
 * dp与px互转
 * @author wlj
 *
 */
public class DpAndPx {

	public static int dpToPx(Context context, float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
	
	/**
	 * 像素转化dip
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue){

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int)(pxValue / scale + 0.5f);

	}

}
