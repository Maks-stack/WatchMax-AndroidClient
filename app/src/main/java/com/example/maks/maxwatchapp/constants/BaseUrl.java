package com.example.maks.maxwatchapp.constants;

import android.os.Build;

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
        return isEmulator() ? "http://10.0.2.2:8005" : "http://max-watch-api.makshub.com";
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
