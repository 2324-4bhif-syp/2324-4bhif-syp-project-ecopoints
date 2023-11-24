package at.htl.ecopoints.backendService

import android.app.DownloadManager
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response


abstract class Service {
    private final val path: String = "http://132.145.237.245/api/"

    fun create(obj: Any, endPoint: String): Response{
        val json = Gson().toJson(obj)

        val request = Request.Builder()
            .url("$path$endPoint")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), json))
            .build()

        return OkHttpClient().newCall(request).execute()
    }

    fun update(obj: Any, endPoint: String, id: Long): Response{
        val json = Gson().toJson(obj)

        val request = Request.Builder()
            .url("$path$endPoint/$id")
            .put(RequestBody.create("application/json".toMediaTypeOrNull(), json))
            .build()

        return OkHttpClient().newCall(request).execute()
    }

    fun delete(endPoint: String, id: Long): Response{
        val request = Request.Builder()
            .url("$path$endPoint/$id")
            .delete()
            .build()

        return OkHttpClient().newCall(request).execute()
    }

    fun getAll(endPoint: String): Response{
        val request = Request.Builder()
            .url("$path$endPoint")
            .get()
            .build()

        return OkHttpClient().newCall(request).execute()
    }

    fun getById(endPoint: String, id: Long): Response{
        val request = Request.Builder()
            .url("$path$endPoint/$id")
            .get()
            .build()

        return OkHttpClient().newCall(request).execute()
    }
}