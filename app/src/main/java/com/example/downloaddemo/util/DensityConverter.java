package com.example.downloaddemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

public class DensityConverter {

	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static Point getDisplayWidthAndHeight(Activity act){
		Display display = act.getWindowManager().getDefaultDisplay();
		Point outSize = new Point();
		display.getSize(outSize);
		return outSize;
	}
	
	/** 获取GridView 的宽高 */
	public static int getGridViewItemSize(Activity act){
		Point point = getDisplayWidthAndHeight(act);
		int screenWidth = point.x;
		int leftMagin = dp2px(act, 40);
		int rightMagin = dp2px(act, 16);
		int jiange = dp2px(act, 2 * 3);
		return (screenWidth - leftMagin - rightMagin - jiange) / 4;
	}
}