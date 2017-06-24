package com.example.maks.maxwatchapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.maks.maxwatchapp.R;

/**
 * Created by Maks on 23/06/17.
 */

public class UserDetails extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.max_status);
        Intent activityThatCalled = getIntent();
        String status =  activityThatCalled.getExtras().getString("status");
        TextView statusTextView = (TextView) findViewById(R.id.status);
        statusTextView.setText(status);
    }
}
