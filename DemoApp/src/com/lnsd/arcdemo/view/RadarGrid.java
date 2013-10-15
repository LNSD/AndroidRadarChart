package com.lnsd.arcdemo.view;

import java.util.ArrayList;
import java.util.List;

import com.lnsd.arcdemo.baseview.ChartGridView;
import com.lnsd.arcdemo.entity.GridLayerStyle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

/**
 * RadarGrid (ChartGridView)
 * @author Lorenzo Delgado Antequera - LNSD002
 * @Social:
 *	<a href="https://github.com/LNSD002">LNSD002 at Github</a> <br>
 *	<a href="https://twitter.com/LNSD002">LNSD002 at Twitter</a> <br>
 *	<a href="https://fb.me/LNSD002">LNSD002 at Facebook</a>
 */
public class RadarGrid extends ChartGridView {

	private static final String TAG = "RadarGrid";

	/*
	 * Constants
	 */	

	// Default values
	public static final int DEFAULT_LONGITUDE_LENGTH = 80;
	public static final int DEFAULT_LATITUDE_NUM = 4;
	public static final int DEFAULT_LONGITUDE_NUM = 4;
	public static final Point DEFAULT_ORIGIN_POSITION = new Point(0, 0);

	/*
	 * Variables
	 */

	// Grid parameters: Structure drawing
	private int lonNum = DEFAULT_LONGITUDE_NUM;
	private int latNum = DEFAULT_LATITUDE_NUM;

	private float maxValue = 1;		// Max value represented in the chart.
	private String labelsArray[];	// Labels array

	private int lonLength = DEFAULT_LONGITUDE_LENGTH;	
	private Point gridOrigin = DEFAULT_ORIGIN_POSITION;

	private GridLayerStyle style;

	/*
	 * Constructors
	 */

	public RadarGrid(Context context) {
		super(context);
		initView(DEFAULT_LONGITUDE_LENGTH);
	}	
	public RadarGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(DEFAULT_LONGITUDE_LENGTH);
	}
	public RadarGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(DEFAULT_LONGITUDE_LENGTH);
	}

	public RadarGrid(Context cnt, int lonNum, int latNum, float maxValue, String[] labelsArray){
		super(cnt);
		this.maxValue = maxValue;
		this.lonNum = lonNum;
		this.latNum = latNum;
		this.maxValue = maxValue;
		this.labelsArray = labelsArray;
		initView(DEFAULT_LONGITUDE_LENGTH);
	}
	public RadarGrid(Context cnt, int lonNum, int latNum, float maxValue, String[] labelsArray, GridLayerStyle style){
		super(cnt);
		this.maxValue = maxValue;
		this.lonNum = lonNum;
		this.latNum = latNum;
		this.maxValue = maxValue;
		this.labelsArray = labelsArray;
		this.style = style;
	}

	/*
	 * Grid init() & View size functions
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

	private List<PointF> pList;
	private Path mPath = new Path();
	private Rect mRect = new Rect();

	/**
	 * Calculate the points to draw the latitude lines.
	 * Gets web grid drawing path points.
	 */
	protected List<PointF> getGridPoints(float pos) {
		List<PointF> points = new ArrayList<PointF>();

		for (int i = 0; i < lonNum; i++) {
			PointF pt = new PointF();

			float offsetX = 
					(float) (gridOrigin.x - lonLength * pos	* Math.sin(i * 2 * Math.PI / lonNum));
			float offsetY = 
					(float) (gridOrigin.y - lonLength * pos	* Math.cos(i * 2 * Math.PI / lonNum));
			pt.set(offsetX, offsetY);

			points.add(pt);
		}
		return points;
	}	

	/*
	 * (non-Javadoc)
	 * @see com.lnsd.arcdemo.view.ChartGridView#onDraw(android.graphics.Canvas)
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.i(TAG,"onDraw called");

		// Get canvas measurements
		int cHeight = this.getHeight();
		int cWidth = this.getWidth();			
		Log.v(TAG, "Width/Height = "+cWidth+'/'+cHeight);

		// Calculate longitude length TODO Check lonLength calc.
		lonLength = (int) ((Math.min(cHeight, cWidth) / 2f) * 0.8);
		Log.v(TAG, "lonLength = "+lonLength);

		// Calculate graph origin
		gridOrigin.set(
				(int) (cWidth / 2f),
				(int) (cHeight / 2f));
		Log.v(TAG, "gridOrigin = "+gridOrigin);


		// Draw latitude grid lines: fill-background
		if(style.isLatAxis()){		
			switch (style.getGridChartType()) {
			case GridLayerStyle.ARC_RADAR_GRIDTYPE:
				canvas.drawCircle(gridOrigin.x, gridOrigin.y, lonLength, getPaintGridFill());
				break;
			case GridLayerStyle.ARC_SPIDER_WEB_GRIDTYPE:
				if(pList == null) pList = getGridPoints(1);

				for (int i = 0; i < lonNum; i++) {
					PointF pt = pList.get(i);
					if (i == 0) {
						mPath.moveTo(pt.x, pt.y);
					} else {
						mPath.lineTo(pt.x, pt.y);
					}
				}
				mPath.close();
				if(style.getBackgroundColor() != Color.TRANSPARENT) canvas.drawPath(mPath, getPaintGridFill());	
				break;
			}
		}

		// Draw longitude grid lines
		if(style.isLonAxis()){
			pList = getGridPoints(1);
			for (int i = 0; i < lonNum; i++) {
				PointF pt = pList.get(i);
				canvas.drawLine(gridOrigin.x, gridOrigin.y, pt.x, pt.y, getPaintGridLongitude());
			}
		}

		// Draw latitude grid lines: Grid border & Inner lines
		if(style.isLatAxis()){		
			switch (style.getGridChartType()) {
			case GridLayerStyle.ARC_RADAR_GRIDTYPE:
				canvas.drawCircle(gridOrigin.x, gridOrigin.y, lonLength, getPaintGridBorder());

				// Draw inner latitude lines
				for (int j = 1; j < latNum; j++) {
					canvas.drawCircle(gridOrigin.x, gridOrigin.y,
							lonLength * (j * 1f / latNum), getPaintGridLatitude());
				}
				break;

			case GridLayerStyle.ARC_SPIDER_WEB_GRIDTYPE:
				canvas.drawPath(mPath, getPaintGridBorder());
				mPath.reset();

				// Draw inner latitude lines 
				for(int j = 1; j < latNum; j++){
					pList = getGridPoints(j * 1f / latNum);

					for(int i = 0; i < lonNum; i++) {
						PointF pt = pList.get(i);
						if (i == 0) {
							mPath.moveTo(pt.x, pt.y);
						} else {
							mPath.lineTo(pt.x, pt.y);
						}
					}
					mPath.close();
					canvas.drawPath(mPath, getPaintGridLatitude());
					mPath.reset();
				}
				break;
			}
		}

		// Draw lonAxis scale values
		if(style.isLonAxisScale()){
			for (int k = 1; k <= latNum; k++) {
				String val = String.format("%.1f", (float) k * maxValue/latNum);

				float offsetX = (float) (gridOrigin.x + style.getGridScaleLabelPadding());
				float offsetY = (float) (gridOrigin.y - k * lonLength/latNum - style.getGridScaleLabelPadding());

				canvas.drawText(val, offsetX, offsetY, getPaintGScaleFont());
			}
		}

		// Draw grid labels
		if(labelsArray != null && style.isLabels()){
			if(labelsArray.length != lonNum) 
				throw new RuntimeException("Labels array length not matches longitude lines number.");
			else
				for (int j = 0; j < lonNum; j++) {
					String label = labelsArray[j];
					if(label.equals(null)) label = "";

					getPaintGLabelFont().getTextBounds(label, 0, label.length(), mRect);

					float offsetX = (float) (gridOrigin.x - mRect.width()/2
							- (lonLength + style.getGridLabelPadding()) * Math.sin(j * 2 * Math.PI / lonNum) );
					float offsetY = (float) (gridOrigin.y + mRect.height()/2
							- (lonLength + style.getGridLabelPadding()) * Math.cos(j * 2 * Math.PI / lonNum));

					canvas.drawText(label, offsetX, offsetY, getPaintGLabelFont());
				}
		} else if(labelsArray == null) Log.w(TAG, "ATT! >> labelsArray = null");	
	}

	/*
	 * Drawing tools getters
	 */

	private Paint getPaintGridFill() {
		Paint mPaintGridFill = new Paint();
		mPaintGridFill.setColor(style.getBackgroundColor());
		mPaintGridFill.setAntiAlias(true);
		return mPaintGridFill;
	}
	private Paint getPaintGridBorder() {
		Paint mPaintGridBorder = new Paint();
		mPaintGridBorder.setColor(style.getGridBorderColor());
		mPaintGridBorder.setStyle(Style.STROKE);
		mPaintGridBorder.setStrokeWidth(style.getGridBorderStrokeWidth());
		mPaintGridBorder.setAntiAlias(true);
		return mPaintGridBorder;
	}
	private Paint getPaintGridLatitude() {
		Paint mPaintGridLatitude = new Paint();
		mPaintGridLatitude.setColor(style.getGridLatitudeColor());
		mPaintGridLatitude.setStyle(Style.STROKE);
		mPaintGridLatitude.setStrokeWidth(style.getGridStrokeWidth());
		mPaintGridLatitude.setAntiAlias(true);
		return mPaintGridLatitude;
	}
	private Paint getPaintGridLongitude() {
		Paint mPaintGridLongitude = new Paint();
		mPaintGridLongitude.setColor(style.getGridLongitudeColor());
		mPaintGridLongitude.setStrokeWidth(style.getGridStrokeWidth());
		return mPaintGridLongitude;
	}
	private Paint getPaintGLabelFont() {
		Paint mPaintGLabelFont = new Paint();
		mPaintGLabelFont.setColor(style.getGridLabelColor());
		mPaintGLabelFont.setTextSize(style.getGridLabelSize());
		return mPaintGLabelFont;
	}
	private Paint getPaintGScaleFont() {
		Paint mPaintGScaleFont = new Paint();
		mPaintGScaleFont.setColor(style.getGridScaleColor());
		mPaintGScaleFont.setTextSize(style.getGridScaleSize());
		return mPaintGScaleFont;
	}
	
	/*
	 * Setters
	 * All of them call invalidate()
	 */

	// Grid form setters
	public void setGridChartType(int gridChartType) {
		style.setGridChartType(gridChartType);
		invalidate();
	}
	public void showLatAxis(boolean latAxis) {
		style.showLatAxis(latAxis);
		invalidate();
	}
	public void showLonAxis(boolean lonAxis) {
		style.showLonAxis(lonAxis);
		invalidate();
	}
	public void showLonAxisScale(boolean lonAxisScale) {
		style.showLonAxisScale(lonAxisScale);
		invalidate();
	}
	public void showLabels(boolean labels) {
		style.showLabels(labels);
		invalidate();
	}
	public void setLonNum(int lonNum) {
		this.lonNum = lonNum;
		invalidate();
	}
	public void setLatNum(int latNum) {
		this.latNum = latNum;
		invalidate();
	}
	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
		invalidate();
	}
	public void setLabelsArray(String[] labelsArray) {
		this.labelsArray = labelsArray;
		invalidate();
	}

	// Style setters
	public void setGridStyle(GridLayerStyle style){
		this.style = style;
		invalidate();
	}
	public GridLayerStyle getGridStyle(){
		return style;
	}

	public void setBackgroundColor(int backgroundColor) {
		style.setBackgroundColor(backgroundColor);
		invalidate();
	}
	public void setGridBorderColor(int gridBorderColor) {
		style.setGridBorderColor(gridBorderColor);
		invalidate();
	}
	public void setGridLatitudeColor(int gridLatitudeColor) {
		style.setGridLatitudeColor(gridLatitudeColor);
		invalidate();
	}
	public void setGridLongitudeColor(int gridLongitudeColor) {
		style.setGridLongitudeColor(gridLongitudeColor);
		invalidate();
	}
	public void setGridStrokeWidth(float gridStrokeWidth) {
		style.setGridStrokeWidth(gridStrokeWidth);
		invalidate();
	}
	public void setGridBorderStrokeWidth(float gridBorderStrokeWidth) {
		style.setGridBorderStrokeWidth(gridBorderStrokeWidth);
		invalidate();
	}
	public void setGridLabelColor(int gridLabelColor) {
		style.setGridLabelColor(gridLabelColor);
		invalidate();
	}
	public void setGridLabelSize(float gridLabelSize) {
		style.setGridLabelSize(gridLabelSize);
		invalidate();
	}
	public void setGridLabelPadding(float gridLabelPadding) {
		style.setGridLabelPadding(gridLabelPadding);
		invalidate();
	}
	public void setGridScaleColor(int gridScaleColor) {
		style.setGridScaleColor(gridScaleColor);
		invalidate();
	}
	public void setGridScaleSize(float gridScaleSize) {
		style.setGridScaleSize(gridScaleSize);
		invalidate();
	}
	public void setGridScaleLabelPadding(float gridScaleLabelPadding) {
		style.setGridScaleLabelPadding(gridScaleLabelPadding);
		invalidate();
	}
}
