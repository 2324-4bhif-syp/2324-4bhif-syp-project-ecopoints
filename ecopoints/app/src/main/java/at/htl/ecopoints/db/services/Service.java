package at.htl.ecopoints.db.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import java.io.IOException;
import okhttp3.Response;

import java.util.List;

public abstract class Service {
    private final String path = "http://132.145.237.245/api/";

    public Response create(Object obj, String endPoint) throws IOException {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(obj);

            Request request = new Request.Builder()
                    .url(path + endPoint)
                    .post(RequestBody.create(MediaType.get("application/json"), json))
                    .build();

            return new OkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            // Log or handle the exception
            e.printStackTrace();
            throw e;
        }
    }

    public Response update(Object obj, String endPoint, String id) throws IOException {
        String json = new Gson().toJson(obj);

        Request request = new Request.Builder()
                .url(path + endPoint + "/" + id)
                .put(RequestBody.create(MediaType.get("application/json"), json))
                .build();

        return new OkHttpClient().newCall(request).execute();
    }

    public Response delete(String endPoint, String id) throws IOException {
        Request request = new Request.Builder()
                .url(path + endPoint + "/" + id)
                .delete()
                .build();

        return new OkHttpClient().newCall(request).execute();
    }

    public <T> List<T> getAll(String endPoint) {
        Request request = new Request.Builder()
                .url(path + endPoint)
                .get()
                .build();

        try {
            Response response = new OkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                return new Gson().fromJson(json, new TypeToken<List<T>>() {}.getType());
            } else {
                System.out.println("Unsuccessful Response: " + response.message());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public <T> T getById(String endPoint, String id) {
        Request request = new Request.Builder()
                .url(path + endPoint + "/" + id)
                .get()
                .build();

        try {
            Response response = new OkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                return new Gson().fromJson(json, new TypeToken<T>() {}.getType());
            } else {
                System.out.println("Unsuccessful Response: " + response.message());
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    String getAccessPath() {
        return path;
    }
}
