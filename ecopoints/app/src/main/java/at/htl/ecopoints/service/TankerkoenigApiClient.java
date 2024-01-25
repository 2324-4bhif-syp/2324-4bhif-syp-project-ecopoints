package at.htl.ecopoints.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TankerkoenigApiClient{

    private static final String API_KEY = "5b14766e-b489-5782-49b0-d376c5c04c4b";
    private static final String BASE_URL = "https://creativecommons.tankerkoenig.de/json/detail.php?id=005056ba-7cb6-1ed2-bceb-90e59ad2cd35&apikey=";

    public interface TankerkoenigApiCallback {
        void onApiResult(String result);
    }

    public void runApi(TankerkoenigApiCallback callback) {
        new ApiTask(callback).execute(BASE_URL + API_KEY);
    }

    private static class ApiTask extends AsyncTask<String, Void, String> {
        private final TankerkoenigApiCallback callback;

        public ApiTask(TankerkoenigApiCallback callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder informationString = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    informationString.append(line);
                }

                reader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                Log.e("TankerkoenigApiClient", "Error fetching data from Tankerkönig API", e);
            }

            return informationString.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (callback != null) {
                callback.onApiResult(result);
            }
        }
    }
}
