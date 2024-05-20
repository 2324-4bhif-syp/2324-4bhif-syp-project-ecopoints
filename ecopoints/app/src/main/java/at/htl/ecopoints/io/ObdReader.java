package at.htl.ecopoints.io;

import android.util.Log;

import androidx.annotation.NonNull;

import com.github.eltonvs.obd.command.ObdResponse;
import com.github.eltonvs.obd.command.engine.RPMCommand;
import com.github.eltonvs.obd.connection.ObdDeviceConnection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import at.htl.ecopoints.model.Store;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;

@Singleton
public class ObdReader {
    private final String TAG = ObdReader.class.getSimpleName();

    private boolean testActive = false;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Inject
    Store store;

    @Inject
    public ObdReader() {

    }

    public void startReading(InputStream inputStream, OutputStream outputStream) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();


        //Setup
        singleThreadExecutor.execute(() -> {
            try {
                ObdDeviceConnection obdConnection = new ObdDeviceConnection(inputStream, outputStream);

            } catch (Exception e) {
                Log.e(TAG, "Error while setting up OBD connection", e);
            }
        });


        executor.scheduleAtFixedRate(() -> {
            try {
                ObdDeviceConnection obdConnection = new ObdDeviceConnection(inputStream, outputStream);
                Continuation<ObdResponse> test = new Continuation<ObdResponse>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return this.getContext();
                    }

                    @Override
                    public void resumeWith(Object o) {
                        Log.d(TAG, "Continuation resumeWith: " + o);
                    }
                };
                var res = obdConnection.run(new RPMCommand(), false, 0, 0, test);
                Log.d(TAG, "RPM: " + res);
            } catch (Exception e) {
                Log.e(TAG, "Error while setting up OBD connection", e);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

//    public void speedometerTest() {
//
//        if (testActive) {
//            testActive = false;
//            executor.shutdown();
//        } else {
//            testActive = true;
//            executor = Executors.newSingleThreadScheduledExecutor();
//            AtomicReference<Double> speed = new AtomicReference<>((double) 0);
//            executor.scheduleAtFixedRate(() -> {
//                Log.d(TAG, "Executed Read on: " + Thread.currentThread().getName());
//                if (speed.get() == 240) {
//                    speed.set((double) 0);
//                }
//                Log.d(TAG, "Speed: " + speed.get());
//                speed.set(speed.get() + 5);
//                store.next(i -> {
//                    i.tripViewModel.carData.setSpeed(speed.getAndSet(speed.get()));
//                });
//            }, 0, 50, TimeUnit.MILLISECONDS);
//        }
//    }
}
