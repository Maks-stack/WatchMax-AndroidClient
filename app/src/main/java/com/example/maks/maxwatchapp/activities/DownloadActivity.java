package com.example.maks.maxwatchapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.maks.maxwatchapp.R;

import java.io.File;

/**
 * Created by Maks on 24/06/17.
 */

public class DownloadActivity extends Activity {

    DownloadManager downloadManager;
    Button downloadButton;

    String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_apk);

        downloadButton = (Button) findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("MAX", "DATA DIRECTORY: " + filePath);
                DeleteFiles();
                CheckPermission();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
            DownloadUpdate();
        }
    }

    void CheckPermission() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            DownloadUpdate();
        }
    }

    void DeleteFiles(){
        File dir = new File("/storage/emulated/0/Android/data/com.example.maks.maxwatchapp/files" + filePath);

        Log.v("MAX", "Dir exists: " + dir.exists());

        File[] files = dir.listFiles();
        for(int i = 0; i < files.length; i++) {
            boolean deleted = files[i].delete();
            Log.v("MAX", "Deleted: " + deleted);
        }
    }

    void DownloadUpdate(){

        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://rawgit.com/Maksmaksmakz/WatchMax-AndroidClient/master/downloadBuild/app-debug.apk");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(this, filePath, "app-debug.apk");
        Long reference = downloadManager.enqueue(request);
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

        Log.v("MAX", "DATA DIRECTORY: " + filePath);

        BroadcastReceiver onComplete=new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}
