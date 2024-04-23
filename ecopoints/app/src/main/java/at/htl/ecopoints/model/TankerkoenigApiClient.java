package at.htl.ecopoints.model;

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

public class TankerkoenigApiClient{

    private static final String API_KEY = "5b14766e-b489-5782-49b0-d376c5c04c4b";
    private static final String BASE_URL = "https://creativecommons.tankerkoenig.de/json/detail.php?id=005056ba-7cb6-1ed2-bceb-90e59ad2cd35&apikey=";

    public String callApi() {
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
                Log.e("TankerkoenigApiClient", "HTTP Error: " + responseCode);
            }

            con.disconnect();
        } catch (IOException e) {
            Log.e("TankerkoenigApiClient", "Error fetching data from Tankerkönig API", e);
        }

        return informationString.toString();
    }

    public GasData getApiData() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonData = mapper.readValue(callApi(), new TypeReference<Map<String, Object>>() {});
        Map<String, Object> stationData = (Map<String, Object>) jsonData.get("station");
        double diesel = (double) stationData.get("diesel");
        double e5 = (double) stationData.get("e5");

        GasData gasData = new GasData();
        gasData.setDiesel(diesel);
        gasData.setE5(e5);

        return gasData;
    }



}
