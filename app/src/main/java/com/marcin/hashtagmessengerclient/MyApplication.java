package com.marcin.hashtagmessengerclient;
/**************************************************************
 * Class used to get application context from non activity
 * classes e.g. when using navigation controller the intent
 * is invoked from from navigation controller.
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}