package com.lnsd.arcdemo.entity;

public class ARCDataEntity {
	private String label;
	private float value;
	private int pointColor = -1;
	private float pointSize = -1;

	public ARCDataEntity(String label, float value){
		this.label = label;
		this.value = value;
	}
	public ARCDataEntity(String label, float value, int color){
		this.label = label;
		this.value = value;
		this.pointColor = color;
	}
	public ARCDataEntity(String label, float value, int color, float size){
		this.label = label;
		this.value = value;
		this.pointColor = color;
		this.pointSize = size;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}

	public void setPointColor(int color) {
		this.pointColor = color;
	}
	public int getPointColor() {
		return pointColor;
	}
	public float getPointSize() {
		return pointSize;
	}
	public void setPointSize(float pointSize) {
		this.pointSize = pointSize;
	}
}
