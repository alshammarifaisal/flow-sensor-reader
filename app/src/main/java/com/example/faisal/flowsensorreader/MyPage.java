package com.example.faisal.flowsensorreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyPage extends AppCompatActivity {
    // dateFormat tells the computer what format the JSON date will be in.
    // for example 2018-04-12T14:42:42.230Z
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Tell android to show activity_my_page.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        Intent thisIntent = getIntent();
        if(thisIntent == null){
            System.out.println("thisIntent is null");
            logout();
            return;
        }
        final String username = thisIntent.getStringExtra("username");
        final String password = thisIntent.getStringExtra("password");

        if(username == null || password == null){
            System.out.println("username or password was null");
            System.out.println(username + ":" + password);
            logout();
            return;
        }

        // Reference the chart Container refers to the id 'chart_parent'
        ViewGroup chartContainer = (ViewGroup) findViewById(R.id.chart_parent);

        // Details the style for the line chart
        // https://github.com/hogelog/line-chart-view/blob/master/line-chart-view-demo/src/main/java/org/hogel/android/linechartviewdemo/DateLineChartActivity.java
        LineChartStyle lineChartStyle = new LineChartStyle(); // create new LineChartStyle object
        lineChartStyle.setDrawPointCenter(false); // have no center point at origin
        LineChartStyle.Border leftBottomBorder = new LineChartStyle.Border( // describe the border.
                LineChartStyle.Border.LEFT, LineChartStyle.Border.BOTTOM // the border is at the left and the bottom only.
        );
        leftBottomBorder.setWidth(8.0f); // set the width of the lines 'f' means the data type of 8 is a float
        lineChartStyle.addBorder(leftBottomBorder); // applies the border
        lineChartStyle.setXLabelFormatter(new LineChartStyle.LabelFormatter() {
            @Override
            public String format(long value) {
                return DateFormat.format("MM/dd", value).toString(); // apply the date format for the line chart
            }
        });

        // 80 pixels between the graph and the edge of the phone
        lineChartStyle.setYLabelWidth(80.0f); // set the y label width. how much space the y axis label has.

        final DateLineChartView chartView = new DateLineChartView(this, lineChartStyle); // create our line chart and apply the style
        chartView.setStyle(lineChartStyle); // set style again. you must
        chartContainer.addView(chartView); // insert the line chart into the activity 'activity_my_page.xml'

        String url = String.format("https://%s:%s@riversense.rubiconsensors.com/api2", username, password); // url where to get the data

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
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
                        // key-> "2018-04-12T14:42:42.230Z": value->0.0
                        String key = (String)iter.next();

                        // Get value name (sensor data)
                        long value = root.getLong(key);

                        // Add to list of points (may not parse correctly)
                        try {
                            //check if the date is greater than a week ago
                            Calendar c=Calendar.getInstance();
                            c.add(Calendar.DATE,-7); // Current date - 7 = 7 days ago
                            if(c.getTime().compareTo(dateFormat.parse(key)) <= 0){
                                // if the date is less than a week, then add to list of points
                                System.out.println("adding: " + key);
                                points.add(new LineChartView.Point(date(key), value));
                            }

                        } catch (ParseException e) {
                            throw new RuntimeException(e); // if there is an error, stop the program
                        }
                    }

                    // add the points to the line chart
                    chartView.setPoints(points);
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logout();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                String creds = String.format("%s:%s",username,password);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;

            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private long date(String date) throws ParseException {
        return dateFormat.parse(date).getTime();
    }

    private void logout(){
        final Intent logoutIntent = new Intent(this, MainActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // only do this for logging out!
        startActivity(logoutIntent);
    }

}
