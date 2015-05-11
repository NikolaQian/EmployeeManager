package com.huami.employeemanager.common;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontHelper {
	public static void applyFont(Context context, TextView view, String path){
		Typeface customFont = Typeface.createFromAsset(context.getAssets(), path);
		view.setTypeface(customFont);
	}
}
