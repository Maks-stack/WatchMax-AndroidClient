package com.example.maks.maxwatchapp.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.maks.maxwatchapp.activities.DetailsMap;
import com.example.maks.maxwatchapp.activities.MainActivity;
import com.example.maks.maxwatchapp.constants.DataConstants;
import com.example.maks.maxwatchapp.constants.UserConstants;
import com.example.maks.maxwatchapp.models.MetaData;
import com.example.maks.maxwatchapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Maks on 26/06/17.
 */

public class DataService {

    private User max;
    private MetaData metaData;
    private static DataService instance = new DataService();

    public static DataService getInstance() {

        return instance;
    }

    private DataService() {

    }

    //request all the foodtrucks
    public void DownloadUserData(Context context, final DetailsMap.DownloadedUserData listener) {

        final JsonArrayRequest getUsers = new JsonArrayRequest(Request.Method.GET, UserConstants.getUsersUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                try {
                    JSONArray users = response;
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject user = users.getJSONObject(i);

                        JSONObject position = user.getJSONObject("position");
                        JSONObject coordinates = position.getJSONObject("coordinates");

                        max = new User(
                                user.getString("_id"),
                                user.getString("name"),
                                user.getString("status"),
                                user.getDouble("energyLevel"),
                                coordinates.getDouble("lat"),
                                coordinates.getDouble("long"));
                    }
                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }
                listener.success(true);
                Log.v("MAX", "DOWNLOAD DONE");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("API", "Err" + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(context).add(getUsers);
        Log.v("MAX", "RETURNING DATA");
    }
    public User GetUser() {
        return max;
    }

    public void DownloadMetaData(Context context, final MainActivity.DownloadedMetaData listener) {
        final JsonObjectRequest getMetaData = new JsonObjectRequest(Request.Method.GET, DataConstants.getMetaDataUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response.toString());
                try {
                    JSONObject metaDataResponse = response;

                    metaData = new MetaData(
                            metaDataResponse.getString("_id"),
                            metaDataResponse.getBoolean("forceUpdate"),
                            metaDataResponse.getString("downloadLink"),
                            metaDataResponse.getInt("version"));

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
        Volley.newRequestQueue(context).add(getMetaData);
    }
    public MetaData GetMetaData() {return metaData;}

    public void SendGpsLocation() {

    }
}
