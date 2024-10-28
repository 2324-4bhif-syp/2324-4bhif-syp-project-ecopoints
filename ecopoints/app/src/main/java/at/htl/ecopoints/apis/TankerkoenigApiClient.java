package at.htl.ecopoints.apis;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import at.htl.ecopoints.model.GasData;

public class TankerkoenigApiClient {

    private static final String API_KEY = "5b14766e-b489-5782-49b0-d376c5c04c4b";
    private static final String BASE_URL = "https://creativecommons.tankerkoenig.de/json/detail.php?id=005056ba-7cb6-1ed2-bceb-90e59ad2cd35&apikey=";

    // Methode zum Starten der asynchronen Anfrage
    public void getApiData(ApiCallback callback) {
        new FetchDataTask(callback).execute();
    }

    // AsyncTask für den Hintergrundabruf der API-Daten
    private static class FetchDataTask extends AsyncTask<Void, Void, GasData> {
        private final ApiCallback callback;
        private String errorMessage = null;

        public FetchDataTask(ApiCallback callback) {
            this.callback = callback;
        }

        @Override
        protected GasData doInBackground(Void... voids) {
            StringBuilder informationString = new StringBuilder();
            try {
                URL url = new URL(BASE_URL + API_KEY);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(10000);

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(con.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        informationString.append(line);
                    }

                    reader.close();
                } else {
                    errorMessage = "HTTP Error: " + responseCode;
                    Log.e("TankerkoenigApiClient", errorMessage);
                    return null;
                }

                con.disconnect();
            } catch (IOException e) {
                errorMessage = "Error fetching data from Tankerkönig API: " + e.getMessage();
                Log.e("TankerkoenigApiClient", errorMessage, e);
                return null;
            }

            return parseResponse(informationString.toString());
        }

        // Verarbeitung der JSON-Antwort
        private GasData parseResponse(String response) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> jsonData = mapper.readValue(response, new TypeReference<Map<String, Object>>() {});
                Map<String, Object> stationData = (Map<String, Object>) jsonData.get("station");
                double diesel = stationData != null && stationData.get("diesel") != null ? (double) stationData.get("diesel") : 0.0;
                double e5 = stationData != null && stationData.get("e5") != null ? (double) stationData.get("e5") : 0.0;

                GasData gasData = new GasData();
                gasData.setDiesel(diesel);
                gasData.setE5(e5);
                return gasData;
            } catch (Exception e) {
                errorMessage = "Error parsing API response: " + e.getMessage();
                Log.e("TankerkoenigApiClient", errorMessage, e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(GasData gasData) {
            if (errorMessage != null) {
                callback.onError(errorMessage);
            } else {
                callback.onSuccess(gasData);
            }
        }
    }
}
