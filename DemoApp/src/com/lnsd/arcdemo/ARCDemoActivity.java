package com.lnsd.arcdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.lnsd.arcdemo.entity.ARCDataLayer;
import com.lnsd.arcdemo.entity.DataLayerStyle;
import com.lnsd.arcdemo.entity.GridLayerStyle;
import com.lnsd.arcdemo.view.RadarChart;
import com.lnsd.arcdemo.view.RadarGrid;

public class ARCDemoActivity extends Activity {

	protected final static String TAG = "ARCDemoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_arclayout);

		RadarChart chart = (RadarChart)findViewById(R.id.arcChart);
		chart.setLatitudeNum(7);
		
		GridLayerStyle gStyle = new GridLayerStyle();
		gStyle.setGridChartType(GridLayerStyle.ARC_SPIDER_WEB_GRIDTYPE);
		gStyle.setBackgroundColor(Color.TRANSPARENT);
		gStyle.setGridBorderColor(Color.parseColor("#256A91"));
		gStyle.setGridBorderStrokeWidth(10f);
		
		gStyle.setGridLatitudeColor(Color.parseColor("#87CDB9"));
		gStyle.setGridLongitudeColor(Color.parseColor("#87CDB9"));
		gStyle.setGridStrokeWidth(2f);
		
		gStyle.setGridLabelColor(Color.parseColor("#F04132"));
		gStyle.setGridLabelPadding(40);
		gStyle.setGridLabelSize(40f);
		
		gStyle.setGridScaleColor(Color.parseColor("#F04132"));
		gStyle.setGridScaleSize(25f);
		gStyle.setGridScaleLabelPadding(2f);
		chart.setGridStyle(gStyle);
		
		DataLayerStyle p = new DataLayerStyle();
		p.setLayerBorderColor(Color.parseColor("#8AE659"));
		p.setLayerBorderWidth(DataLayerStyle.DEFAULT_LBORDER_WIDTH);
		p.setLayerFillColor(-1);
		p.setLayerFillAlpha(DataLayerStyle.DEFAULT_LFILL_ALPHA);

		ARCDataLayer data1 = new ARCDataLayer(p);

		data1.put("A", 2f);
		data1.put("B", 10f);
		data1.put("C", 5f);
		data1.put("D", 2f);
		data1.put("E", 10f);
		data1.put("F", 5f);
		
		p = new DataLayerStyle();
		p.setLayerBorderColor(Color.YELLOW);
		p.setLayerBorderWidth(DataLayerStyle.DEFAULT_LBORDER_WIDTH);
		p.setLayerFillColor(-1);
		p.setLayerFillAlpha(DataLayerStyle.DEFAULT_LFILL_ALPHA);

		ARCDataLayer data2 = new ARCDataLayer(p);

		data2.put("A", 3f);
		data2.put("B", 7f);
		data2.put("C", 12f);
		data2.put("D", 7f);
		data2.put("E", 12f);
		data2.put("F", 3f);
		
		p = new DataLayerStyle();
		p.setLayerBorderColor(Color.parseColor("#F04132"));
		p.setLayerBorderWidth(DataLayerStyle.DEFAULT_LBORDER_WIDTH);
		p.setLayerFillColor(-1);
		p.setLayerFillAlpha(DataLayerStyle.DEFAULT_LFILL_ALPHA);

		ARCDataLayer data3 = new ARCDataLayer(p);

		data3.put("A", 12f);
		data3.put("B", 3f);
		data3.put("C", 9f);
		data3.put("D", 3f);

		Log.d(TAG, "Compatiblility(data1, data2) = "+data1.check(data2));
		Log.d(TAG, "Compatiblility(data1, data2) = "+data1.check(data3));
		
		data3.put("E", 9f); 
		data3.put("F", 3f);
		
		ArrayList<ARCDataLayer> data = new ArrayList<ARCDataLayer>();
		data.add(data1);
		data.add(data2);
		data.add(data3);

		try {
			chart.setData((ARCDataLayer[]) data.toArray(new ARCDataLayer[data.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}
