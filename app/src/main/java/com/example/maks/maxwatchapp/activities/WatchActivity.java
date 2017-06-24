package com.example.maks.maxwatchapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.maks.maxwatchapp.R;
import com.example.maks.maxwatchapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WatchActivity extends AppCompatActivity {

    final ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        String url = "http://max-watch-api.makshub.com/api/v1/users";

        final JsonArrayRequest getUsers = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());

                try{
                    JSONArray users = response;
                    for(int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);
                        String id = user.getString("_id");
                        String name = user.getString("name");
                        String status = user.getString("status");
                        Double energyLevel = user.getDouble("energyLevel");

                        JSONObject position = user.getJSONObject("position");
                        JSONObject coordinates = position.getJSONObject("coordinates");

                        Double latitude = coordinates.getDouble("lat");
                        Double longitude = coordinates.getDouble("long");

                        User newUser = new User(id, name, status, energyLevel, longitude, latitude);

                        userList.add(newUser);
                        System.out.println("This is the user statuZZZZZZZZ: " + newUser.getStatus());
                    }
                }catch(JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }

                ShowUserDetails();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("API", "Err" + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(getUsers);
    }
    void ShowUserDetails() {
        Intent showUserDetails = new Intent(this, UserDetails.class); //new Intent(this, UserDetails.class);
        showUserDetails.putExtra("status", userList.get(0).getStatus());
        startActivity(showUserDetails);
    }
}
