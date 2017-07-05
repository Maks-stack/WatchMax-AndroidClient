package com.example.maks.maxwatchapp.constants;

import com.example.maks.maxwatchapp.helpers.Helpers;

/**
 * Created by Maks on 26/06/17.
 */

public class BaseUrl {

    public BaseUrl() {
    }

    public static BaseUrl getInstance() {
        return instance;
    }

    private static BaseUrl instance = new BaseUrl();

    public static String getBaseUrl() {
        return Helpers.isEmulator() ? "http://10.0.2.2:8005" : "http://max-watch-api.makshub.com";
    }
}
