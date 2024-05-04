package at.htl.ecopoints;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import javax.inject.Inject;

import at.htl.ecopoints.ui.layout.ProfileView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileActivity extends ComponentActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Inject
    ProfileView profileView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        profileView.compose(this);
    }
}
