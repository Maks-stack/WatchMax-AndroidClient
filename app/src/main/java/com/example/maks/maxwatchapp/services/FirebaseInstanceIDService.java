package com.example.maks.maxwatchapp.services;

import com.example.maks.maxwatchapp.data.DataService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Maks on 30/06/17.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        RegisterToken(token);
    }

    private void RegisterToken(String token) {
        DataService.getInstance().SendFireBaseToken(getBaseContext(), token);
    }
}
