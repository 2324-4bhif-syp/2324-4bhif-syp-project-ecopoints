/*
package at.htl.ecopoints.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import at.htl.ecopoints.databinding.ActivityProfileBinding
import at.htl.ecopoints.databinding.UserViewRankingBinding
import at.htl.ecopoints.navigation.BottomNavBar
import at.htl.ecopoints.ui.theme.EcoPointsTheme
import at.htl.ecopoints.R

class RankingUserActivity : ComponentActivity() {

    private lateinit var binding: UserViewRankingBinding
    private lateinit var returnBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EcoPointsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    binding = UserViewRankingBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    returnBtn = findViewById(R.id.btn_return)

                    returnBtn.setOnClickListener {
                        onBackPressed()
                    }
                }
            }
        }
    }
}
*/