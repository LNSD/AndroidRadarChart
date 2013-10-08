package com.lnsd.arcdemo.entity;

import java.util.ArrayList;

import android.graphics.Color;

public class ARCDataLayer {
	public static final float DEFAULT_LBORDER_WIDTH = 2f;
	public static final int DEFAULT_LFILL_ALPHA = 70;
	public static final float DEFAULT_POINT_SIZE = 3f;
	
	private ArrayList<ARCDataEntity> dataList = new ArrayList<ARCDataEntity>();
	private int layerBorderColor = Color.BLACK;
	private float layerBorderWidth = DEFAULT_LBORDER_WIDTH;
	private int layerFillColor = Color.BLACK;
	private int layerFillAlpha = DEFAULT_LFILL_ALPHA;

	public ARCDataLayer(int color, float borderWidth){
		this.layerBorderColor = color;
		this.layerBorderWidth = borderWidth;
		this.layerFillColor = Color.TRANSPARENT;
	}
	public ARCDataLayer(int fillColor, int alpha){
		this.layerBorderColor = fillColor;
		this.layerFillColor = fillColor;
		this.layerFillAlpha = alpha;
	}
	public ARCDataLayer(int strokeColor, float borderWidth, int fillColor, int fillAlpha){
		this.layerBorderColor = strokeColor;
		this.layerBorderWidth = borderWidth;
		this.layerFillColor = fillColor;
		this.layerFillAlpha = fillAlpha;
	}

	public void add(ARCDataEntity data){
		if(data.getPointColor() == -1)
			data.setPointColor(layerBorderColor);
		if(data.getPointSize() == -1)
			data.setPointSize(DEFAULT_POINT_SIZE);
		dataList.add(data);
	}
	public ArrayList<ARCDataEntity> getDataList(){
		return dataList;
	}
 	public ARCDataEntity get(int index){
		return dataList.get(index);
	}
	public int getDataEntitiesLength(){
		return dataList.size();
	}
	public float getMaxValueInLayer(){
		float MAX = 0;
		for (ARCDataEntity entity : dataList) {
			if(Math.abs(entity.getValue())>MAX)
				MAX = entity.getValue();
		}
		return MAX;
	}
	
	public void setLayerBorderColor(int color){
		this.layerBorderColor = color;
	}
	public int getLayerBorderColor(){
		return layerBorderColor;
	}
	public void setLayerFillColor(int color){
		this.layerFillColor = color;
	}
	public int getLayerFillColor(){
		return layerFillColor;
	}
	public void setLayerBorderWidth(float layerBorderWidth) {
		this.layerBorderWidth = layerBorderWidth;
	}
	public float getLayerBorderWidth() {
		return layerBorderWidth;
	}	
	public void setLayerFillAlpha(int layerFillAlpha) {
		this.layerFillAlpha = layerFillAlpha;
	}
	public int getLayerFillAlpha() {
		return layerFillAlpha;
	}
	
}

//TODO Do something with graph data list length