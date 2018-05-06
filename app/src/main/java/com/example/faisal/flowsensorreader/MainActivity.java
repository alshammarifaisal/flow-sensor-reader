// Max Chehab contact: maxchehab@gmail.com

package com.example.faisal.flowsensorreader;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.hogel.android.linechartview.LineChartView;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    /*



    <EditText
    android:id="@+id/passwordInput"  <- Id to reference with findViewById(R.id.passwordInput)
    android:layout_width="wrap_content"
    android:layout_height="50dp" <-height
    android:layout_below="@+id/editText"
    android:layout_centerHorizontal="true"
    android:layout_marginTop="42dp"
    android:ems="10"
    android:hint="password"
    android:inputType="textPassword" />
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super teelling the andoid to access this page.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/// final = means theat does not change
        final Activity mainActivity = this;
        //this means refer to the main activity
        //Get access to contact button
        // contact button emaill
        /// refuer to the id which contact button to the activity main xml

        Button contactButton = (Button) findViewById(R.id.contactButton);
        // when you clik, it should runs

        //Listen for the button to be clicked and run any code inside of the function onClick(View v).
        contactButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // the subject contact flow sensor
                String subject = "Contact Flow Sensor";    // subject for email
                String address = "zkohl@rubiconsensors.com";      // email address for the company

                ShareCompat.IntentBuilder.from(mainActivity)
                        .setType("message/rfc822")
                        .addEmailTo(address)
                        .setSubject(subject)
                        .setChooserTitle("Select mail client")///option which email
                        .startChooser();
            }
        });

        // The only reason the 'final' keyword is here is because it is being
        // accessed from a private class. The private class is new View.OnClicListener()...
        final EditText usernameText = (EditText) findViewById(R.id.usernameInput);
        //EditText is the type that it takes from activity_mail.xml
        final EditText passwordText = (EditText) findViewById(R.id.passwordInput);
        //EditText is the type that it takes from activity_mail.xml

        Button loginButton = (Button) findViewById(R.id.loginButton);
        //Button is the type that it takes from activity_mail.xml



        //When the user presses the 'loginButton' it runs this code.
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //access to the text inside of 'usernameText' and 'passwordText'
                final String username = usernameText.getText().toString();
                final String password = passwordText.getText().toString();

                // Check if the username and password is empty
                if(username.length() == 0 || password.length() == 0){
                    //If the username and password is empty, tell the user that something is wrong.
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Invalid Username or Password"); // The title of the alert
                    alertDialog.setMessage("Your username and password must not be empty."); //message of the alert
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss(); // when you press 'ok' it removes the alert
                                }
                            });
                    alertDialog.show(); // show the alert.
                } else {
                    String url = String.format("https://%s:%s@riversense.rubiconsensors.com/api2", username, password); // url where to get the data

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                             startActivity(new Intent (MainActivity.this, HomeActivity.class).
                                     putExtra("password", password).
                                     putExtra("username", username));
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Invalid Username or Password"); // The title of the alert
                            alertDialog.setMessage("Your username or password is invalid."); //message of the alert
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss(); // when you press 'ok' it removes the alert
                                        }
                                    });
                            alertDialog.show(); // show the alert.
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
            }
        });
    }
}
