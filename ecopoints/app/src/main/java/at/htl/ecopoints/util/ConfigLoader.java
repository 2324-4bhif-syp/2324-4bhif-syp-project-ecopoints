package at.htl.ecopoints.util;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigLoader {
    private static final Properties properties = new Properties();

    public static void init(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("application.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("backend.baseurl");
    }
}
