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

import com.lnsd.arcdemo.entity.ARCDataLayer;

public class RadarChart2 extends Chart {

	/*
	 * Constants
	 */

	// Chart types
	public static final int NO_GRID = 0;
	public static final int SPIDER_WEB_CHART = 1;
	public static final int RADAR_CHART = 2;

	// Useful constants
	public static final String DEFAULT_TITLE = "Spider Web Chart";
	public static final boolean DEFAULT_DISPLAY_LONGITUDE = true;
	public static final int DEFAULT_LONGITUDE_NUM = 5;
	public static final int DEFAULT_LONGITUDE_LENGTH = 80;
	public static final boolean DEFAULT_DISPLAY_LATITUDE = true;
	public static final int DEFAULT_LATITUDE_NUM = 5;
	public static final Point DEFAULT_POSITION = new Point(0, 0);

	public static final int DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT;
	public static final int DEFAULT_LATITUDE_COLOR = Color.BLACK;
	public static final int DEFAULT_LONGITUDE_COLOR = Color.BLACK;
	public static final float DEFAULT_STROKE_WIDTH = 2f;
	public static final int DEFAULT_GLABEL_COLOR = Color.BLACK;
	public static final float DEFAULT_GLABEL_SIZE = 32f;
	public static final float DEFAULT_GLABEL_PADDING = 16f;


	/*
	 *  Variables
	 */

	private int chartType = RADAR_CHART;
	private ArrayList<ARCDataLayer> data;
	private String title = DEFAULT_TITLE; //TODO Add chart title
	private Point position = DEFAULT_POSITION;
	private int longitudeNum = DEFAULT_LONGITUDE_NUM;
	private int longitudeColor = DEFAULT_LONGITUDE_COLOR;
	private int longitudeLength = DEFAULT_LONGITUDE_LENGTH;
	private boolean displayLatitude = DEFAULT_DISPLAY_LATITUDE;
	private int latitudeNum = DEFAULT_LATITUDE_NUM;
	private float maxValue = 0;
	private int maxEntitiesInLayer = 0;
	private int minEntitiesInLayer = 0;

	// Colors & stroke width
	private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
	private int gridBorderColor = -1;
	private int gridLatitudeColor = DEFAULT_LATITUDE_COLOR;
	private int gridLongitudeColor = DEFAULT_LONGITUDE_COLOR;
	private int gridLabelColor = DEFAULT_GLABEL_COLOR;
	private float gridLabelSize = DEFAULT_GLABEL_SIZE;
	private float gridStrokeWidth = DEFAULT_STROKE_WIDTH;
	private float gridBorderStrokeWidth = -1;
	private float gridLabelPadding = DEFAULT_GLABEL_PADDING;
	private boolean axis = true;
	private boolean labels = true;

	/**
	 * init() variables
	 */
	
	private Paint mPaintGridFill;
	private Paint mPaintGridBorder;
	private Paint mPaintGridLatitude;
	private Paint mPaintGridLongitude;
	private Paint mPaintLabelFont;

	/**
	 * Class constructor taking only a context. Use this constructor to create
	 * {@link RadarChart2} objects from your own code.
	 *
	 * @param context
	 */

	public RadarChart2(Context context) {
		super(context);
		init();
	}

	/**
	 * Class constructor taking a context and an attribute set. This constructor
	 * is used by the layout engine to construct a {@link RadarChart2} from a set of
	 * XML attributes.
	 *
	 * @param context
	 * @param attrs   An attribute set which can contain attributes from
	 *                {@link com.RadarChart2.android.customviews.R.styleable.RadarChart} as well as attributes inherited
	 *                from {@link android.view.View}.
	 */

	public RadarChart2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public RadarChart2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/*
	 * ViewGroup Methods
	 */
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Paint initialization 
	 */
	
	public void init(){
		
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

		mPaintLabelFont = new Paint();
		mPaintLabelFont.setColor(gridLabelColor);
		mPaintLabelFont.setTextSize(gridLabelSize);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// get safe rect
		int rect = super.getHeight();

		// calculate longitude length
		longitudeLength = (int) ((rect / 2f) * 0.8);

		// calculate position TODO Check this out
		position = new Point((int) (super.getWidth() / 2f),
				(int) (super.getHeight() / 2f + 0.2 * longitudeLength));

		// draw this chart-grid
		switch (chartType) {
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

		// draw data on chart
		drawData(canvas);
	}

	/**
	 * <p>
	 * calculate the points to draw the latitude lines
	 * </p>
	 * 
	 * @param pos
	 *            <p>
	 *            Latitude degree. 1 = Grid border
	 *            </p>
	 * 
	 * @return List<PointF>
	 *         <p>
	 *         result points
	 *         </p>
	 *         
	 */
	protected List<PointF> getWebAxisPoints(float pos) {
		List<PointF> points = new ArrayList<PointF>();
		for (int i = 0; i < longitudeNum; i++) {
			PointF pt = new PointF();
			float offsetX = (float) (position.x - longitudeLength * pos
					* Math.sin(i * 2 * Math.PI / longitudeNum));
			float offsetY = (float) (position.y - longitudeLength * pos
					* Math.cos(i * 2 * Math.PI / longitudeNum));
			pt.set(offsetX, offsetY);

			points.add(pt);
		}
		return points;
	}
	protected float getRadarRadiusPoints(float pos) {		
		return (float) longitudeLength * pos;
	}

	/**
	 * <p>
	 * calculate the points to draw the data
	 * </p>

	 * @param data
	 *            <p>
	 *            data for calculation
	 *            </p>
	 * 
	 * @return List<PointF>
	 *         <p>
	 *         result points
	 *         </p>
	 *         
	 */
	protected List<PointF> getDataPoints(ARCDataLayer data) {
		List<PointF> points = new ArrayList<PointF>();
		for (int i = 0; i < longitudeNum; i++) {
			PointF pt = new PointF();
			float offsetX = (float) (position.x - data.get(i).getValue() / maxValue
					* longitudeLength
					* Math.sin(i * 2 * Math.PI / longitudeNum));
			float offsetY = (float) (position.y - data.get(i).getValue() / maxValue
					* longitudeLength
					* Math.cos(i * 2 * Math.PI / longitudeNum));
			pt.set(offsetX, offsetY);

			points.add(pt);
		}
		return points;
	}

	/**
	 * <p>
	 * draw spider web
	 * </p>
	 * 
	 * @param canvas
	 *            Canvas
	 */
	protected void drawNOGrid(Canvas canvas) {
		List<PointF> pointList = getWebAxisPoints(1);

		if(labels){
			//Draw labels
			if (data != null) {
				for (int i = 0; i < pointList.size(); i++) {

					// Draw labels. Label defined by the first list.
					String title = data.get(0).get(i).getLabel();

					Rect labelRect = new Rect();
					mPaintLabelFont.getTextBounds(title, 0, title.length(), labelRect);

					float offsetX = (float) (position.x - labelRect.width()/2
							- (longitudeLength + gridLabelPadding) * Math.sin(i * 2 * Math.PI / longitudeNum) );
					float offsetY = (float) (position.y + labelRect.height()/2
							- (longitudeLength + gridLabelPadding) * Math.cos(i * 2 * Math.PI / longitudeNum));

					canvas.drawText(title, offsetX, offsetY, mPaintLabelFont);
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
				canvas.drawLine(position.x, position.y, pt.x, pt.y, mPaintGridLongitude);
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
				mPaintLabelFont.getTextBounds(title, 0, title.length(), labelRect);

				float offsetX = (float) (position.x - labelRect.width()/2
						- (longitudeLength + gridLabelPadding) * Math.sin(i * 2 * Math.PI / longitudeNum) );
				float offsetY = (float) (position.y + labelRect.height()/2
						- (longitudeLength + gridLabelPadding) * Math.cos(i * 2 * Math.PI / longitudeNum));

				canvas.drawText(title, offsetX, offsetY, mPaintLabelFont);
			}
		}
		mPath.close();
		canvas.drawPath(mPath, mPaintGridFill);

		// draw longitude lines
		for (int i = 0; i < pointList.size(); i++) {
			PointF pt = pointList.get(i);
			canvas.drawLine(position.x, position.y, pt.x, pt.y, mPaintGridLongitude);
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
				mPaintLabelFont.getTextBounds(title, 0, title.length(), labelRect);

				float offsetX = (float) (position.x - labelRect.width()/2
						- (longitudeLength + gridLabelPadding) * Math.sin(i * 2 * Math.PI / longitudeNum) );
				float offsetY = (float) (position.y + labelRect.height()/2
						- (longitudeLength + gridLabelPadding) * Math.cos(i * 2 * Math.PI / longitudeNum));

				canvas.drawText(title, offsetX, offsetY, mPaintLabelFont);
			}
		}
		canvas.drawCircle(position.x, position.y, getRadarRadiusPoints(1), mPaintGridFill);


		// Draw longitude lines
		for (int i = 0; i < pointList.size(); i++) {
			PointF pt = pointList.get(i);
			canvas.drawLine(position.x, position.y, pt.x, pt.y, mPaintGridLongitude);
		}

		//Draw grid border
		canvas.drawCircle(position.x, position.y, getRadarRadiusPoints(1), mPaintGridBorder);

		// Draw latitude lines
		for (int j = 1; j < latitudeNum; j++) {
			canvas.drawCircle(position.x, position.y, getRadarRadiusPoints(j * 1f / latitudeNum), mPaintGridLatitude);
		}
	}
	
	/**
	 * Draws a data layer
	 * @param canvas
	 */
	protected void drawData(Canvas canvas) {
		if (null != data) {
			for (int j = 0; j < data.size(); j++) {
				ARCDataLayer layer = data.get(j);

				Paint mPaintFill = new Paint();
				mPaintFill.setColor(layer.getLayerFillColor());
				mPaintFill.setStyle(Style.FILL);
				mPaintFill.setAntiAlias(true);
				mPaintFill.setAlpha(layer.getLayerFillAlpha());

				Paint mPaintBorder = new Paint();
				mPaintBorder.setColor(layer.getLayerBorderColor());
				mPaintBorder.setStyle(Style.STROKE);
				mPaintBorder.setStrokeWidth(layer.getLayerBorderWidth());
				mPaintBorder.setAntiAlias(true);

				//TODO Check. Paint to draw fonts.
				Paint mPaintFont = new Paint();
				mPaintFont.setColor(Color.WHITE);

				// Paint to draw points
				Paint mPaintPoint = new Paint();

				Path mPath = new Path();

				// get points to draw
				List<PointF> pointList = getDataPoints(layer);
				// initialize path
				for (int i = 0; i < pointList.size(); i++) {
					PointF pt = pointList.get(i);
					if (i == 0) {
						mPath.moveTo(pt.x, pt.y);
					} else {
						mPath.lineTo(pt.x, pt.y);
					}
				}
				mPath.close();

				canvas.drawPath(mPath, mPaintFill);
				canvas.drawPath(mPath, mPaintBorder);

				//TODO Replace points with bitmaps
				for(int i = 0; i < pointList.size(); i++){
					mPaintPoint.setColor(layer.get(i).getPointColor());
					canvas.drawCircle(pointList.get(i).x, pointList.get(i).y, 
							layer.get(i).getPointSize(), mPaintPoint);
				}
			}
		}
	}

	
	public ArrayList<ARCDataLayer> getData() {
		return data;
	}

	public void setData(ArrayList<ARCDataLayer> data) {
		this.data = data;

		for (ARCDataLayer arcDataLayer : data) {
			if(Math.abs(arcDataLayer.getMaxValueInLayer()) > this.maxValue)
				this.maxValue = Math.abs(arcDataLayer.getMaxValueInLayer());
			if(Math.abs(arcDataLayer.getDataEntitiesLength()) > this.maxEntitiesInLayer)
				this.maxEntitiesInLayer = Math.abs(arcDataLayer.getDataEntitiesLength());
			if(Math.abs(arcDataLayer.getDataEntitiesLength()) < this.minEntitiesInLayer)
				this.minEntitiesInLayer = Math.abs(arcDataLayer.getDataEntitiesLength());
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Point getPosition() {
		return position;
	}
	public void setPosition(Point position) {
		this.position = position;
	}
	public int getLongitudeNum() {
		return longitudeNum;
	}
	public void setLongitudeNum(int longitudeNum) {
		this.longitudeNum = longitudeNum;
	}
	public int getLongitudeColor() {
		return longitudeColor;
	}
	public void setLongitudeColor(int longitudeColor) {
		this.longitudeColor = longitudeColor;
	}
	public int getLongitudeLength() {
		return longitudeLength;
	}
	public void setLongitudeLength(int longitudeLength) {
		this.longitudeLength = longitudeLength;
	}
	public boolean isDisplayLatitude() {
		return displayLatitude;
	}
	public void setDisplayLatitude(boolean displayLatitude) {
		this.displayLatitude = displayLatitude;
	}
	public int getLatitudeNum() {
		return latitudeNum;
	}
	public void setLatitudeNum(int latitudeNum) {
		this.latitudeNum = latitudeNum;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public void setGridLatitudeColor(int gridLatitudeColor) {
		this.gridLatitudeColor = gridLatitudeColor;
	}
	public void setGridLongitudeColor(int gridLongitudeColor) {
		this.gridLongitudeColor = gridLongitudeColor;
	}
	public void setGridStrokeWidth(float gridStrokeWidth) {
		this.gridStrokeWidth = gridStrokeWidth;
	}
	public void setGridLabelColor(int color) {
		this.gridLabelColor = color;
	}
	public void setGridLabelSize(float size){
		this.gridLabelSize = size;
	}
	public void setGridBorderColor(int color){
		this.gridBorderColor = color;
	}
	public void setChartGridType(int type){
		this.chartType = type;
	}
	public void setGridBorderStrokeWidth(float width){
		this.gridBorderStrokeWidth = width;
	}
	public void setGridLabelPadding(float gridLabelPadding) {
		this.gridLabelPadding = gridLabelPadding;
	}
	public boolean getAxisStatus() {
		return axis;
	}
	public void showAxis(boolean axis) {
		this.axis = axis;
	}
	public boolean getLabelStatus() {
		return labels;
	}
	public void showLabes(boolean labels) {
		this.labels = labels;
	}
}
