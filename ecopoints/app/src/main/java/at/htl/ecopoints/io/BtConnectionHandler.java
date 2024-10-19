package at.htl.ecopoints.io;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.ecopoints.model.BtDevice;
import at.htl.ecopoints.model.Store;

@Singleton
public class BtConnectionHandler {
    private final String TAG = BtConnectionHandler.class.getSimpleName();
    private static final UUID RF_COMM_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public InputStream inputStream;
    public OutputStream outputStream;

    @Inject
    Store store;
    public Set<BluetoothDevice> pairedBtDevices;
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Inject
    public BtConnectionHandler() {

    }

    public void createConnection(BtDevice device) {

        if (pairedBtDevices == null) {
            getPairedDevices();
        }

        if (device == null) {
            store.next(i -> {
                        if (i != null) {
                            i.tripViewModel.connectionStateString = "No Device Selected";
                        }
                    }
            );
            return;
        }

        BluetoothDevice btDevice = pairedBtDevices.stream()
                .filter(i -> Objects.equals(i.getAddress(), device.getAddress()))
                .findFirst().orElse(null);

        if (btDevice != null) {
            Log.d(TAG, "Connecting to Bt-Device: " + btDevice.getAddress());
            store.next(i -> {
                        if (i != null) {
                            i.tripViewModel.connectionStateString = "Connecting...";
                        }
                    }
            );
            connect(btDevice).thenAccept(connected -> {
                if (connected) {
                    store.next(i -> {
                                if (i != null) {
                                    i.tripViewModel.isConnected = true;
                                    i.tripViewModel.connectionStateString = "Connected";
                                }
                            }
                    );
                } else {
                    store.next(i -> {
                                if (i != null) {
                                    i.tripViewModel.isConnected = false;
                                    i.tripViewModel.connectionStateString = "Could not connect";
                                }
                            }
                    );
                }
            });
        } else {
            store.next(i -> {
                        if (i != null) {
                            i.tripViewModel.isConnected = false;
                            i.tripViewModel.connectionStateString = "Device not found";
                        }
                    }
            );
        }
    }

    @SuppressLint("MissingPermission")
    private CompletableFuture<Boolean> connect(BluetoothDevice btDevice) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();


        executor.execute(() -> {
            try {
                @SuppressLint("MissingPermission") BluetoothSocket btSocket = btDevice.createRfcommSocketToServiceRecord(RF_COMM_UUID);
                btSocket.connect();
                if (btSocket.isConnected()) {
                    Log.d(TAG, "Connected to Bt-Device: " + btDevice.getName());

                    try {
                        inputStream = btSocket.getInputStream();
                        outputStream = btSocket.getOutputStream();
                    } catch (IOException e) {
                        Log.e(TAG, "Exception when retrieving input and output stream:" + e.getMessage());
                    }
                    store.next(i -> {
                        if (i != null) {
                            i.tripViewModel.isConnected = true;
                            i.tripViewModel.connectionStateString = "Connected";
                        }
                    });
                    future.complete(true);
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to connect to Bt-Device: " + btDevice.getName(), e);
                future.complete(false);
            }
        });

        return future;
    }

    @SuppressLint("MissingPermission")
    public Set<BtDevice> getPairedDevices() {
        try {
            pairedBtDevices = mBluetoothAdapter.getBondedDevices();
            return pairedBtDevices.stream().map(i -> new BtDevice(i.getName(), i.getAddress()))
                    .sorted(Comparator.comparing(BtDevice::getName))
                    .filter(i -> i.getName().toLowerCase().contains("obd") || i.getName().toLowerCase().contains("evo"))
                    .collect(Collectors.toCollection(LinkedHashSet::new));

        } catch (Exception exception) {
            Log.e(TAG, "Could not find Bt-Devices: " + exception.getMessage());
        }

        return Set.of(new BtDevice("No Device Found", ""));
    }
}
