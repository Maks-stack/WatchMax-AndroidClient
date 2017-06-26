package com.example.maks.maxwatchapp.constants;

import com.example.maks.maxwatchapp.BuildConfig;

/**
 * Created by Maks on 24/06/17.
 */

public class UserConstants {
    public final static String getUsersUrl = BaseUrl.getInstance().getBaseUrl() + "/api/v1/users";
    public final static String putGpsLocation = BaseUrl.getInstance().getBaseUrl() + "/api/v1/users/position";
}
