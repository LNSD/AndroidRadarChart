package com.lnsd.arcdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.lnsd.arcdemo.entity.ARCDataLayer;
import com.lnsd.arcdemo.entity.DataLayerParams;
import com.lnsd.arcdemo.view.RadarChart;

public class ARCDemoActivity extends Activity {

	protected final static String TAG = "ARCDemoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_arclayout);


		RadarChart chart = (RadarChart)findViewById(R.id.arcChart);
		//chart.setChartGridType(RadarChart.SPIDER_WEB_CHART);
		//chart.setGridLabelPadding(-40f);
		//chart.setBackgroundColor(Color.TRANSPARENT);
		//chart.setGridBorderColor(Color.RED);
		//chart.setGridBorderStrokeWidth(30f);
		//chart.setGridLatitudeColor(Color.parseColor("#34495e"));
		//chart.setGridLongitudeColor(Color.BLUE);
		//chart.setGridLabelColor(Color.parseColor("#e67e22"));
		//chart.setGridStrokeWidth(40f);
		//chart.setGridLabelSize(40f);

		DataLayerParams p = new DataLayerParams();
		p.setLayerBorderColor(Color.BLUE);
		p.setLayerBorderWidth(DataLayerParams.DEFAULT_LBORDER_WIDTH);
		p.setLayerFillColor(Color.BLUE);
		p.setLayerFillAlpha(DataLayerParams.DEFAULT_LFILL_ALPHA);

		ARCDataLayer data1 = new ARCDataLayer(p);

		data1.put("A", 2f);
		data1.put("B", 10f);
		data1.put("C", 5f);
		data1.put("D", 2f);
		data1.put("E", 10f);
		data1.put("F", 5f);

		p.setLayerBorderColor(Color.RED);
		p.setLayerBorderWidth(DataLayerParams.DEFAULT_LBORDER_WIDTH);
		p.setLayerFillColor(Color.RED);
		p.setLayerFillAlpha(DataLayerParams.DEFAULT_LFILL_ALPHA);

		ARCDataLayer data2 = new ARCDataLayer(p);

		data2.put("A", 3f);
		data2.put("B", 7f);
		data2.put("C", 12f);
		data2.put("D", 7f);
		data2.put("E", 12f);
		data2.put("F", 3f);
		
		p.setLayerBorderColor(Color.YELLOW);
		p.setLayerBorderWidth(DataLayerParams.DEFAULT_LBORDER_WIDTH);
		p.setLayerFillColor(Color.YELLOW);
		p.setLayerFillAlpha(DataLayerParams.DEFAULT_LFILL_ALPHA);

		ARCDataLayer data3 = new ARCDataLayer(p);

		data3.put("A", 3f);
		data3.put("B", 7f);
		data3.put("C", 12f);
		data3.put("D", 7f);
		//data3.put("E", 12f); // Deliberately not added to data layer 
		//data3.put("F", 3f);  // Deliberately not added to data layer

		Log.d(TAG, "Compatiblility(data1, data2) = "+data1.check(data2));
		Log.d(TAG, "Compatiblility(data1, data2) = "+data1.check(data3));
		
		ArrayList<ARCDataLayer> data = new ArrayList<ARCDataLayer>();
		data.add(data1);
		data.add(data2);

		chart.setData(data);
		chart.setLongitudeNum(6);
		chart.setLatitudeNum(10);
	}
}
