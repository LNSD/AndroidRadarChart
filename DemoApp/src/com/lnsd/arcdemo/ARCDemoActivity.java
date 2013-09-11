package com.lnsd.arcdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.lnsd.arcdemo.entity.TitleValueEntity;
import com.lnsd.arcdemo.view.RadarChart;

public class ARCDemoActivity extends Activity {

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
	        
			List<TitleValueEntity> data1 = new ArrayList<TitleValueEntity>();
			data1.add(new TitleValueEntity("A", 1));
			data1.add(new TitleValueEntity("B", 1));
			data1.add(new TitleValueEntity("C", 1));
			data1.add(new TitleValueEntity("D", 1));
			data1.add(new TitleValueEntity("E", 1));
			data1.add(new TitleValueEntity("F", 1));

			List<TitleValueEntity> data2 = new ArrayList<TitleValueEntity>();
			data2.add(new TitleValueEntity("A", 2));
			data2.add(new TitleValueEntity("B", 2));
			data2.add(new TitleValueEntity("C", 2));
			data2.add(new TitleValueEntity("D", 2));
			data2.add(new TitleValueEntity("E", 2));
			data2.add(new TitleValueEntity("F", 2));

			List<List<TitleValueEntity>> data = new ArrayList<List<TitleValueEntity>>();
			data.add(data1);
			data.add(data2);

			chart.setData(data);
			chart.setLongitudeNum(5);
			chart.setLatitudeNum(10);
	    
	}
}
