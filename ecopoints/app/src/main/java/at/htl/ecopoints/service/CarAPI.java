package at.htl.ecopoints.service;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CarAPI {

    public void callApi() {

        Thread call = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("https://cis-automotive.p.rapidapi.com/getBrands")
                        .get()
                        .addHeader("X-RapidAPI-Key", "ec7a6c860fmsh730b81bf2ef94aap196d20jsncaae51b0d377")
                        .addHeader("X-RapidAPI-Host", "cis-automotive.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();

                Log.i("CarAPI", "Response: " + response.body().string());
            }
            catch (Exception e) {
                Log.e("CarAPI", "Error fetching data from Car API", e);
            }
        });

        call.start();

    }
}
