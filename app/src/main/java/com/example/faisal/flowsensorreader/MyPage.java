package com.example.faisal.flowsensorreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.hogel.android.linechartview.DateLineChartView;
import org.hogel.android.linechartview.LineChartStyle;
import org.hogel.android.linechartview.LineChartView;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // Reference the chart Container
        ViewGroup chartContainer = (ViewGroup) findViewById(R.id.chart_parent);

        LineChartStyle lineChartStyle = new LineChartStyle();
        lineChartStyle.setDrawPointCenter(false);
        LineChartStyle.Border leftBottomBorder = new LineChartStyle.Border(
                LineChartStyle.Border.LEFT, LineChartStyle.Border.BOTTOM
        );
        leftBottomBorder.setWidth(8.0f);
        lineChartStyle.addBorder(leftBottomBorder);
        lineChartStyle.setXLabelFormatter(new LineChartStyle.LabelFormatter() {
            @Override
            public String format(long value) {
                return DateFormat.format("dd:HH:mm:ss", value).toString();
            }
        });
        lineChartStyle.setYLabelWidth(80.0f);

        final DateLineChartView chartView = new DateLineChartView(this, lineChartStyle);
        chartView.setStyle(lineChartStyle);
        chartContainer.addView(chartView);



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://riversense.rubiconsensors.com/api";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            // Save response as a JSONObject
                            JSONObject root  = new JSONObject(response);
                            // Get all keys
                            Iterator iter = root.keys();
                            // Create list of points
                            List<LineChartView.Point> points = new ArrayList<>();

                            // Loop through all the keys
                            while(iter.hasNext()){
                                // Get key name (Date)
                                String key = (String)iter.next();
                                // Get value name (sensor data)
                                long value = root.getLong(key);

                                // Add to list of points (may not parse correctly)
                                try {
                                    points.add(new LineChartView.Point(date(key), value));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            // Set the points
                            chartView.setPoints(points);
                        } catch(Exception ex){
                            System.out.println(ex.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });

        queue.add(stringRequest);
    }


    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private long date(String date) throws ParseException {
        return dateFormat.parse(date).getTime();
    }

}
