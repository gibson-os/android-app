package de.wollis_page.gibsonos.task

import android.content.Context
import android.util.Log
import com.squareup.moshi.Moshi
import de.wollis_page.gibsonos.dto.Desktop
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account

object DesktopTask {
    @JvmStatic
    fun index(context: Context?, account: Account): Desktop? {
        Log.d(Config.LOG_TAG, "desktop index start")
        val dataStore = DataStore(context!!, account.url, account.token!!)
        Log.d(Config.LOG_TAG, "desktop index")
        dataStore.setRoute("core", "desktop", "index")
        Log.d(Config.LOG_TAG, "desktop index jo")

        try {
            val response = dataStore.data
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Desktop::class.java)
            return jsonAdapter.fromJson(response?.getJSONObject("data").toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}