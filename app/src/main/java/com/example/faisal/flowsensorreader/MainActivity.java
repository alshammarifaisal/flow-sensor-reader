// Max Chehab contact: maxchehab@gmail.com

package com.example.faisal.flowsensorreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Get access to contact button
        Button contactButton = (Button) findViewById(R.id.contactButton);

        //Listen for the button to be clicked and run any code inside of the function onClick(View v).
        contactButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Create intent.

                String subject = "Contact Flow Sensor";    // subject for email
                String address = "contact@flow.com";      // email address for the company

                Intent intent = new Intent(Intent.ACTION_VIEW); // Create intent object, a query to the android phone to create an email
                intent.setType("text/plain"); // the content of the intent is in plain text
                intent.putExtra(Intent.EXTRA_EMAIL, address); //specify the address of the email
                intent.putExtra(Intent.EXTRA_SUBJECT, subject); // specify the subject of the email

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        final EditText usernameText = (EditText) findViewById(R.id.usernameInput);
        final EditText passwordText = (EditText) findViewById(R.id.passwordInput);
        Button loginButton = (Button) findViewById(R.id.loginButton);


        final Intent homePageIntent = new Intent (this, HomeActivity.class);


        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                if(username.length() == 0 || password.length() == 0){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Invalid Username or Password");
                    alertDialog.setMessage("Your username and password must not be empty.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    startActivity(homePageIntent);
                }
            }
        });
    }
}
