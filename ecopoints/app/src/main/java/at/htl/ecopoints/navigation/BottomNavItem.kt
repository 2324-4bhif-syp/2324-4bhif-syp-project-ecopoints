package at.htl.ecopoints.navigation

import at.htl.ecopoints.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String){

    class Home : BottomNavItem("Home", R.drawable.ic_home, "home_route")
    class Ranking : BottomNavItem("Ranking", R.drawable.ic_ranking, "ranking_route")
    class Profile : BottomNavItem("Profile", R.drawable.ic_profile, "profile_route")
}