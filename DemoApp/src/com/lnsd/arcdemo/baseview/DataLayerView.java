package com.lnsd.arcdemo.baseview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public abstract class DataLayerView extends View {
	private static final String TAG = "DataLayerView"; 

	public DataLayerView(Context context) {
		super(context);
	}
	public DataLayerView(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	public DataLayerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * Called by measure() of the base view class. 
	 * This method is called only if a layout is requested on this child.
	 * Return after setting the required size through setMeasuredDimension
	 * The default onMeasure from View may be sufficient for you.
	 * But if you allow using wrap_conent for this view you may want to 
	 * override the wrap_content behavior which by default takes the entire space
	 * supplied. The following logic implments this properly by overriding
	 * onMeasure. This should be suitable for a number of cases, otherwise
	 * you have all the logic here.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getImprovedDefaultWidth(widthMeasureSpec),
				getImprovedDefaultHeight(heightMeasureSpec));
	}
	
	private int getImprovedDefaultHeight(int measureSpec) {
		//int result = size;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize =  MeasureSpec.getSize(measureSpec);

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			return hGetMaximumHeight();
		case MeasureSpec.EXACTLY:
			return specSize;
		case MeasureSpec.AT_MOST:
			return hGetMinimumHeight();
		}
		
		//you shouldn't come here
		Log.e(TAG,"Unknown Specmode");
		return specSize;
	}

	private int getImprovedDefaultWidth(int measureSpec) {
		//int result = size;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize =  MeasureSpec.getSize(measureSpec);

		switch (specMode) {
		case MeasureSpec.UNSPECIFIED:
			return hGetMaximumWidth();
		case MeasureSpec.EXACTLY:
			return specSize;
		case MeasureSpec.AT_MOST:
			return hGetMinimumWidth();
		}
		
		//you shouldn't come here
		Log.e(TAG,"Unknown Specmode");
		return specSize;
	}

	//Override these methods to provide a maximum size
	//("h" stands for hook pattern)
	abstract protected int hGetMaximumHeight();
	abstract protected int hGetMaximumWidth();

	protected int hGetMinimumHeight() {
		return this.getSuggestedMinimumHeight();
	}
	protected int hGetMinimumWidth() {
		return this.getSuggestedMinimumWidth();
	}

	/*
	 * Called by layout() of the View class
	 * By the time onLayout() is called the onSizeChanged
	 * is already called by layout().
	 * You typically override this when you are writing your own layouts.
	 * Then you will call the layout() on your children.
	 * 
	 * If you are customizing just a view you don't have to override this method.
	 * parent onLayout() is empty.
	 * 
	 * changed: this is a new size or position for this view
	 * rest: postions with respect to the parent
	 */
	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	/*
	 * This is called as part of layout() method from the base View class.
	 * onLayout() will be called subsequently.
	 * onLayout() is useful for view groups
	 * oldw and oldh are zero if you are newly added.
	 * View has already recorded the width and height
	 * invalidate() is already active and called by the super class
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w,h,oldw,oldh);
	}

}
