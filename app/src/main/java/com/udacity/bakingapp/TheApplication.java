package com.udacity.bakingapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class TheApplication extends Application {

    // Code for using LeakCanary has been commented out in this file and StepListFragment.java
    // private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            Log.i("Application Class ", "using LeakCanary");
            return;
        }
        refWatcher = LeakCanary.install(this);
        */
    }

    /*
    public static RefWatcher getRefWatcher(Context context) {
        TheApplication application = (TheApplication) context.getApplicationContext();
        return application.refWatcher;
    }
    */

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
