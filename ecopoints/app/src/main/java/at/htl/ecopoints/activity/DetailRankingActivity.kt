package at.htl.ecopoints.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import at.htl.ecopoints.model.User
import at.htl.ecopoints.ui.theme.EcoPointsTheme


class DetailRankingActivity : AppCompatActivity() {

    private val user: User by lazy {
        intent?.getSerializableExtra(USER_ID) as User
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoPointsTheme {
                ProfileScreen(user)
            }
        }
    }


    companion object{
        private const val USER_ID = "user_id"
        fun newIntent(context: Context, user: User) =
            Intent(context, DetailRankingActivity::class.java).apply {
                putExtra(USER_ID, user)
            }
    }
}