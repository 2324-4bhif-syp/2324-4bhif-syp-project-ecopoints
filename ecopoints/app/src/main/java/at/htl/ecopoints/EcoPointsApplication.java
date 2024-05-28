package at.htl.ecopoints;

import android.app.Application;
import android.util.Log;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class EcoPointsApplication extends Application {
    private static final String TAG = EcoPointsApplication.class.getSimpleName();

    public EcoPointsApplication() {
        Log.i(TAG, "EcoPoints");
    }
}