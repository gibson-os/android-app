package de.wollis_page.gibsonos.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Desktop
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.model.Account

object DesktopTask: AbstractTask() {
    fun index(context: GibsonOsActivity, account: Account): Desktop {
        val response = run(context, getDataStore(context, account, "core", "desktop", "index"))
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Desktop::class.java)

        return jsonAdapter.fromJson(response.getJSONObject("data").toString()) ?:
            throw TaskException("Account not in response!", R.string.account_error_not_exists)
    }
}