package com.example.maks.maxwatchapp.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.maks.maxwatchapp.R;
import com.example.maks.maxwatchapp.constants.DataConstants;
import com.example.maks.maxwatchapp.constants.UserConstants;
import com.example.maks.maxwatchapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Boolean updateAvailable = false;
    String apkUrl = "";
    final ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        DownloadedMetaData downloadedMetaData = new DownloadedMetaData() {
            @Override
            public void success(Boolean success) {
                if(updateAvailable) {
                    OpenUpdateIntent();
                }else {
                    OpenUserProfileIntent();
                }
            }
        };
        CheckAvailableUpdate(downloadedMetaData);
    }

    void CheckAvailableUpdate(final DownloadedMetaData listener) {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            final Integer versionNumberFromApp = packageInfo.versionCode;

            final JsonObjectRequest getMetaData = new JsonObjectRequest(Request.Method.GET, DataConstants.getMetaDataUrlDev, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                    try {
                        JSONObject metaData = response;

                        final Integer versionNumberFromServer = metaData.getInt("version");
                        boolean forceUpdate = metaData.getBoolean("forceUpdate");

                        Log.v("MAX", "New Version from Server : " + versionNumberFromServer + "OldVersion from Manifest : " + versionNumberFromApp);
                        updateAvailable = versionNumberFromServer > versionNumberFromApp || forceUpdate;
                        apkUrl = metaData.getString("downloadLink");

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
            Volley.newRequestQueue(this).add(getMetaData);
        }catch(PackageManager.NameNotFoundException e) {
            Log.v("MAX", e.toString());
        }
    }

    void OpenUpdateIntent(){
        Intent openDownloadDialog = new Intent(this, DownloadActivity.class);
        openDownloadDialog.putExtra("url", apkUrl);
        startActivity(openDownloadDialog);
    }

    void OpenUserProfileIntent() {
        Intent showUserDetails = new Intent(this, DetailsMap.class);
        startActivity(showUserDetails);
    }

    public interface DownloadedMetaData {
        void success(Boolean success);
    }
}
