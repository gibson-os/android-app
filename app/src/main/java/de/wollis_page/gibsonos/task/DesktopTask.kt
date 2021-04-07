package de.wollis_page.gibsonos.task

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.dto.Desktop
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account

object DesktopTask {
    fun index(context: Context?, account: Account): Desktop? {
        val dataStore = DataStore(context!!, account.url, account.token!!)
        dataStore.setRoute("core", "desktop", "index")

        try {
            val response = dataStore.getData()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Desktop::class.java)
            return jsonAdapter.fromJson(response?.getJSONObject("data").toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}