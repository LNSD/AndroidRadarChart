package com.lnsd.arcdemo.view;

import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;

import com.lnsd.arcdemo.baseview.DataLayerView;
import com.lnsd.arcdemo.entity.ARCDataLayer;
import com.lnsd.arcdemo.entity.DataLayerStyle;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

public class RadarDataLayer extends DataLayerView {

	private static final String TAG = "RadarDataLayer";

	/*
	 * Constants
	 */	

	public static final int DEFAULT_LONGITUDE_NUM = 4;
	public static final int DEFAULT_LONGITUDE_LENGTH = 80;
	public static final Point DEFAULT_ORIGIN_POSITION = new Point(0, 0);

	/*
	 * Variables
	 */

	private ARCDataLayer data;
	private ARCDataLayer animData;
	private DataLayerStyle cLayerParams = new DataLayerStyle();
	private ValueAnimator anim;

	private boolean points = true;

	private int lonNum = DEFAULT_LONGITUDE_NUM;	
	private int lonLength = DEFAULT_LONGITUDE_LENGTH;	
	private Point gridOrigin = DEFAULT_ORIGIN_POSITION;

	private float maxValue = 1;

	/*
	 * Constructors
	 */

	public RadarDataLayer(Context context) {
		super(context);
		initView(DEFAULT_LONGITUDE_LENGTH);
	}	
	public RadarDataLayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(DEFAULT_LONGITUDE_LENGTH);
	}
	public RadarDataLayer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(DEFAULT_LONGITUDE_LENGTH);
	}

	public RadarDataLayer(Context cnt, ARCDataLayer layerData){
		super(cnt);
		this.maxValue = layerData.getMaxValue();
		this.cLayerParams = layerData.getLayerStyle();
		this.lonNum = layerData.size();
		this.data = layerData;
		animateLayer(layerData.getInterpolator(), layerData.getDuration());
	}
	public RadarDataLayer(Context cnt, float max, ARCDataLayer layerData){
		super(cnt);
		this.maxValue = max;
		this.cLayerParams = layerData.getLayerStyle();
		this.lonNum = layerData.size();
		this.data = layerData;
		animateLayer(layerData.getInterpolator(), layerData.getDuration());
	}

	/*
	 * DataLayer init() & View size functions
	 */

	public void initView(int lonLength) {
		this.setMinimumHeight(2 * lonLength);
		this.setMinimumWidth(2 * lonLength);
	}

	@Override
	protected int hGetMaximumHeight() {
		return lonLength * 2;
	}
	@Override
	protected int hGetMaximumWidth() {
		return lonLength * 2;
	} 

	/*
	 * Drawing functions
	 */

	private Path mPath = new Path();

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Get canvas measurements
		int cHeight = this.getHeight();
		int cWidth = this.getWidth();			

		// Calculate longitude length TODO Check lonLength calc.
		lonLength = (int) ((Math.min(cHeight, cWidth) / 2f) * 0.8);

		// Calculate graph origin
		gridOrigin.set(
				(int) (cWidth / 2f),
				(int) (cHeight / 2f));

		if(data == null) throw new RuntimeException("Error: NullPointerException at data.");
		else if(null != data) {


			int i = 0;
			for (Map.Entry<String,Float> entry: (animData == null)?data.entrySet():animData.entrySet()) {

				// Get values from data layer.
				float offsetX = (float) (gridOrigin.x - entry.getValue() / maxValue
						* lonLength * Math.sin(i * 2 * Math.PI / lonNum));
				float offsetY = (float) (gridOrigin.y - entry.getValue() / maxValue
						* lonLength * Math.cos(i * 2 * Math.PI / lonNum));


				// Draw data layer polygon.
				if (i == 0) {
					mPath.moveTo(offsetX, offsetY);
				} else {
					mPath.lineTo(offsetX, offsetY);
				}

				if(points) 
					canvas.drawCircle(offsetX, offsetY, 3, getPaintLayerPoint());

				i++;
			}

			mPath.close();
			canvas.drawPath(mPath, getPaintLayerFilling());
			canvas.drawPath(mPath, getPaintLayerBorder());
			mPath.reset();
		}
	}

	/*
	 * Drawing tools getters
	 */

	private Paint getPaintLayerFilling() {
		Paint mPaintFill = new Paint();
		mPaintFill.setColor(cLayerParams.getLayerFillColor());
		mPaintFill.setStyle(Style.FILL);
		mPaintFill.setAntiAlias(true);
		mPaintFill.setAlpha(cLayerParams.getLayerFillAlpha());
		return mPaintFill;
	}
	private Paint getPaintLayerBorder() {
		Paint mPaintBorder = new Paint();
		mPaintBorder.setColor(cLayerParams.getLayerBorderColor());
		mPaintBorder.setStyle(Style.STROKE);
		mPaintBorder.setStrokeWidth(cLayerParams.getLayerBorderWidth());
		mPaintBorder.setAntiAlias(true);
		return mPaintBorder;
	}
	private Paint getPaintLayerPoint() {
		Paint mPaintPoint = new Paint();
		mPaintPoint.setColor(cLayerParams.getLayerBorderColor());
		mPaintPoint.setStrokeWidth(cLayerParams.getLayerBorderWidth());
		return mPaintPoint;
	}

	/*
	 *	Animation methods 
	 */

	public void animateLayer(Interpolator inter, long duration){
		if(duration > 0){
			this.anim = ValueAnimator.ofFloat(0, 1);
			this.animData = new ARCDataLayer(data);
			this.anim.setDuration(duration);
			this.anim.setInterpolator(inter);

			this.anim.addUpdateListener(new AnimatorUpdateListener() {

				@Override public void onAnimationUpdate(ValueAnimator animation) {
					float scale = (Float) animation.getAnimatedValue();

					for (Map.Entry<String,Float> entry: data.entrySet()) {
						animData.put(entry.getKey(), scale*entry.getValue());
						invalidate();
					}
				}
			});
		}
	}
	public void startAnimation(){
		if(anim != null) this.anim.start();
	}
	
	/*
	 * Setters
	 * All of them call invalidate()
	 */

	public void setData(ARCDataLayer data) {
		this.data = data;
		invalidate();
	}
	public void setLayerStyle(DataLayerStyle layerStyle) {
		this.cLayerParams = layerStyle;
		invalidate();
	}
	public void showPoints(boolean points) {
		this.points = points;
		invalidate();
	}
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		invalidate();
	}
}
