package at.htl.ecopoints.backendService

import at.htl.ecopoints.model.Trip
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response


abstract class Service {
    //private final val path: String = "http://132.145.237.245/api/"
    private final val path: String = "http://0.0.0.0:8080/api/"

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

    inline fun <reified T> getAll(endPoint: String): List<T>? {
        val request = Request.Builder()
            .url("$`access$path`$endPoint")
            .get()
            .build()

        return try {
            val response: Response = OkHttpClient().newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                Gson().fromJson(json, Array<T>::class.java).toList()
            } else {
                println("Unsuccessful Response: ${response.message}")
                null
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    inline fun <reified T> getById(endPoint: String, id: Long): T? {
        val request = Request.Builder()
            .url("$`access$path`$endPoint/$id")
            .get()
            .build()

        return try {
            val response: Response = OkHttpClient().newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                Gson().fromJson(json, T::class.java)
            } else {
                println("Unsuccessful Response: ${response.message}")
                null
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            null
        }
    }

    @PublishedApi
    internal val `access$path`: String
        get() = path
}