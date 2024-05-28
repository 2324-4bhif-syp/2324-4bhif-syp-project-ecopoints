package at.htl.ecopoints;

import android.os.Bundle;

import javax.inject.Inject;

import androidx.activity.ComponentActivity;
import androidx.compose.ui.platform.ComposeView;
import at.htl.leonding.MainViewRenderer;import dagger.hilt.android.AndroidEntryPoint;

/** Our main activity implemented in java.
 * We separate the application logic from the view.
 * The View is implemented in a separate file (separation of concerns).
 * We use Kotlin for the Jetpack Compose views.
 */
@AndroidEntryPoint
public class MainActivity extends ComponentActivity {
    @Inject
    MainViewRenderer mainViewRenderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var view = new ComposeView(this);
        mainViewRenderer.setContent(view);
        setContentView(view);
    }
}
