package de.wollis_page.gibsonos.module.core.task

import android.os.Build
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.task.AbstractTask

object UserTask: AbstractTask() {
    fun login(context: GibsonOsActivity, url: String, username: String, password: String): Account {
        val dataStore = DataStore(url, "")
        dataStore.setRoute("core", "user", "appLogin")
        dataStore.addParam("username", username)
        dataStore.addParam("password", password)
        dataStore.addParam("model", Build.MODEL)
        dataStore.addParam("fcmToken", context.application.firebaseToken.toString())

        val data = this.run(context, dataStore).getJSONObject("data")

        return Account(
            data.getLong("id"),
            data.getString("user"),
            data.getLong("deviceId"),
            url,
            data.getString("token")
        )
    }

    fun deleteDevice(context: GibsonOsActivity, account: Account) {
        val dataStore = this.getDataStore(account, "core", "user", "deleteDevice")
        dataStore.addParam("id", account.userId)
        dataStore.addParam("devices[]", account.deviceId)
        this.run(context, dataStore)
    }

    fun updateFcmToken(account: Account, fcmToken: String) {
        val dataStore = this.getDataStore(account, "core", "user", "updateFcmToken")
        dataStore.addParam("token", account.token.toString())
        dataStore.addParam("fcmToken", fcmToken)
        dataStore.loadJson()
    }
}