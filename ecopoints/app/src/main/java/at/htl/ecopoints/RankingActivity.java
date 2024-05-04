package at.htl.ecopoints;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.ComponentActivity;

import javax.inject.Inject;

import at.htl.ecopoints.ui.layout.RankingView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RankingActivity extends ComponentActivity {

    private static final String TAG = RankingActivity.class.getSimpleName();

    @Inject
    RankingView rankingView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        rankingView.compose(this);
    }
}
