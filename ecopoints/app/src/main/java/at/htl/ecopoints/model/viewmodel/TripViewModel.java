package at.htl.ecopoints.model.viewmodel;

import java.util.concurrent.ConcurrentHashMap;

import at.htl.ecopoints.model.Map;
import at.htl.ecopoints.model.BtDevice;

public class TripViewModel {

    public Map map = new Map();
//    public Trip trip = new Trip();
//    public CarData carData = new CarData();
    public BtDevice selectedDevice = null;
    public volatile Boolean isConnected = false;
    public volatile String connectionStateString = "Not Connected";
    public boolean showDeviceSelectionDialog = false;
    public boolean showTestCommandDialog = false;
    public ConcurrentHashMap<String,String> availablePIDSs = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,String> availableCommands = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String,String> carData = new ConcurrentHashMap<>();
}
