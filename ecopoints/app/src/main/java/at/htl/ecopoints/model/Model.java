package at.htl.ecopoints.model;

import at.htl.ecopoints.model.viewmodel.TripViewModel;

public class Model {

    public TripViewModel tripViewModel = new TripViewModel();
    public RankingInfo rankingInfo = new RankingInfo();
    public ProfileInfo profileInfo = new ProfileInfo();
    public HomeInfo homeInfo = new HomeInfo();
    public UIState uiState = new UIState();
    public Model() {
    }
}