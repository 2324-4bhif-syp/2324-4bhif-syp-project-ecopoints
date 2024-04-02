package at.htl.ecopoints.model;

import java.io.InputStream;
import java.io.OutputStream;

import at.htl.ecopoints.model.reading.BtDevice;

public class BtConnectionInfo {
    private static final String TAG = BtConnectionInfo.class.getSimpleName();
    public BtDevice selectedDevice = null;
    public volatile Boolean isConnected = false;
    public volatile String connectionStateString = "Not Connected";
    public InputStream inputStream = null;
    public OutputStream outputStream = null;
    public boolean showDeviceSelectionDialog = false;

    public BtConnectionInfo() {

    }

}
