package com.lnsd.arcdemo.entity;

public class TitleValueColorEntity extends TitleValueEntity {

	private int color;
	public TitleValueColorEntity(String title, float value, int color) {
		super(title, value);
		this.color = color;
	}
	public TitleValueColorEntity() {
		super();
	}

	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
}
