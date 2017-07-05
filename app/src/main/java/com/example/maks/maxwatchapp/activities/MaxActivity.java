package com.example.maks.maxwatchapp.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.maks.maxwatchapp.R;
import com.example.maks.maxwatchapp.data.DataService;
import com.example.maks.maxwatchapp.services.GPS_Service;

/**
 * Created by Maks on 25/06/17.
 */

public class MaxActivity extends Activity {

    private Button startGpsButton;
    private Button stopGpsButton;

    private EditText messageField;
    private Button sendMessageButton;

    private BroadcastReceiver broadCastReceiver;

    @Override
    protected void onResume() {
        super.onResume();

        if(broadCastReceiver == null) {
            broadCastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Log.v("MAX", "COORDINATES: " + intent.getExtras().get("coordinates"));
                }
            };
        }
        registerReceiver(broadCastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.max_status_edit);

        startGpsButton = (Button) findViewById(R.id.startGpsService);
        stopGpsButton = (Button) findViewById(R.id.stopGpsService);

        messageField = (EditText) findViewById(R.id.messageInput);
        sendMessageButton = (Button) findViewById(R.id.sendMessage);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("Max", "message: " + messageField.getText());
                DataService.getInstance().SendMessage(getBaseContext(), messageField.getText().toString());
            }
        });

        if(!RuntimePermissions()) {
            EnableButtons();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadCastReceiver != null) {
            unregisterReceiver(broadCastReceiver);
        }
    }

    private Boolean RuntimePermissions(){
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);

            return true;
            }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                EnableButtons();
            }else{
                RuntimePermissions();
            }
        }
    }

    private void EnableButtons() {
        startGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                startService(i);
            }
        });
        stopGpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                stopService(i);
            }
        });
    }
}
