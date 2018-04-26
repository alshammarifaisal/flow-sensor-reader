package com.example.faisal.flowsensorreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.hogel.android.linechartview.LineChartView;

import java.util.ArrayList;
import java.util.List;

public class MyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        LineChartView chartView = (LineChartView) findViewById(R.id.chart_view);
        chartView.setManualMinY(0);

        List<LineChartView.Point> points = new ArrayList<>();

        long[][] data = new long[][]{
                {0, 0},
                {76, 76},
                {100, 100},
                {200, 20}
        };

        for(long[] point : data){
            System.out.println(point[0] + ", " + point[1]);
            points.add(new LineChartView.Point(point[0], point[1]));
        }

        chartView.setPoints(points);

    }
}
