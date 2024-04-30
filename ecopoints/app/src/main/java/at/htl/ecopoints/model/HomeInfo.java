package at.htl.ecopoints.model;

import java.util.Date;
import java.util.List;

import at.htl.ecopoints.db.DBHelper;

public class HomeInfo {

    public HomeInfo() {
    }

    public boolean showDetailedLastRidesPopup = false;

    public boolean showDialog = false;

    public Date selectedTripDate = new Date();

}
