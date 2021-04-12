package de.wollis_page.gibsonos.task

import android.os.Build
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account

object UserTask: AbstractTask() {
    @Throws(TaskException::class)
    fun login(context: GibsonOsActivity, url: String, username: String, password: String): Account {
        val dataStore = DataStore(url, "")
        dataStore.setRoute("core", "user", "appLogin")
        dataStore.addParam("username", username)
        dataStore.addParam("password", password)
        dataStore.addParam("model", Build.MODEL)

        return Account(username, url, run(context, dataStore).getString("token"))
    }
}