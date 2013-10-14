package com.lnsd.arcdemo.baseview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public abstract class Chart extends ViewGroup{

	public Chart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public Chart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public Chart(Context context) {
		super(context);
	}
}
