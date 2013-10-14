package com.lnsd.arcdemo.entity;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class ARCDataLayer extends TreeMap<String, Float> {
	
	private DataLayerStyle params = new DataLayerStyle();
	private String layerTitle = "";
	
	/**
	 * Empty constructor.
	 */
	public ARCDataLayer(){}
	
	/**
	 * Class constructor.
	 * @param params Custom styling.
	 */
	public ARCDataLayer(DataLayerStyle params){
		this.params = params;
	}
	
	/**
	 * Class constructor.
	 * @param layerTitle Layer data series title.
	 * @param params Custom styling.
	 */
	public ARCDataLayer(String layerTitle, DataLayerStyle params){
		this.params = params;
		this.layerTitle = layerTitle;
	}

	/**
	 * Compares if both dataLayers are compatible.
	 * If both DataLayer objects have the same keys stored, they are compatibles. 
	 * @param dataLayer DataLayer to compare .
	 * @return If compatible returns true, false otherwise.
	 */
	public boolean check(ARCDataLayer dataLayer){
		if(this.size() != dataLayer.size()) return false;
		for (Map.Entry<String,Float> entry: dataLayer.entrySet()) {
			if(!containsKey(entry.getKey())) return false;
		}
		
		return true;
	}
	
	/**
	 * Gets the maximum value stored in the DataLayer.
	 * @return Maximum value.
	 */
	public float getMaxValue(){
		float max = 0;
		for (Map.Entry<String,Float> entry: entrySet()) {
			if(entry.getValue()>max) max = entry.getValue();
		}
		return max;
	}
	
	/**
	 * Gets all the layer labels.
	 * @return Array of labels.
	 */
	public String[] getLabels() {
		ArrayList<String> labels = new ArrayList<String>();
		for (Map.Entry<String,Float> entry: entrySet()) {
			labels.add(entry.getKey());
		}
		return (String[]) labels.toArray(new String[labels.size()]);
	}

	/*
	 * Getters & Setters
	 */
	
	public DataLayerStyle getLayerStyle() {
		return params;
	}
	public void setLayerStyle(DataLayerStyle params) {
		this.params = params;
	}
	public String getLayerTitle() {
		return layerTitle;
	}
	public void setLayerTitle(String layerTitle) {
		this.layerTitle = layerTitle;
	}

}
