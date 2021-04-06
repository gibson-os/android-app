package de.wollis_page.gibsonos.task

import android.content.Context
import android.os.Build
import android.util.Log
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account

object UserTask {
    @JvmStatic
    fun login(context: Context, url: String, username: String, password: String): Account? {
        val dataStore = DataStore(context, url, "")
        dataStore.setRoute("core", "user", "appLogin")
        dataStore.addParam("username", username)
        dataStore.addParam("password", password)
        dataStore.addParam("model", Build.MODEL)

        try {
            return Account(username, url, dataStore.getData()?.getJSONObject("data")?.getString("token"))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}