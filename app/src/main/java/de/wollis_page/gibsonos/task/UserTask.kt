package de.wollis_page.gibsonos.task

import android.os.Build
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account

object UserTask {
    @JvmStatic
    fun login(context: GibsonOsActivity, url: String, username: String, password: String): Account? {
        val dataStore = DataStore(context, url, "")
        dataStore.setRoute("core", "user", "appLogin")
        dataStore.addParam("username", username)
        dataStore.addParam("password", password)
        dataStore.addParam("model", Build.MODEL)
        context.showLoading()

        try {
            return Account(username, url, dataStore.getData()?.getJSONObject("data")?.getString("token"))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            context.hideLoading()
        }

        return null
    }
}