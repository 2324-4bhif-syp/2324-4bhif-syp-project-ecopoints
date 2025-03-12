package at.htl.ecopoints.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.htl.ecopoints.model.dto.TripMetaData;

public class HomeInfo {

    public HomeInfo() {
    }

    public boolean showDetailedLastRidesPopup = false;

    public boolean showDialog = false;

    public Date selectedTripDate = new Date();

    public boolean showDialog2 = false;
    public Date selectedTripDate2 = new Date();

    public TripMetaData selectedTrip = new TripMetaData();

    public boolean showDetailedLastRidesPopup2 = false;

    public List<TripMetaData> trips = new ArrayList<>();
}
