package com.lnsd.arcdemo.entity;

import java.util.Map;
import java.util.TreeMap;

import android.graphics.Color;

@SuppressWarnings("serial")
public class ARCDataLayer extends TreeMap<String, Float> {
	
	private DataLayerParams params = new DataLayerParams();
	
	/**
	 * Empty constructor.
	 */
	public ARCDataLayer(){}
	
	/**
	 * Class constructor.
	 * @param params Custom params.
	 */
	public ARCDataLayer(DataLayerParams params){
		this.params = params;
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

	/*
	 * Getters & Setters
	 */
	
	public DataLayerParams getLayerParameters() {
		return params;
	}
	public void setLayerParameters(DataLayerParams params) {
		this.params = params;
	}

}
