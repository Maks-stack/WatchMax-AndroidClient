package com.example.maks.maxwatchapp.data;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.maks.maxwatchapp.activities.DetailsMap;
import com.example.maks.maxwatchapp.activities.MainActivity;
import com.example.maks.maxwatchapp.constants.DataConstants;
import com.example.maks.maxwatchapp.constants.FireBaseConstants;
import com.example.maks.maxwatchapp.constants.UserConstants;
import com.example.maks.maxwatchapp.models.Message;
import com.example.maks.maxwatchapp.models.MetaData;
import com.example.maks.maxwatchapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Maks on 26/06/17.
 */

public class DataService {

    private User max;
    private ArrayList<Message> messages = new ArrayList<Message>();
    private MetaData metaData;
    private static DataService instance = new DataService();

    private ProgressDialog progressDialog;

    private int userRequestsPending = 0;

    public static DataService getInstance() {

        return instance;
    }

    private DataService() {

    }

    //request all Users
    public void DownloadUserData(final Context context, final DetailsMap.DownloadedUserData listener) {
        ShowProgressSpinner(context);

        messages = new ArrayList<Message>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        final JsonArrayRequest getMessages = new JsonArrayRequest(Request.Method.GET, UserConstants.getMessagesUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());
                try {
                    JSONArray messagesArray = response;
                    for (int i = 0; i < messagesArray.length(); i++) {

                        Message newMessage = new Message (
                            messagesArray.getJSONObject(i).getString("_id"),
                            messagesArray.getJSONObject(i).getString("text")
                        );

                        messages.add(i, newMessage);
                    }
                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }
                userRequestsPending --;
                Log.v("MAX", "DOWNLOAD DONE");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HideProgressSpinner();
                ShowAlertDialog(context, error.toString());
                Log.v("API", "Err" + error.getLocalizedMessage());
            }
        });
        final JsonArrayRequest getUsers = new JsonArrayRequest(Request.Method.GET, UserConstants.usersUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                HideProgressSpinner();
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
                                coordinates.getDouble("long"),
                                messages
                        );
                    }
                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }
                userRequestsPending --;
                Log.v("MAX", "DOWNLOAD DONE");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HideProgressSpinner();
                ShowAlertDialog(context, error.toString());
                Log.v("API", "Err" + error.getLocalizedMessage());
            }
        });

        requestQueue.add(getMessages);
        userRequestsPending ++;
        requestQueue.add(getUsers);
        userRequestsPending ++;

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(userRequestsPending == 0) {
                    listener.success(true);
                }
            }
        });

        Log.v("MAX", "RETURNING DATA");
    }
    public User GetUser() {
        return max;
    }

    public void DownloadMetaData(final Context context, final MainActivity.DownloadedMetaData listener) {
        ShowProgressSpinner(context);
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
                HideProgressSpinner();
                listener.success(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowAlertDialog(context, error.toString());
                HideProgressSpinner();
                Log.v("API", "Err" + error.getLocalizedMessage());
            }
        });
        getMetaData.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(getMetaData);
    }
    public MetaData GetMetaData() { return metaData; }

    public void SendGpsLocation(Context context, double longitude, double latitude) {
        Log.v("MAX", "Latitude: " + latitude + " Longitude: " + longitude);

        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject coordinates = new JSONObject();
            coordinates.put("lat",  latitude);
            coordinates.put("long", longitude);

            jsonBody.put("coordinates", coordinates);

            final String requestBodyString = jsonBody.toString();

            JsonObjectRequest sendGpsLocation = new JsonObjectRequest(Request.Method.PUT, UserConstants.putGpsLocation, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String message = response.getString("message");
                        Log.i("MAX", message);
                    }catch (JSONException e){
                        Log.v("MAX", "Exception: " + e.getLocalizedMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBodyString == null ? null : requestBodyString.getBytes("utf-8");
                    } catch(UnsupportedEncodingException uee) {
                        VolleyLog.wtf("unsupported encoding", requestBodyString, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if(response.statusCode == 200) {
                        Log.v("MAX", "SUCCESS GPS" + response);
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            Volley.newRequestQueue(context).add(sendGpsLocation);
        } catch(JSONException e) {
            Log.v("MAX", e.toString());
        }
    }

    public void SendMessage(Context context, String message) {

        try {
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonMessage = new JSONObject();

            jsonMessage.put("text",  message);
            jsonBody.put("message", jsonMessage);

            final String requestBodyString = jsonBody.toString();

            JsonObjectRequest sendMessage = new JsonObjectRequest(Request.Method.POST, UserConstants.postMessageUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String message = response.getString("message");
                        Log.i("MAX Line 243 ", message);
                    }catch (JSONException e){
                        Log.v("MAX", "Exception: " + e.getLocalizedMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBodyString == null ? null : requestBodyString.getBytes("utf-8");
                    } catch(UnsupportedEncodingException uee) {
                        VolleyLog.wtf("unsupported encoding", requestBodyString, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if(response.statusCode == 200) {
                        Log.v("MAX", "SUCCESS GPS" + response);
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            Volley.newRequestQueue(context).add(sendMessage);
        } catch(JSONException e) {
            Log.v("MAX", "line 279 " + e.toString());
        }
    }

    public void SendFireBaseToken(Context context, String token){
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);

            final String requestBodyString = jsonBody.toString();

            StringRequest sendToken = new StringRequest(Request.Method.POST, FireBaseConstants.tokenUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.v("MAX Line 292 ", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("MAX", "VOLLEY ERROR: " + error);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBodyString == null ? null : requestBodyString.getBytes("utf-8");
                    } catch(UnsupportedEncodingException uee) {
                        VolleyLog.wtf("unsupported encoding", requestBodyString, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if(response.statusCode == 200) {
                        Log.v("MAX", "SUCCESS GPS" + response);
                    }
                    return super.parseNetworkResponse(response);
                }
            };
            Volley.newRequestQueue(context).add(sendToken);
        }catch(JSONException e) {
            Log.v("MAX", "Error Sending Token: " + e.toString());
        }
    }

    private void ShowProgressSpinner(Context context) {

        HideProgressSpinner();
        progressDialog= new ProgressDialog(context);
        progressDialog.setTitle("Lade Daten runter!");
        progressDialog.setMessage("Geht ganz schnell.");
        progressDialog.show();
    }

    private void HideProgressSpinner() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void ShowAlertDialog(Context context, String text) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(text);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Try Again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
