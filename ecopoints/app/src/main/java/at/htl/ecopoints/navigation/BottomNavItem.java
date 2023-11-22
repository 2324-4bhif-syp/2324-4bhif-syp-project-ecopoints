package at.htl.ecopoints.navigation;

import at.htl.ecopoints.R;

public enum BottomNavItem {
    HOME("Home", R.drawable.ic_home, "home_route"),
    RANKING("Ranking", R.drawable.ic_ranking, "ranking_route"),
    PROFILE("Profile", R.drawable.ic_profile, "profile_route");

    private final String title;
    private final int icon;
    private final String route;

    BottomNavItem(String title, int icon, String route) {
        this.title = title;
        this.icon = icon;
        this.route = route;
    }

    public String getTitle() {
        return title;
    }

    public int getIcon() {
        return icon;
    }

    public String getRoute() {
        return route;
    }
}
