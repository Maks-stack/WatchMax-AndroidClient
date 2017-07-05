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
import com.example.maks.maxwatchapp.data.DataService;
import com.example.maks.maxwatchapp.models.Message;
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

import java.util.ArrayList;

public class DetailsMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView statusTextView;
    private TextView energyLevelTextView;
    private TextView messageTextView;
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
        messageTextView = (TextView) findViewById(R.id.message);

        DetailsMap.DownloadedUserData downloadedUserData = new DetailsMap.DownloadedUserData() {
            @Override
            public void success(Boolean success) {

                if(success) {
                    max = DataService.getInstance().GetUser();
                    UpdateUI();
                }
            }
        };

        DataService.getInstance().DownloadUserData(this, downloadedUserData);
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

    public interface DownloadedUserData {
        void success(Boolean success);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    void UpdateUI() {
        Log.v("MAX", "USING DATA");
        LatLng maxPosition = new LatLng(max.getLatitude(), max.getLongitude());
        mMap.addMarker(new MarkerOptions().position(maxPosition).title("Current Max"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maxPosition, 10));

        statusTextView.setText(max.getStatus());
        energyLevelTextView.setText(max.getEnergyLevel().toString());
        ArrayList<Message> messages = max.getMessages();
        Log.v("MAX DetailsMap line 106", "Messages Length: " + messages.size());
        if(messages.size() > 0) {
            messageTextView.setText(messages.get(messages.size() - 1).getText());
        }else {
            messageTextView.setText("Herp derpsum dee pee, ler herpler derp merp serp berp. Herpy dee nerpy perper ner sherp. Herderder derpsum ler derperker berp derpy! Herpler herpy merp dee sherlamer terpus, tee berps me perper? Derp sherlamer me herpler dee. Terp tee sherp perp terpus merp me ler derperker. Perper sherlamer derpy terpus dee sherp der herpderpsmer derpsum perp. Mer berp terp me tee nerpy. Derp derps terp dee derperker ter ler herpler perp. Derpy terpus sherpus ner sherp mer berp derp. Nerpy ter herpy, mer derps.");
        }
    }
}
