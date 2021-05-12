package de.wollis_page.gibsonos.module.explorer.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.explorer.index.dto.ConvertStatus
import de.wollis_page.gibsonos.task.AbstractTask

object Html5Task: AbstractTask() {
    fun convertStatus(context: GibsonOsActivity, token: String): ConvertStatus {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "html5", "convertStatus")
        dataStore.addParam("token", token)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(ConvertStatus::class.java)

        return jsonAdapter.fromJson(response.getJSONObject("data").toString()) ?:
            throw TaskException("Convert status not in response!", R.string.explorer_html5_error_convert_status)
    }
}