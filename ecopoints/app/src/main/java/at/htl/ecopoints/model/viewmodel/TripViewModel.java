package at.htl.ecopoints.model.viewmodel;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import at.htl.ecopoints.model.Map;
import at.htl.ecopoints.model.BtDevice;

public class TripViewModel {

    public Map map = new Map();
    public UUID tripId = null;
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
    public boolean tripActive = false;

    // Refers to connecting to the device and setting up obd
    public boolean isSetupFinished = true;
    // To show the current step of the ELM Setup process, while not showing up when the connection over bluetooth is not established
    public boolean hasELMSetupAndCheckingForAvailableCommandsProcessStarted = false;
    //Current Step the ELM Setup process is on, will be shown in the UI when connecting to the elm device
    public String elmSetupCurrentStep = "Not Initialized";
}
