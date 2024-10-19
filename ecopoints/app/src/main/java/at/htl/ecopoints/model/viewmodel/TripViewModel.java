package at.htl.ecopoints.model.viewmodel;

import java.util.concurrent.ConcurrentHashMap;

import at.htl.ecopoints.model.CarData;
import at.htl.ecopoints.model.Map;
import at.htl.ecopoints.model.Trip;
import at.htl.ecopoints.model.BtDevice;

public class TripViewModel {

    public Map map = new Map();
    public Trip trip = new Trip();
    public CarData carData = new CarData();
    public BtDevice selectedDevice = null;
    public volatile Boolean isConnected = false;
    public volatile String connectionStateString = "Not Connected";
    public boolean showDeviceSelectionDialog = false;
    public boolean showTestCommandDialog = false;
    public ConcurrentHashMap<String,String> obdTestCommandResults = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,String> commandResults = new ConcurrentHashMap<>();
}
