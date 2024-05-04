package at.htl.ecopoints.model;

public class ProfileInfo {
    public User currentUser = new User();

    public ProfileInfo() {
        RankingInfo rankingInfo = new RankingInfo();
        this.currentUser = rankingInfo.users.get(0);
    }
}
