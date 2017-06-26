package com.example.maks.maxwatchapp.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.maks.maxwatchapp.R;
import com.example.maks.maxwatchapp.constants.UserConstants;
import com.example.maks.maxwatchapp.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailsMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView statusTextView;
    private TextView energyLevelTextView;
    User max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MAXOUTPUT", " DETAILS MAP CREATED");
        setContentView(R.layout.max_status);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        statusTextView = (TextView) findViewById(R.id.status);
        energyLevelTextView = (TextView) findViewById(R.id.energyLevelTextView);

        UserDetails.DownloadedUserData downloadedUserData = new UserDetails.DownloadedUserData() {
            @Override
            public void success(Boolean success) {

                UpdateUI();

                LatLng currentPos = new LatLng(max.getLatitude(), max.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentPos).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos));
            }
        };

        DownloadUserData(downloadedUserData);
    }

    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        Log.v("MAXOUTPUT", " DETAILS MAP STARTED");
    }


    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        Log.v("MAXOUTPUT", " DETAILS MAP RESUMED");
    }

    void DownloadUserData(final UserDetails.DownloadedUserData listener){
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    void UpdateUI() {
        LatLng maxPosition = new LatLng(max.getLatitude(), max.getLongitude());
        mMap.addMarker(new MarkerOptions().position(maxPosition).title("Current Max"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maxPosition, 10));

        statusTextView.setText(max.getStatus());
        energyLevelTextView.setText(max.getEnergyLevel().toString());
    }
}
