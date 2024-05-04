package at.htl.ecopoints;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import javax.inject.Inject;

import at.htl.ecopoints.ui.layout.HomeView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends ComponentActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Inject
    HomeView homeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        homeView.compose(this);
    }
}
