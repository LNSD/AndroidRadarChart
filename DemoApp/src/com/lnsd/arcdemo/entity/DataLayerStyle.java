package com.lnsd.arcdemo.entity;

import android.graphics.Color;

/*
 * Layer customization Class
 */
public class DataLayerStyle {
	public static final float DEFAULT_LBORDER_WIDTH = 2f;
	public static final int DEFAULT_LFILL_ALPHA = 70;
	
	private int layerBorderColor = Color.BLACK;
	private float layerBorderWidth = DEFAULT_LBORDER_WIDTH;
	private int layerFillColor = -1;
	private int layerFillAlpha = DEFAULT_LFILL_ALPHA;
	
	public int getLayerBorderColor() {
		return layerBorderColor;
	}
	public void setLayerBorderColor(int layerBorderColor) {
		this.layerBorderColor = layerBorderColor;
	}
	public float getLayerBorderWidth() {
		return layerBorderWidth;
	}
	public void setLayerBorderWidth(float layerBorderWidth) {
		this.layerBorderWidth = layerBorderWidth;
	}
	public int getLayerFillColor() {
		return (layerFillColor == -1)? layerBorderColor:layerFillColor;
	}
	public void setLayerFillColor(int layerFillColor) {
		this.layerFillColor = layerFillColor;
	}
	public int getLayerFillAlpha() {
		return layerFillAlpha;
	}
	public void setLayerFillAlpha(int layerFillAlpha) {
		this.layerFillAlpha = layerFillAlpha;
	}
}