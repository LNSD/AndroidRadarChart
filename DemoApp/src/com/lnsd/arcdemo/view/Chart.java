package com.lnsd.arcdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;

public abstract class Chart extends ViewGroup{
	
	protected static final String TAG = "Chart";
	
	/*
	 * Constructors
	 */

	public Chart(Context context) {
		super(context);
	}
	public Chart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public Chart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * Chart inner components classes.
	 */

	protected abstract class ChartGridView extends View{
		public ChartGridView(Context context) {
			super(context);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			Log.d(TAG+".onMeasure()","Class = "+getClass().toString());
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			setMeasuredDimension(
					getImprovedDefaultWidth(widthMeasureSpec),
					getImprovedDefaultHeight(heightMeasureSpec));
		}

		private int getImprovedDefaultWidth(int measureSpec) {
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			switch (specMode) {
			case MeasureSpec.UNSPECIFIED:
				return hGetMaximumWidth();
			case MeasureSpec.EXACTLY:
				return specSize;
			case MeasureSpec.AT_MOST:
				return hGetMinimumWidth();
			}
			Log.e(TAG+".onMeasure(...)", "Unknown spec mode.");
			return specSize;
		}
		
		private int getImprovedDefaultHeight(int measureSpec) {
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			switch (specMode) {
			case MeasureSpec.UNSPECIFIED:
				return hGetMaximumHeight();
			case MeasureSpec.EXACTLY:
				return specSize;
			case MeasureSpec.AT_MOST:
				return hGetMinimumHeight();
			}
			Log.e(TAG+".onMeasure(...)", "Unknown spec mode.");
			return specSize;
		}
		
		//Override this methods to provide a maximum size.
		abstract protected int hGetMaximumHeight();
		abstract protected int hGetMaximumWidth();
		
		protected int hGetMinimumHeight(){
			return this.getSuggestedMinimumHeight();
		}
		protected int hGetMinimumWidth(){
			return this.getSuggestedMinimumWidth();
		}
		
		
	}
	protected abstract class LayerChartView extends View{
		public LayerChartView(Context context) {
			super(context);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			setMeasuredDimension(measureWidth(widthMeasureSpec),
					measureHeight(heightMeasureSpec));
		}

		private int measureWidth(int measureSpec) {
			int result = 0;
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			if (specMode == MeasureSpec.EXACTLY) {
				result = specSize;
			} else if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
			return result;
		}
		private int measureHeight(int measureSpec) {
			int result = 0;
			int specMode = MeasureSpec.getMode(measureSpec);
			int specSize = MeasureSpec.getSize(measureSpec);

			if (specMode == MeasureSpec.EXACTLY) {
				result = specSize;
			} else if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
			return result;
		}
	}
}
