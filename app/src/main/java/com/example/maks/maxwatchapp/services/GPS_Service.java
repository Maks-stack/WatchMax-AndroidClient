package com.example.maks.maxwatchapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.maks.maxwatchapp.data.DataService;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by Maks on 26/06/17.
 */

public class GPS_Service extends Service {

    private LocationListener listener;
    private LocationManager manager;
    private Location lastLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;

                Intent i = new Intent("location_update");
                i.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude());
                sendBroadcast(i);
                DataService.getInstance().SendGpsLocation(getBaseContext(), lastLocation.getLongitude(), lastLocation.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                //noinspection MissingPermission
                lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        //noinspection MissingPermission
        manager.requestSingleUpdate(criteria, listener, null);

        //noinspection MissingPermission
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(manager != null){
            //noinspection MissingPermission
            manager.removeUpdates(listener);
        }
    }

    public interface GpsDataSent {
        void success(Boolean success);
    }
}
