package com.example.maks.maxwatchapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.maks.maxwatchapp.R;
import com.example.maks.maxwatchapp.constants.UserConstants;
import com.example.maks.maxwatchapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maks on 23/06/17.
 */

public class UserDetails extends Activity{
    User max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.max_status);

        final TextView statusTextView = (TextView) findViewById(R.id.status);
        final TextView energyLevelTextView = (TextView) findViewById(R.id.energyLevelTextView);

        /*
        final Button locationButton = (Button) findViewById(R.id.openMap);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("MAX", "OPEN MAPS");
                Intent showLocation = new  Intent(UserDetails.this, MaxMap.class);
                startActivity(showLocation);
            }
        });
        */

        //Intent activityThatCalled = getIntent();
        //String status =  activityThatCalled.getExtras().getString("status");

        DownloadedUserData downloadedUserData = new DownloadedUserData() {
            @Override
            public void success(Boolean success) {

                statusTextView.setText(max.getStatus());
                energyLevelTextView.setText(max.getEnergyLevel().toString());
            }
        };

        DownloadUserData(downloadedUserData);
    }

    void DownloadUserData(final DownloadedUserData listener){
        final JsonArrayRequest getUsers = new JsonArrayRequest(Request.Method.GET, UserConstants.getUsersUrlDev, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                try {
                    JSONArray users = response;
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);

                        String id = user.getString("_id");
                        String name = user.getString("name");
                        String status = user.getString("status");
                        Double energyLevel = user.getDouble("energyLevel");

                        JSONObject position = user.getJSONObject("position");
                        JSONObject coordinates = position.getJSONObject("coordinates");

                        Double latitude = coordinates.getDouble("lat");
                        Double longitude = coordinates.getDouble("long");

                        max = new User(id, name, status, energyLevel, longitude, latitude);
                    }
                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }
                listener.success(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("API", "Err" + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(getUsers);
    }
    public interface DownloadedUserData {
        void success(Boolean success);
    }
}
