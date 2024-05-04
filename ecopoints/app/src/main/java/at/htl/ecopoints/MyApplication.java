package at.htl.ecopoints;

import android.app.Application;
import android.util.Log;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    public MyApplication() {
        Log.i(TAG, "EcoPoints");
    }
}