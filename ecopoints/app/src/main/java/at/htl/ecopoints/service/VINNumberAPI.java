package at.htl.ecopoints.service;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class VINNumberAPI {
    private static final String BASE_URL = "https://vin-decoder19.p.rapidapi.com/vin_decoder_basic?vin=";
    private static final String API_KEY = "ec7a6c860fmsh730b81bf2ef94aap196d20jsncaae51b0d377";
    private static final String API_HOST = "vin-decoder19.p.rapidapi.com";

    public Response getCarFromVIN(String vinNumber) {
        Thread apiCall = new Thread(() -> {
            OkHttpClient client = new OkHttpClient();

            try {
                Request request = new Request.Builder()
                        .url(BASE_URL + vinNumber)
                        .get()
                        .addHeader("X-RapidAPI-Key", API_KEY)
                        .addHeader("X-RapidAPI-Host", API_HOST)
                        .build();

                Log.i("VINNumberAPI", "Request: " + client.newCall(request).execute().body().string());

                ObjectMapper mapper = new ObjectMapper();
                ResponseBody responseBody = client.newCall(request).execute().body();

                Map<String, Object> jsonData = mapper.readValue(responseBody.charStream(), new TypeReference<Map<String, Object>>() {});
                Map<String, Object> brandData = (Map<String, Object>) jsonData.get("Make");
                Map<String, Object> modelData = (Map<String, Object>) jsonData.get("Model");

                String brand = brandData.get("value").toString();
                String model = modelData.get("value").toString();

                Log.i("VINNumberAPI", "Brand: " + brand + ", Model: " + model);
            }
            catch (Exception e) {
                Log.e("VINNumberAPI", "Error fetching data from VINNumber API", e);
            }
        });

        apiCall.start();

        return null;
    }
}
