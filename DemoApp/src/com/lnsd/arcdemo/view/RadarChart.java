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
import android.view.View;
import android.view.ViewGroup;

import com.lnsd.arcdemo.entity.ARCDataLayer;
@SuppressWarnings("unused") // TODO Delete this!
public class RadarChart extends Chart {
	
	protected static final String TAG = "RadarChart";
	
	/*
	 * Constants
	 */

	// Chart types
	public static final int ARC_GRIDTYPE_NO_GRID = 0;
	public static final int ARC_GRIDTYPE_SPIDER_WEB_GRID = 1;
	public static final int ARC_GRIDTYPE_RADAR_GRID = 2;
	
	public static final Point DEFAULT_ORIGIN_POSITION = new Point(0, 0);
	public static final int DEFAULT_LATITUDE_NUM = 5;

	//public static final String DEFAULT_TITLE = "Spider Web Chart";
	//public static final boolean DEFAULT_DISPLAY_LONGITUDE = true;
	//public static final int DEFAULT_LONGITUDE_NUM = 5;
	//public static final boolean DEFAULT_DISPLAY_LATITUDE = true;
	//public static final Point DEFAULT_POSITION = new Point(0, 0);

	/*
	 *	Variables 
	 */

	//Common variables
	private Context arcContext;
	private ArrayList<ARCDataLayer> data;	// Data layers.
	
	private Point origin = DEFAULT_ORIGIN_POSITION; // Grid origin.
	private int longitudeNum = 1;
	private int latitudeNum = DEFAULT_LATITUDE_NUM;
	
	//Grid variables
	private RadarGrid arcGrid;	// Chart's grid View.
	
	private float maxDataValue = 0;
	private int maxEntitiesInLayer = 1;
	private int minEntitiesInLayer = 1;
	
	//Data layer variables
	private ArrayList<RadarDataLayer> arcDataLayers;	// Data layers Views.
	
	/*
	 * Constructors
	 */
	public RadarChart(Context context) {
		super(context);
		this.arcContext = context;
	}
	public RadarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.arcContext = context;
	}
	public RadarChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.arcContext = context;
	}

	/*
	 * ViewGroup methods
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int wSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
		int hSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
		
		/*
		for (int i = 0; i<getChildCount(); i++) {
			View view = getChildAt(i);
			if(view.getVisibility() != GONE){
				view.measure(wSpec, hSpec);
			}
		}*/
		Log.w(TAG+".onMeasure()","wSpec = "+MeasureSpec.getSize(widthMeasureSpec)+
				" // hSpec = "+MeasureSpec.getSize(heightMeasureSpec));
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		Log.i(TAG+".onLayout()","onLayout() reached.");
		Log.i(TAG+".onLayout()","onLayout() = "
				+"left: " + left +" /right: " + right
				+" /top: " + top +" /bottom: " + bottom);
		
		for (int i = 0; i<getChildCount(); i++) {
			
			if(getChildAt(i).getVisibility() != VISIBLE) continue;
			Log.v(TAG+".onLayout()","ChildAt("+i+") = "+getChildAt(i).getClass().toString());
			getChildAt(i).layout(left, left + getChildAt(i).getMeasuredWidth(), top, 0);
		}
	}

	/*
	 * Data manipulation Methods
	 */
	
	public void setData(ArrayList<ARCDataLayer> data) {
		this.data = data;

		for (ARCDataLayer arcDataLayer : data) {
			if(Math.abs(arcDataLayer.getMaxValueInLayer()) > this.maxDataValue)
				this.maxDataValue = Math.abs(arcDataLayer.getMaxValueInLayer());
			if(Math.abs(arcDataLayer.getDataEntitiesLength()) > this.maxEntitiesInLayer)
				this.maxEntitiesInLayer = Math.abs(arcDataLayer.getDataEntitiesLength());
			if(Math.abs(arcDataLayer.getDataEntitiesLength()) < this.minEntitiesInLayer)
				this.minEntitiesInLayer = Math.abs(arcDataLayer.getDataEntitiesLength());
		}
		
		arcGrid = new RadarGrid(arcContext);
		arcGrid.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.addView(arcGrid);
	}
	
	/*
	 * Inner classes
	 */
	
	private class RadarGrid extends ChartGridView{
		
		protected static final String TAG = "RadarGrid";
		
	/*
	 * Constants
	 */	
		public static final int DEFAULT_LONGITUDE_LENGTH = 80;
		
		public static final int DEFAULT_GBACKGROUND_COLOR = Color.TRANSPARENT;
		public static final int DEFAULT_GLATITUDE_COLOR = Color.BLACK;
		public static final int DEFAULT_GLONGITUDE_COLOR = Color.BLACK;
		public static final float DEFAULT_GSTROKE_WIDTH = 2f;

		public static final int DEFAULT_GLABEL_COLOR = Color.BLACK;
		public static final float DEFAULT_GLABEL_SIZE = 32f;
		public static final float DEFAULT_GLABEL_PADDING = 16f;	

	/*
	 * Variables
	 */
		// Grid form variables
		private int gridChartType = ARC_GRIDTYPE_RADAR_GRID;
		private boolean axis = true;
		private boolean labels = true;
		
		// Drawing variables
		private int longitudeLength = DEFAULT_LONGITUDE_LENGTH;	
		
		private int backgroundColor = DEFAULT_GBACKGROUND_COLOR;
		private int gridBorderColor = -1;
		private int gridLatitudeColor = DEFAULT_GLATITUDE_COLOR;
		private int gridLongitudeColor = DEFAULT_GLONGITUDE_COLOR;
		private int gridLabelColor = DEFAULT_GLABEL_COLOR;
		private float gridLabelSize = DEFAULT_GLABEL_SIZE;
		private float gridStrokeWidth = DEFAULT_GSTROKE_WIDTH;
		private float gridBorderStrokeWidth = -1;
		private float gridLabelPadding = DEFAULT_GLABEL_PADDING;
		
		//Paint variables
		private Paint mPaintGridFill;
		private Paint mPaintGridBorder;
		private Paint mPaintGridLatitude;
		private Paint mPaintGridLongitude;
		private Paint mPaintGLabelFont;
		
	/*
	 * Constructors
	 */
		public RadarGrid(Context context) {
			super(context);
			initGrid();
		}

		/*
		 * init() methods
		 */
		public void initGrid(){
			longitudeNum = minEntitiesInLayer;
			
			this.setMinimumHeight(2*DEFAULT_LONGITUDE_LENGTH);
			this.setMinimumWidth(2*DEFAULT_LONGITUDE_LENGTH);
		}
		
		public void initGridPaint(){	
			// Init drawing tools			
			mPaintGridFill = new Paint();
			mPaintGridFill.setColor(backgroundColor);
			mPaintGridFill.setAntiAlias(true);

			mPaintGridBorder = new Paint();
			mPaintGridBorder.setColor(
					(gridBorderColor==-1)? gridLatitudeColor:gridBorderColor);
			mPaintGridBorder.setStyle(Style.STROKE);
			mPaintGridBorder.setStrokeWidth(
					(gridBorderStrokeWidth==-1)? gridStrokeWidth:gridBorderStrokeWidth);
			mPaintGridBorder.setAntiAlias(true);

			mPaintGridLatitude = new Paint();
			mPaintGridLatitude.setColor(gridLatitudeColor);
			mPaintGridLatitude.setStyle(Style.STROKE);
			mPaintGridLatitude.setStrokeWidth(gridStrokeWidth);
			mPaintGridLatitude.setAntiAlias(true);

			mPaintGridLongitude = new Paint();
			mPaintGridLongitude.setColor(gridLongitudeColor);
			mPaintGridLongitude.setStrokeWidth(gridStrokeWidth);

			mPaintGLabelFont = new Paint();
			mPaintGLabelFont.setColor(gridLabelColor);
			mPaintGLabelFont.setTextSize(gridLabelSize);
		}
		
		@Override
		protected int hGetMaximumHeight() {
			return 2*longitudeLength;
		}
		@Override
		protected int hGetMaximumWidth() {
			return 2*longitudeLength;
		}
		
		/**
		 * Calculate the points to draw the latitude lines.
		 * Gets web grid drawing path points.
		 */
		protected List<PointF> getWebAxisPoints(float pos) {
			List<PointF> points = new ArrayList<PointF>();
			
			for (int i = 0; i < minEntitiesInLayer; i++) {
				PointF pt = new PointF();
				float offsetX = (float) (origin.x - longitudeLength * pos
						* Math.sin(i * 2 * Math.PI / longitudeNum));
				float offsetY = (float) (origin.y - longitudeLength * pos
						* Math.cos(i * 2 * Math.PI / longitudeNum));
				pt.set(offsetX, offsetY);

				points.add(pt);
			}
			return points;
		}	
		/**
		 * Calculate the radius to draw the latitude lines.
		 * Gets radius length for circular grid.
		 */
		protected float getRadarRadiusPoints(float pos) {		
			return (float) longitudeLength * pos;
		}

		/*
		 * View methods
		 * (non-Javadoc)
		 * @see android.view.View#onDraw(android.graphics.Canvas)
		 */
		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			Log.i(TAG+".onDraw()", "Draw reached.");
			
			// Get canvas measurements
			int rectHeight = super.getHeight();
			int rectWidth = super.getWidth();			

			// Calculate longitude length
			longitudeLength = (int) ((Math.min(rectHeight, rectWidth) / 2f) * 0.8);

			// Calculate graph origin
			origin.set(
					(int) (rectWidth / 2f),
					(int) (rectHeight / 2f));

			Log.d(TAG+".init()", "rH = "+rectHeight+" // rW = "+rectWidth);
			Log.d(TAG+".init()", "mH = "+super.getMeasuredHeight()+" // mW = "+super.getMeasuredHeight());
			
			initGridPaint();
			
			switch (gridChartType) {
			case 0: //No grid
				drawNOGrid(canvas);
				break;
			case 1: //Spider Web chart
				drawSpiderWeb(canvas);
				break;
			default:
				drawRadarGrid(canvas);
				break;
			}
		}
		
		protected void drawNOGrid(Canvas canvas) {
			List<PointF> pointList = getWebAxisPoints(1);

			if(labels){
				//Draw labels
				if (data != null) {
					for (int i = 0; i < pointList.size(); i++) {

						// Draw labels. Label defined by the first list.
						String title = data.get(0).get(i).getLabel();

						Rect labelRect = new Rect();
						mPaintGLabelFont.getTextBounds(title, 0, title.length(), labelRect);

						float offsetX = (float) (origin.x - labelRect.width()/2
								- (longitudeLength + gridLabelPadding) * Math.sin(i * 2 * Math.PI / longitudeNum) );
						float offsetY = (float) (origin.y + labelRect.height()/2
								- (longitudeLength + gridLabelPadding) * Math.cos(i * 2 * Math.PI / longitudeNum));

						canvas.drawText(title, offsetX, offsetY, mPaintGLabelFont);
					}
				}
			}

			if(axis){
				Paint mPaintGridLongitude = new Paint();
				mPaintGridLongitude.setColor(gridLongitudeColor);
				mPaintGridLongitude.setStrokeWidth(gridStrokeWidth);

				// Draw longitude lines
				for (int i = 0; i < pointList.size(); i++) {
					PointF pt = pointList.get(i);
					canvas.drawLine(origin.x, origin.y, pt.x, pt.y, mPaintGridLongitude);
				}
			}
		}
		protected void drawSpiderWeb(Canvas canvas) {
			
			Path mPath = new Path();
			List<PointF> pointList = getWebAxisPoints(1);

			//Draw fill-background
			if (data != null) {
				for (int i = 0; i < pointList.size(); i++) {
					PointF pt = pointList.get(i);
					if (i == 0) {
						mPath.moveTo(pt.x, pt.y);
					} else {
						mPath.lineTo(pt.x, pt.y);
					}

					// Draw labels. Label defined by the first list.
					String title = data.get(0).get(i).getLabel();

					Rect labelRect = new Rect();
					mPaintGLabelFont.getTextBounds(title, 0, title.length(), labelRect);

					float offsetX = (float) (origin.x - labelRect.width()/2
							- (longitudeLength + gridLabelPadding) * Math.sin(i * 2 * Math.PI / longitudeNum) );
					float offsetY = (float) (origin.y + labelRect.height()/2
							- (longitudeLength + gridLabelPadding) * Math.cos(i * 2 * Math.PI / longitudeNum));

					canvas.drawText(title, offsetX, offsetY, mPaintGLabelFont);
				}
			}
			mPath.close();
			canvas.drawPath(mPath, mPaintGridFill);

			// draw longitude lines
			for (int i = 0; i < pointList.size(); i++) {
				PointF pt = pointList.get(i);
				canvas.drawLine(origin.x, origin.y, pt.x, pt.y, mPaintGridLongitude);
			}

			//Draw web border
			canvas.drawPath(mPath, mPaintGridBorder);

			// draw spider web
			for (int j = 1; j < latitudeNum; j++) {

				Path mPathInner = new Path();
				List<PointF> pointListInner = getWebAxisPoints(j * 1f / latitudeNum);

				for (int i = 0; i < pointListInner.size(); i++) {
					PointF pt = pointListInner.get(i);
					if (i == 0) {
						mPathInner.moveTo(pt.x, pt.y);
					} else {
						mPathInner.lineTo(pt.x, pt.y);
					}
				}
				mPathInner.close();
				canvas.drawPath(mPathInner, mPaintGridLatitude);
			}
		}
		protected void drawRadarGrid(Canvas canvas) {
			
			List<PointF> pointList = getWebAxisPoints(1);

			//Draw fill-background
			if (data != null) {
				for (int i = 0; i < pointList.size(); i++) {

					// Draw labels. Label defined by the first list.
					String title = data.get(0).get(i).getLabel();

					Rect labelRect = new Rect();
					mPaintGLabelFont.getTextBounds(title, 0, title.length(), labelRect);

					float offsetX = (float) (origin.x - labelRect.width()/2
							- (longitudeLength + gridLabelPadding) * Math.sin(i * 2 * Math.PI / longitudeNum) );
					float offsetY = (float) (origin.y + labelRect.height()/2
							- (longitudeLength + gridLabelPadding) * Math.cos(i * 2 * Math.PI / longitudeNum));

					canvas.drawText(title, offsetX, offsetY, mPaintGLabelFont);
				}
			}
			canvas.drawCircle(origin.x, origin.y, getRadarRadiusPoints(1), mPaintGridFill);


			// Draw longitude lines
			for (int i = 0; i < pointList.size(); i++) {
				PointF pt = pointList.get(i);
				canvas.drawLine(origin.x, origin.y, pt.x, pt.y, mPaintGridLongitude);
			}

			//Draw grid border
			canvas.drawCircle(origin.x, origin.y, getRadarRadiusPoints(1), mPaintGridBorder);

			// Draw latitude lines
			for (int j = 1; j < latitudeNum; j++) {
				canvas.drawCircle(origin.x, origin.y, getRadarRadiusPoints(j * 1f / latitudeNum), mPaintGridLatitude);
			}
		}
		
		/*
		 * Getters & Setters
		 */
		
		public boolean isAxisVisible() {
			return axis;
		}
		public void setAxisVisible(boolean axis) {
			this.axis = axis;
		}
		public boolean isLabelsVisible() {
			return labels;
		}
		public void setLabelsVisible(boolean labels) {
			this.labels = labels;
		}
		public int getBackgroundColor() {
			return backgroundColor;
		}
		public void setBackgroundColor(int backgroundColor) {
			this.backgroundColor = backgroundColor;
		}
		public int getGridBorderColor() {
			return gridBorderColor;
		}
		public void setGridBorderColor(int gridBorderColor) {
			this.gridBorderColor = (gridBorderColor==-1)? gridLatitudeColor:gridBorderColor;
		}
		public int getGridLatitudeColor() {
			return gridLatitudeColor;
		}
		public void setGridLatitudeColor(int gridLatitudeColor) {
			this.gridLatitudeColor = gridLatitudeColor;
		}
		public int getGridLongitudeColor() {
			return gridLongitudeColor;
		}
		public void setGridLongitudeColor(int gridLongitudeColor) {
			this.gridLongitudeColor = gridLongitudeColor;
		}
		public int getGridLabelColor() {
			return gridLabelColor;
		}
		public void setGridLabelColor(int gridLabelColor) {
			this.gridLabelColor = gridLabelColor;
		}
		public float getGridLabelSize() {
			return gridLabelSize;
		}
		public void setGridLabelSize(float gridLabelSize) {
			this.gridLabelSize = gridLabelSize;
		}
		public float getGridStrokeWidth() {
			return gridStrokeWidth;
		}
		public void setGridStrokeWidth(float gridStrokeWidth) {
			this.gridStrokeWidth = gridStrokeWidth;
		}
		public float getGridBorderStrokeWidth() {
			return gridBorderStrokeWidth;
		}
		public void setGridBorderStrokeWidth(float gridBorderStrokeWidth) {
			this.gridBorderStrokeWidth = (gridBorderStrokeWidth==-1)? gridStrokeWidth:gridBorderStrokeWidth;
		}
		public float getGridLabelPadding() {
			return gridLabelPadding;
		}
		public void setGridLabelPadding(float gridLabelPadding) {
			this.gridLabelPadding = gridLabelPadding;
		}
	}

	private class RadarDataLayer extends ChartGridView{
		public RadarDataLayer(Context context) {
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			//TODO Migrate data layer drawing methods & variables
		}

		@Override
		protected int hGetMaximumHeight() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		protected int hGetMaximumWidth() {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	/*
	 * Getters & Setters
	 */
	
	public RadarGrid getArcGrid() {
		return arcGrid;
	}
}
