package com.example.faisal.flowsensorreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    // THIS IS HOW TO UPLOAD TO GITHUB
    // 1) git add .
    // 2) git commit -m "YOUR CUSTOM MESSAGE"
    // 3) git push


    // THIS IS HOW TO DOWNLOAD
    // 1) git pull
    
    // this is an example edit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Tells android that you have this java file
        setContentView(R.layout.activity_home); // associates this java program with the layout file 'activity_home'

        Intent thisIntent = getIntent();
        if(thisIntent == null){
            logout();
            return;
        }
        final String username = thisIntent.getStringExtra("username");
        final String password = thisIntent.getStringExtra("password");

        if(username == null || password == null){
            logout();
            return;
        }

        // associate the button with an id of 'loginButton' with the variable 'logoutButton'
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        // Listen for the logout button to be clicked.
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               logout();
            }
        });


        Button mypageButton = (Button) findViewById(R.id.mypageButton);
        final Intent mypageIntent = new Intent(HomeActivity.this, MyPage.class)
                                        .putExtra("password", password)
                                        .putExtra("username", username);

        // Listen for the logout button to be clicked.
        mypageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to the login page.
                startActivity(mypageIntent);
            }
        });
    }

    private void logout(){
        final Intent logoutIntent = new Intent(this, MainActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // only do this for logging out!
        startActivity(logoutIntent);
    }
}
