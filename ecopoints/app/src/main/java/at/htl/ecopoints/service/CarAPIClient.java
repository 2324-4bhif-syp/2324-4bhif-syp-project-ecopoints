package at.htl.ecopoints.service;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CarAPIClient {

    private static final String BASE_URL = "https://api.api-ninjas.com/v1/cars?model=";
    private String API_KEY;

    public void getCars(Context context, String model) throws Exception {
        API_KEY = loadProperty(context);
        URL url = new URL(BASE_URL + model);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        InputStream responseStream = connection.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseStream);
        Log.i("CarAPIClient", root.toString());
        System.out.println(root.path("fact").asText());
    }

    private static String loadProperty(Context context){
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return app.metaData.getString("ninjas.car.api.key");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
