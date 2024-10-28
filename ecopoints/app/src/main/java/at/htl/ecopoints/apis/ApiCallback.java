package at.htl.ecopoints.apis;

import at.htl.ecopoints.model.GasData;

// Callback-Interface für asynchrone Ergebnisse
public interface ApiCallback {
    void onSuccess(GasData gasData);

    void onError(String errorMessage);
}
