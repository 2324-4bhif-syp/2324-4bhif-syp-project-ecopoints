package at.htl.ecopoints.activity.ui.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import at.htl.ecopoints.R
import at.htl.ecopoints.activity.ui.theme.ui.theme.EcoPointsTheme
import at.htl.ecopoints.navigation.BottomNavBar
import com.google.android.material.composethemeadapter.MdcTheme
import androidx.compose.ui.viewinterop.AndroidView

class ProfileActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

    }

}
