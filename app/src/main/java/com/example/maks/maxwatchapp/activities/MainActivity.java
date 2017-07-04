package com.example.maks.maxwatchapp.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MetaData metaData;
    String apkUrl = "";
    final ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_watch);

        RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.activity_watch);
        rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckAvailableUpdate();
            }
        });

        DownloadedMetaData downloadedMetaData = new DownloadedMetaData() {
            @Override
            public void success(Boolean success) {
                metaData = DataService.getInstance().GetMetaData();
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
        Intent showWatchScreen = new Intent(this, DetailsMap.class);

        Boolean isMax = false;
        File file = new File(Environment.getExternalStorageDirectory() + "/maxWatchConfig" ,"config.txt");
        if(file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                line = br.readLine();
                br.close();

                isMax = line.contains("isMax");

                Log.v("MAX READER", line);
            } catch (IOException e) {
                //You'll need to add proper error handling here
            }
        }

        startActivity(isMax ? showLoginScreen : showWatchScreen);
    }

    public interface DownloadedMetaData {
        void success(Boolean success);
    }
}
