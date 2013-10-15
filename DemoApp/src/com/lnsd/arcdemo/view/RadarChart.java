package com.lnsd.arcdemo.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.lnsd.arcdemo.baseview.Chart;
import com.lnsd.arcdemo.entity.ARCDataLayer;
import com.lnsd.arcdemo.entity.GridLayerStyle;

public class RadarChart extends Chart {

	/*
	 * Constants
	 */

	public static final String TAG = "RadarChart";

	public static final int DEFAULT_MAX_SIMULTANEOUS_LAYERS = 3;
	public static final int DEFAULT_MIN_ELEMENT_DATALAYER = 3;

	public static final String EXCEPTION_WRONG_SIZE = "Not enough elements in layer.";
	public static final String EXCEPTION_LAYER_COMPATIBILITY = "Layer not compatible.";

	/*
	 * Variables
	 */

	private Context context;
	private ArrayList<ARCDataLayer> dataLayers;	// Data entity layers
	private GridLayerStyle gridStyle = new GridLayerStyle();

	private TextView titleView;						// Chart title
	private TextView subtitleView;					// Chart subtitle
	private RadarGrid arcGridView;					// Grid view
	private RadarDataLayer[] arcDataLayerViews;		// Showing data layers

	private boolean title = true;
	private boolean subtitle = true;

	private int layersAllowed = DEFAULT_MAX_SIMULTANEOUS_LAYERS;
	private int lonNum = 0;
	private int latNum = RadarGrid.DEFAULT_LATITUDE_NUM;

	private String[] labels;
	private float maxValue = 0;

	/*
	 * Constructors
	 */

	public RadarChart(Context context) {
		super(context);
		this.context = context;
	}
	public RadarChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	public RadarChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	/*
	 * Data manipulation
	 */

	public void setData(ARCDataLayer ... dataIn) throws Exception{

		// Iterative size check
		for (ARCDataLayer element : dataIn) {	
			if(element.size() < DEFAULT_MIN_ELEMENT_DATALAYER) {
				throw new Exception(EXCEPTION_WRONG_SIZE);
			}
		}

		// Check compatibility
		for (int i = 0; i < dataIn.length; i++) {
			for (int j = 0; j < dataIn.length; j++) {

				if(!dataIn[i].check(dataIn[j])) {
					throw new Error(EXCEPTION_LAYER_COMPATIBILITY);
				}
			}	
		}
		
		// Add to dataLayers
		dataLayers = new ArrayList<ARCDataLayer>();
		for (ARCDataLayer element : dataIn) {	
			dataLayers.add(element);
		}
		
		updateChart();
	}
	public void addData(ARCDataLayer dataIn) throws Exception {

		// Size check
		if(dataIn.size() < DEFAULT_MIN_ELEMENT_DATALAYER) {
			throw new Exception(EXCEPTION_WRONG_SIZE);
		}

		// Compatibility check
		if(!(dataLayers == null) && !dataLayers.isEmpty()) {
			if(!dataLayers.get(0).check(dataIn)) {
				throw new Error(EXCEPTION_LAYER_COMPATIBILITY);
			}
		}else{
			if(dataLayers == null) dataLayers = new ArrayList<ARCDataLayer>();
		}

		dataLayers.add(dataIn);
		updateChart();
	}
	public void removeData(ARCDataLayer data) {
		if(dataLayers == null) return;
		if(!dataLayers.remove(data)) return;
		if(dataLayers.isEmpty()) dataLayers = null;
		updateChart();
	}
	public void clearData(){
		dataLayers = null;

		updateChart();
	}

	/*
	 * View Manipulation 
	 */

	private void updateChart(){

		if(dataLayers != null) { // addData, setData, removeData
			
			// Set labels
			this.labels = dataLayers.get(0).getLabels();
			
			// Set lonNum
			this.lonNum = labels.length;
			
			// Iterate for max value
			for (int i = 0; i < layersAllowed; i++) {
				if(dataLayers.size() <= i) break;
				if(maxValue < dataLayers.get(i).getMaxValue()) this.maxValue = dataLayers.get(i).getMaxValue();
			}
			
			// Clear all
			removeAllViewsInLayout();
			
			// Set grid view
			arcGridView = setGridToChart();
			addView(arcGridView);
			
			// Set data views
			arcDataLayerViews = new RadarDataLayer[layersAllowed];
			for (int i = 0; i < layersAllowed; i++) {
				if(dataLayers.size() <= i) break;
				arcDataLayerViews[i] = setDataLayerToChart(i);
				addView(arcDataLayerViews[i]);
			}
		
		}else{ // removeData (empty)

			this.maxValue = 0;
			this.labels = null;
			this.lonNum = 0;

			removeAllViews();

			arcDataLayerViews = null;
			arcGridView = null;
		}
	}
	private RadarGrid setGridToChart(){
		return new RadarGrid(context, lonNum, latNum, maxValue, labels, gridStyle);
	}
	private RadarDataLayer setDataLayerToChart(int index){
		return new RadarDataLayer(context, maxValue, dataLayers.get(index));
	}
	public void simultaneousLayersAllowed(int num) {
		this.layersAllowed = num;
		updateChart();
	}

	// Gesture methods
	public void stepForwardViewList(){} 	// TODO Rename & fill
	public void stepBackViewList(){} 		// TODO Rename & fill

	/*
	 * Animation methods
	 */

	public void startAnimation(){
		if(arcDataLayerViews == null) return; 
		for (int i = 0; i < arcDataLayerViews.length; i++) {
				arcDataLayerViews[i].startAnimation();
		}
	}

	/*
	 * ViewGroup overridden functions
	 */

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		//Call layout() on children
		int numOfChildren = this.getChildCount();
		for (int i=0; i < numOfChildren; i++ ) {
			View child = this.getChildAt(i);
			child.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
		}
	}

	/*
	 * Getters & Setters
	 */

	public void setLatitudeNum(int latNum) {
		this.latNum = latNum;
	}
	public void setGridStyle(GridLayerStyle style){
		this.gridStyle = style;
		if(arcGridView != null) arcGridView.setGridStyle(style);
	}
	public GridLayerStyle getGridStyle(){
		if(arcGridView == null) return gridStyle;
		else{
			gridStyle = arcGridView.getGridStyle();
			return gridStyle;
		}
	}
}
