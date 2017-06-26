package com.example.maks.maxwatchapp.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
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
import com.example.maks.maxwatchapp.data.DataService;
import com.example.maks.maxwatchapp.models.MetaData;
import com.example.maks.maxwatchapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MetaData metaData;
    String apkUrl = "";
    final ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        DownloadedMetaData downloadedMetaData = new DownloadedMetaData() {
            @Override
            public void success(Boolean success) {
                metaData = DataService.getInstance().GetMetaData();
                CheckAvailableUpdate();
            }
        };
        DataService.getInstance().DownloadMetaData(this, downloadedMetaData);

    }

    void CheckAvailableUpdate() {
        Integer versionNumberFromApp = 0;
        Integer versionNumberFromServer = metaData.getVersionNumber();
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionNumberFromApp = packageInfo.versionCode;

        }catch(PackageManager.NameNotFoundException e) {
            Log.v("MAX", e.toString());
        }

        if(versionNumberFromApp < versionNumberFromServer) {
            OpenUpdateIntent();
        }else {
            OpenUserProfileIntent();
        }
    }

    void OpenUpdateIntent(){
        Intent openDownloadDialog = new Intent(this, DownloadActivity.class);
        openDownloadDialog.putExtra("url", metaData.getDownloadLink());
        startActivity(openDownloadDialog);
    }

    void OpenUserProfileIntent() {
        Intent showLoginScreen = new Intent(this, LoginActivity.class);
        startActivity(showLoginScreen);
    }

    public interface DownloadedMetaData {
        void success(Boolean success);
    }
}
