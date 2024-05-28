package at.htl.leonding

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import at.htl.ecopoints.model.Store
import at.htl.ecopoints.model.UIState
import at.htl.leonding.feature.tabscreen.TabView
import javax.inject.Inject
import javax.inject.Singleton

/** Render the contents of the Main Compose View.
 * We also watch orientation changes and forward those to the Model.
 */
@Singleton
class MainViewRenderer @Inject constructor() {
    @Inject
    lateinit var tabScreenView: TabView
    @Inject
    lateinit var store: Store

    fun setContent(view: ComposeView) {
        view.setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
                val configuration = LocalConfiguration.current
                LaunchedEffect(configuration) {
                    orientation = configuration.orientation
                    val currentOrientation = orientationFromConfiguration(configuration)
                    store.apply { it.uiState.orientation = currentOrientation }
                }
                tabScreenView.TabViewLayout()
            }
        }
    }
    private fun orientationFromConfiguration(configuration: Configuration): UIState.Orientation {
        return when(configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> UIState.Orientation.portrait
            Configuration.ORIENTATION_LANDSCAPE -> UIState.Orientation.landscape
            else -> UIState.Orientation.undefined
        }
    }
}


