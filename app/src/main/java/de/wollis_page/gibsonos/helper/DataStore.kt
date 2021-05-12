package de.wollis_page.gibsonos.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.exception.MessageException
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class DataStore(url: String, token: String?) {
    private val params: HashMap<String, String> = HashMap()
    private val seperator = "/"
    private val url: String
    private val token: String
    private val client: OkHttpClient = OkHttpClient()

    private var cacheTTL = (2 * 60 * 1000).toLong()
    private var timeout = 20000
    private var module: String? = null
    private var task: String? = null
    private var action: String? = null

    init {
        if (token == null) {
            throw TaskException("Account token doesn't exists!", R.string.account_error_no_token)
        }

        this.token = token

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
        } catch (exception: UnsupportedEncodingException) {
            exception.printStackTrace()
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

    private fun getParams(): RequestBody {
        if (this.params.size == 0) {
            return "".toRequestBody("application/json; charset=utf-8".toMediaType())
        }

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)

        for (key in this.params.keys) {
            Log.d(Config.LOG_TAG, "Add param '" + key + "' with value '" + this.params[key].toString() + "'")
            builder.addFormDataPart(key, this.params[key]!!)
        }

        return builder.build()
    }

    @Throws(ResponseException::class)
    fun loadJson(): JSONObject {
        try {
            return this.checkError(this.execute())
        } catch (exception: MessageException) {
            throw exception
        } catch (exception: Exception) {
            exception.printStackTrace()

            throw ResponseException(exception.message ?: "Request has errors!", "", 0)
        }
    }

    fun loadBitmap(): Bitmap {
        try {
            return BitmapFactory.decodeStream(this.getBody(this.execute()).byteStream())
        } catch (exception: ResponseException) {
            throw exception
        } catch (exception: Exception) {
            throw ResponseException(exception.message ?: "Request has errors!", "", 0)
        }
    }

    private fun execute(): Response {
        val requestUrl = this.getUrl()
        Log.i(Config.LOG_TAG, requestUrl)
        val requestBuilder = Request.Builder()
            .url(requestUrl)
            .header("X-Requested-With", "XMLHttpRequest")
            .post(this.getParams())

        if (this.token.isNotEmpty()) {
            Log.i(Config.LOG_TAG, "X-Device-Token: " + this.token)
            requestBuilder.header("X-Device-Token", this.token)
        }

        val response = this.client.newCall(requestBuilder.build()).execute()
        Log.i(Config.LOG_TAG, "Response code: " + response.code)

        if (!response.isSuccessful) {
            this.checkError(response)
            throw ResponseException("Request error!", "", response.code)
        }

        return response
    }

    private fun getBody(response: Response): ResponseBody {
        return response.body ?: throw ResponseException("Body is empty!", "", response.code)
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

    private fun checkError(response: Response): JSONObject {
        val jsonResponse = JSONObject(this.getBody(response).string())
        Log.i(Config.LOG_TAG, "Response body: $jsonResponse")

        if (
            (jsonResponse.has("failure") && jsonResponse.getBoolean("failure")) ||
            (!jsonResponse.has("success") || !jsonResponse.getBoolean("success"))
        ) {
            val message = if (jsonResponse.has("data")) {
                val data = jsonResponse.getJSONObject("data")

                when {
                    data.has("message") -> data.getString("message")
                    data.has("msg") -> data.getString("msg")
                    else -> "Response error!"
                }
            } else "Response error!"

            throw ResponseException(message, jsonResponse.toString(2), response.code)
        }

        return jsonResponse
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