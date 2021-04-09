package de.wollis_page.gibsonos.task

import android.widget.Toast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Desktop
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.DataStore
import de.wollis_page.gibsonos.model.Account

object DesktopTask {
    fun index(context: GibsonOsActivity, account: Account): Desktop {
        val token = account.token ?: throw TaskException("Account token doesn't exists!")

        val dataStore = DataStore(context, account.url, token)
        dataStore.setRoute("core", "desktop", "index")
        context.showLoading()

        try {
            val response = dataStore.getData()
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Desktop::class.java)

            return jsonAdapter.fromJson(response.getJSONObject("data").toString()) ?:
                throw TaskException("Account not in response!")
        } catch (exception: Exception) {
            exception.printStackTrace()

            throw TaskException(exception.message ?: "Task exception")
        } finally {
            context.hideLoading()
        }
    }
}