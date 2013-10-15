package com.lnsd.arcdemo.entity;

import android.graphics.Color;

public class GridLayerStyle {

	/*
	 * Constants
	 */	

	// Chart types
	public static final int ARC_SPIDER_WEB_GRIDTYPE = 0;
	public static final int ARC_RADAR_GRIDTYPE = 1;

	// Default values
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
	 * Getters & setters
	 */
	
	// Getters
	public int getGridChartType() {
		return gridChartType;
	}
	public boolean isLatAxis() {
		return latAxis;
	}
	public boolean isLonAxis() {
		return lonAxis;
	}
	public boolean isLonAxisScale() {
		return lonAxisScale;
	}
	public boolean isLabels() {
		return labels;
	}
	public int getBackgroundColor() {
		return backgroundColor;
	}
	public int getGridBorderColor() {
		return (gridBorderColor==-1)? gridLatitudeColor:gridBorderColor;
	}
	public int getGridLatitudeColor() {
		return gridLatitudeColor;
	}
	public int getGridLongitudeColor() {
		return gridLongitudeColor;
	}
	public float getGridStrokeWidth() {
		return gridStrokeWidth;
	}
	public float getGridBorderStrokeWidth() {
		return (gridBorderStrokeWidth==-1)? gridStrokeWidth:gridBorderStrokeWidth;
	}
	public int getGridLabelColor() {
		return gridLabelColor;
	}
	public float getGridLabelSize() {
		return gridLabelSize;
	}
	public float getGridLabelPadding() {
		return gridLabelPadding;
	}
	public int getGridScaleColor() {
		return (gridScaleColor==-1)? gridLabelColor:gridScaleColor;
	}
	public float getGridScaleSize() {
		return gridScaleSize;
	}
	public float getGridScaleLabelPadding() {
		return gridScaleLabelPadding;
	}
	
	// Setters
	public void setGridChartType(int gridChartType) {
		this.gridChartType = gridChartType;
	}
	public void showLatAxis(boolean latAxis) {
		this.latAxis = latAxis;
	}
	public void showLonAxis(boolean lonAxis) {
		this.lonAxis = lonAxis;
	}
	public void showLonAxisScale(boolean lonAxisScale) {
		this.lonAxisScale = lonAxisScale;
	}
	public void showLabels(boolean labels) {
		this.labels = labels;
	}
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public void setGridBorderColor(int gridBorderColor) {
		this.gridBorderColor = gridBorderColor;
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
	public void setGridBorderStrokeWidth(float gridBorderStrokeWidth) {
		this.gridBorderStrokeWidth = gridBorderStrokeWidth;
	}
	public void setGridLabelColor(int gridLabelColor) {
		this.gridLabelColor = gridLabelColor;
	}
	public void setGridLabelSize(float gridLabelSize) {
		this.gridLabelSize = gridLabelSize;
	}
	public void setGridLabelPadding(float gridLabelPadding) {
		this.gridLabelPadding = gridLabelPadding;
	}
	public void setGridScaleColor(int gridScaleColor) {
		this.gridScaleColor = gridScaleColor;
	}
	public void setGridScaleSize(float gridScaleSize) {
		this.gridScaleSize = gridScaleSize;
	}
	public void setGridScaleLabelPadding(float gridScaleLabelPadding) {
		this.gridScaleLabelPadding = gridScaleLabelPadding;
	}
}
