package com.example.faisal.flowsensorreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    //hello world
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Tells android that you have this java file
        setContentView(R.layout.activity_home); // associates this java program with the layout file 'activity_home'

        // associate the button with an id of 'loginButton' with the variable 'logoutButton'
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        // This intent will tell android to go the MainActivity (the login page)
        final Intent logoutIntent = new Intent(this, MainActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // only do this for logging out!

        // Listen for the logout button to be clicked.
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to the login page.
                startActivity(logoutIntent);
            }
        });


        Button mypageButton = (Button) findViewById(R.id.mypageButton);
        final Intent mypageIntent = new Intent(this, MyPage.class);

        // Listen for the logout button to be clicked.
        mypageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to the login page.
                startActivity(mypageIntent);
            }
        });
    }
}
