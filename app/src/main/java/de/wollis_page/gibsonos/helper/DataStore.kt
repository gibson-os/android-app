package de.wollis_page.gibsonos.helper

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import de.wollis_page.gibsonos.exception.ResponseException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class DataStore(private val context: Context, url: String, private val token: String) {
    private val params: HashMap<String, String> = HashMap()
    private val seperator = "/"
    private val url: String
    private val client: OkHttpClient = OkHttpClient()

    private var cacheTTL = (2 * 60 * 1000).toLong()
    private var timeout = 20000
    private var module: String? = null
    private var task: String? = null
    private var action: String? = null

    init {
        var cleanUrl = url

        if (!cleanUrl.endsWith("/")) {
            cleanUrl += '/'
        }

        if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
            cleanUrl = "http://$cleanUrl"
        }

        this.url = cleanUrl
    }

    fun addParamEncoded(key: String, value: String) {
        var cleanValue = value.trim { it <= ' ' }

        try {
            cleanValue = URLEncoder.encode(value, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        this.params[key] = cleanValue
    }

    fun addParam(key: String, value: String) {
        this.params[key] = value.trim { it <= ' ' }
    }

    fun addParam(key: String, value: Float) {
        this.params[key] = value.toString()
    }

    fun addParam(key: String, value: Double) {
        this.params[key] = java.lang.Double.valueOf(value).toString()
    }

    fun addParam(key: String, value: Int) {
        this.params[key] = Integer.valueOf(value).toString()
    }

    fun addParam(key: String, value: Boolean) {
        this.params[key] = java.lang.Boolean.valueOf(value).toString()
    }

    fun clearParams() {
        this.params.clear()
    }

    fun removeParam(key: String) {
        this.params.remove(key)
    }

    fun getData(): JSONObject? {
        return if (isOnline()) {
            execute()
        } else {
            null
        }
    }

    private fun getParams(): RequestBody {
        if (this.params.size == 0) {
            return "".toRequestBody("application/json; charset=utf-8".toMediaType())
        }

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        for (key in this.params.keys) {
            builder.addFormDataPart(key, this.params[key]!!)
        }

        return builder.build()
    }

    @Throws(ResponseException::class)
    private fun execute(): JSONObject? {
        val requestUrl = getUrl()
        Log.i(Config.LOG_TAG, requestUrl)
        val requestBuilder = Request.Builder()
            .url(requestUrl)
            .header("X-Requested-With", "XMLHttpRequest")
            .post(getParams())

        if (this.token.isNotEmpty()) {
            Log.i(Config.LOG_TAG, "X-Device-Token: " + this.token)
            requestBuilder.header("X-Device-Token", this.token)
        }

        try {
            val response = this.client.newCall(requestBuilder.build()).execute()
            Log.i(Config.LOG_TAG, "Response code: " + response.code)
            val jsonResponse = JSONObject(response.body!!.string())
            Log.i(Config.LOG_TAG, "Response body: $jsonResponse")

            if (jsonResponse.has("failure") && jsonResponse.getBoolean("failure") ||
                    jsonResponse.has("success") && !jsonResponse.getBoolean("success")) {
                val message = if (jsonResponse.has("message")) jsonResponse.getString("message") else "Fehler bei der Abfrage!"
                throw ResponseException(message, jsonResponse, response.code)
            }

            return jsonResponse
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getUrl(): String {
        var buildedUrl = this.url

        if (!this.module.isNullOrBlank()) {
            buildedUrl += this.module + this.seperator
        }

        if (!this.task.isNullOrBlank()) {
            buildedUrl += this.task + this.seperator
        }

        if (!this.action.isNullOrBlank()) {
            buildedUrl += this.action
        }

        return buildedUrl
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun setRoute(module: String?, task: String?, action: String?) {
        this.module = module
        this.task = task
        this.action = action
    }

    fun setTimeout(timeout: Int) {
        this.timeout = timeout
    }

    fun setCacheTimeToLive(ttl: Int) {
        this.cacheTTL = ttl.toLong()
    }
}