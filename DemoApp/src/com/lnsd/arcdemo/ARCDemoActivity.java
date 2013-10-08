package com.lnsd.arcdemo;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.lnsd.arcdemo.entity.ARCDataEntity;
import com.lnsd.arcdemo.entity.ARCDataLayer;
import com.lnsd.arcdemo.view.RadarChart;

public class ARCDemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_arclayout);

		RadarChart chart = (RadarChart)findViewById(R.id.arcChart);
		//chart.setChartGridType(RadarChart.NO_GRID);
		//chart.showAxis(false);
		//chart.showLabes(false);
		//chart.setGridLabelPadding(-40f);
		//chart.setBackgroundColor(Color.TRANSPARENT);
		//chart.setGridBorderColor(Color.RED);
		//chart.setGridBorderStrokeWidth(30f);
		//chart.setGridLatitudeColor(Color.parseColor("#34495e"));
		//chart.setGridLongitudeColor(Color.BLUE);
		//chart.setGridLabelColor(Color.parseColor("#e67e22"));
		//chart.setGridStrokeWidth(40f);
		//chart.setGridLabelSize(40f);

		ARCDataLayer data1 = new ARCDataLayer(
				Color.BLUE, ARCDataLayer.DEFAULT_LBORDER_WIDTH,
				Color.BLUE, ARCDataLayer.DEFAULT_LFILL_ALPHA);
		data1.add(new ARCDataEntity("A", 2, Color.MAGENTA));
		data1.add(new ARCDataEntity("B", 11, Color.MAGENTA));
		data1.add(new ARCDataEntity("C", 5, Color.MAGENTA));
		data1.add(new ARCDataEntity("D", 2, Color.MAGENTA));
		data1.add(new ARCDataEntity("E", 10, Color.MAGENTA, 10f));
		data1.add(new ARCDataEntity("F", 5, Color.MAGENTA));

		ARCDataLayer data2 = new ARCDataLayer(
				Color.GREEN, ARCDataLayer.DEFAULT_LBORDER_WIDTH,
				Color.GREEN, ARCDataLayer.DEFAULT_LFILL_ALPHA);
		data2.add(new ARCDataEntity("A", 10));
		data2.add(new ARCDataEntity("B", 2));
		data2.add(new ARCDataEntity("C", 5));
		data2.add(new ARCDataEntity("D", 20));
		data2.add(new ARCDataEntity("E", 2));
		data2.add(new ARCDataEntity("F", 5));

		ArrayList<ARCDataLayer> data = new ArrayList<ARCDataLayer>();
		data.add(data1);
		data.add(data2);

		chart.setData(data);
		//chart.setLongitudeNum(5);
		//chart.setLatitudeNum(5);

	}
}
