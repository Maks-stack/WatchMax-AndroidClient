package com.example.maks.maxwatchapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.maks.maxwatchapp.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by Maks on 25/06/17.
 */

public class LoginActivity extends Activity {

    Button loginMax;
    Button loginWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        Boolean isMax = (Environment.getExternalStorageDirectory() + "/maxWatchConfig").contains("isMax");

        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

        loginMax = (Button) findViewById(R.id.maxButton);
        loginWatch = (Button) findViewById(R.id.watchButton);

        loginMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMaxActivity();
            }
        });
        loginWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenWatchActivity();
            }
        });
    }
    void OpenMaxActivity(){
        Intent showLoginScreen = new Intent(this, MaxActivity.class);
        startActivity(showLoginScreen);
    }
    void OpenWatchActivity(){
        Intent showLoginScreen = new Intent(this, DetailsMap.class);
        startActivity(showLoginScreen);
    }
}
