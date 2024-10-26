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
    public boolean showCannotStartTripDialog = false;

    // Refers to connecting to the device and setting up obd
    public boolean isSetupFinished = false;
    // To show the current step of the ELM Setup process, while not showing up when the connection over bluetooth is not established
    public boolean hasELMSetupAndCheckingForAvailableCommandsProcessStarted = false;
    //Current Step the ELM Setup process is on, will be shown in the UI when connecting to the elm device
    public String elmSetupCurrentStep = "";
}
