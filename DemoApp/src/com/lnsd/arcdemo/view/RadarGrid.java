package com.lnsd.arcdemo.view;

import java.util.ArrayList;
import java.util.List;

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

	// Chart types
	public static final int ARC_SPIDER_WEB_GRIDTYPE = 0;
	public static final int ARC_RADAR_GRIDTYPE = 1;

	// Default values
	public static final int DEFAULT_LONGITUDE_LENGTH = 80;
	public static final int DEFAULT_LATITUDE_NUM = 4;
	public static final int DEFAULT_LONGITUDE_NUM = 4;
	public static final Point DEFAULT_ORIGIN_POSITION = new Point(0, 0);

	public static final int DEFAULT_GBACKGROUND_COLOR = Color.TRANSPARENT;
	public static final int DEFAULT_GLATITUDE_COLOR = Color.BLACK;
	public static final int DEFAULT_GLONGITUDE_COLOR = Color.BLACK;
	public static final float DEFAULT_GSTROKE_WIDTH = 2f;

	public static final int DEFAULT_GLABEL_COLOR = Color.BLACK;
	public static final float DEFAULT_GLABEL_SIZE = 12f;
	public static final float DEFAULT_GLABEL_PADDING = 16f;	

	public static final float DEFAULT_GSCALE_SIZE = 17f;
	public static final float DEFAULT_GSCALE_PADDING = 4f;	

	/*
	 * Variables
	 */

	// Grid parameters: Structure drawing
	private int gridChartType = ARC_RADAR_GRIDTYPE;
	private boolean latAxis = true;
	private boolean lonAxis = true;
	private boolean lonAxisScale = true;
	private boolean labels = true;

	private int lonNum = DEFAULT_LONGITUDE_NUM;
	private int latNum = DEFAULT_LATITUDE_NUM;

	private float maxValue = 1;		// Max value represented in the chart.
	private String labelsArray[];	// Labels array

	private int lonLength = DEFAULT_LONGITUDE_LENGTH;	
	private Point gridOrigin = DEFAULT_ORIGIN_POSITION;

	// Grid parameters: Grid styling
	private int backgroundColor = DEFAULT_GBACKGROUND_COLOR;
	private int gridBorderColor = -1;
	private int gridLatitudeColor = DEFAULT_GLATITUDE_COLOR;
	private int gridLongitudeColor = DEFAULT_GLONGITUDE_COLOR;
	private float gridStrokeWidth = DEFAULT_GSTROKE_WIDTH;
	private float gridBorderStrokeWidth = -1;

	private int gridLabelColor = DEFAULT_GLABEL_COLOR;
	private float gridLabelSize = DEFAULT_GLABEL_SIZE;
	private float gridLabelPadding = DEFAULT_GLABEL_PADDING;
	private int gridScaleColor = -1;
	private float gridScaleSize = DEFAULT_GSCALE_SIZE;
	private float gridScaleLabelPadding = DEFAULT_GSCALE_PADDING;

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
	public RadarGrid(Context cnt, int lonNum, int lonLength, int latNum, float maxValue, String[] labelsArray){
		super(cnt);
		this.maxValue = maxValue;
		this.lonNum = lonNum;
		this.latNum = latNum;
		this.maxValue = maxValue;
		this.labelsArray = labelsArray;
		this.lonLength = lonLength;
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

				
		// Draw longitude grid lines
		if(lonAxis){
			pList = getGridPoints(1);
			for (int i = 0; i < lonNum; i++) {
				PointF pt = pList.get(i);
				canvas.drawLine(gridOrigin.x, gridOrigin.y, pt.x, pt.y, getPaintGridLongitude());
			}
		}

		// Draw latitude grid lines
		if(latAxis){		
			switch (gridChartType) {
			case ARC_RADAR_GRIDTYPE:
				// Draw fill-background & Grid border
				canvas.drawCircle(gridOrigin.x, gridOrigin.y, lonLength, getPaintGridFill());
				canvas.drawCircle(gridOrigin.x, gridOrigin.y, lonLength, getPaintGridBorder());

				// Draw inner latitude lines
				for (int j = 1; j < latNum; j++) {
					canvas.drawCircle(gridOrigin.x, gridOrigin.y,
							lonLength * (j * 1f / latNum), getPaintGridLatitude());
				}
				break;

			case ARC_SPIDER_WEB_GRIDTYPE:
				if(pList == null) pList = getGridPoints(1);

				mPath.setFillType(Path.FillType.WINDING);
				// Draw fill-background & Grid border
				for (int i = 0; i < lonNum; i++) {
					PointF pt = pList.get(i);
					if (i == 0) {
						mPath.moveTo(pt.x, pt.y);
					} else {
						mPath.lineTo(pt.x, pt.y);
					}
				}
				mPath.close();
				if(backgroundColor != Color.TRANSPARENT)
					canvas.drawPath(mPath, getPaintGridFill());
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
		if(lonAxisScale){
			for (int k = 1; k < latNum; k++) {
				String val = String.valueOf((float) k * maxValue/latNum);

				float offsetX = (float) (gridOrigin.x + gridScaleLabelPadding);
				float offsetY = (float) (gridOrigin.y - k * lonLength/latNum - gridScaleLabelPadding);

				canvas.drawText(val, offsetX, offsetY, getPaintGScaleFont());
			}
		}

		// Draw grid labels
		if(labelsArray != null && labels){
			if(labelsArray.length != lonNum) 
				throw new RuntimeException("Labels array length not matches longitude lines number.");
			else
				for (int j = 0; j < lonNum; j++) {
					String label = labelsArray[j];
					if(label.equals(null)) label = "";

					getPaintGLabelFont().getTextBounds(label, 0, label.length(), mRect);

					float offsetX = (float) (gridOrigin.x - mRect.width()/2
							- (lonLength + gridLabelPadding) * Math.sin(j * 2 * Math.PI / lonNum) );
					float offsetY = (float) (gridOrigin.y + mRect.height()/2
							- (lonLength + gridLabelPadding) * Math.cos(j * 2 * Math.PI / lonNum));

					canvas.drawText(label, offsetX, offsetY, getPaintGLabelFont());
				}
		} else if(labelsArray == null) Log.w(TAG, "ATT! >> labelsArray = null");	
	}

	/*
	 * Drawing tools getters
	 */

	private Paint getPaintGridFill() {
		Paint mPaintGridFill = new Paint();
		mPaintGridFill.setColor(backgroundColor);
		mPaintGridFill.setAntiAlias(true);
		return mPaintGridFill;
	}
	private Paint getPaintGridBorder() {
		Paint mPaintGridBorder = new Paint();
		mPaintGridBorder.setColor(
				(gridBorderColor==-1)? gridLatitudeColor:gridBorderColor);
		mPaintGridBorder.setStyle(Style.STROKE);
		mPaintGridBorder.setStrokeWidth(
				(gridBorderStrokeWidth==-1)? gridStrokeWidth:gridBorderStrokeWidth);
		mPaintGridBorder.setAntiAlias(true);
		return mPaintGridBorder;
	}
	private Paint getPaintGridLatitude() {
		Paint mPaintGridLatitude = new Paint();
		mPaintGridLatitude.setColor(gridLatitudeColor);
		mPaintGridLatitude.setStyle(Style.STROKE);
		mPaintGridLatitude.setStrokeWidth(gridStrokeWidth);
		mPaintGridLatitude.setAntiAlias(true);
		return mPaintGridLatitude;
	}
	private Paint getPaintGridLongitude() {
		Paint mPaintGridLongitude = new Paint();
		mPaintGridLongitude.setColor(gridLongitudeColor);
		mPaintGridLongitude.setStrokeWidth(gridStrokeWidth);
		return mPaintGridLongitude;
	}
	private Paint getPaintGLabelFont() {
		Paint mPaintGLabelFont = new Paint();
		mPaintGLabelFont.setColor(gridLabelColor);
		mPaintGLabelFont.setTextSize(gridLabelSize);
		return mPaintGLabelFont;
	}
	private Paint getPaintGScaleFont() {
		Paint mPaintGScaleFont = new Paint();
		mPaintGScaleFont.setColor(
				(gridScaleColor==-1)? gridLabelColor:gridScaleColor);
		mPaintGScaleFont.setTextSize(gridScaleSize);
		return mPaintGScaleFont;
	}

	/*
	 * Setters
	 * All of them call invalidate()
	 */

	// Grid form setters
	public void setGridChartType(int gridChartType) {
		this.gridChartType = gridChartType;
		invalidate();
	}
	public void showLatAxis(boolean latAxis) {
		this.latAxis = latAxis;
		invalidate();
	}
	public void showLonAxis(boolean lonAxis) {
		this.lonAxis = lonAxis;
		invalidate();
	}
	public void showLonAxisScale(boolean lonAxisScale) {
		this.lonAxisScale = lonAxisScale;
		invalidate();
	}
	public void showLabels(boolean labels) {
		this.labels = labels;
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
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		invalidate();
	}
	public void setGridBorderColor(int gridBorderColor) {
		this.gridBorderColor = gridBorderColor;
		invalidate();
	}
	public void setGridLatitudeColor(int gridLatitudeColor) {
		this.gridLatitudeColor = gridLatitudeColor;
		invalidate();
	}
	public void setGridLongitudeColor(int gridLongitudeColor) {
		this.gridLongitudeColor = gridLongitudeColor;
		invalidate();
	}
	public void setGridStrokeWidth(float gridStrokeWidth) {
		this.gridStrokeWidth = gridStrokeWidth;
		invalidate();
	}
	public void setGridBorderStrokeWidth(float gridBorderStrokeWidth) {
		this.gridBorderStrokeWidth = gridBorderStrokeWidth;
		invalidate();
	}
	public void setGridLabelColor(int gridLabelColor) {
		this.gridLabelColor = gridLabelColor;
		invalidate();
	}
	public void setGridLabelSize(float gridLabelSize) {
		this.gridLabelSize = gridLabelSize;
		invalidate();
	}
	public void setGridLabelPadding(float gridLabelPadding) {
		this.gridLabelPadding = gridLabelPadding;
		invalidate();
	}
	public void setGridScaleColor(int gridScaleColor) {
		this.gridScaleColor = gridScaleColor;
		invalidate();
	}
	public void setGridScaleSize(float gridScaleSize) {
		this.gridScaleSize = gridScaleSize;
		invalidate();
	}
	public void setGridScaleLabelPadding(float gridScaleLabelPadding) {
		this.gridScaleLabelPadding = gridScaleLabelPadding;
		invalidate();
	}
}
