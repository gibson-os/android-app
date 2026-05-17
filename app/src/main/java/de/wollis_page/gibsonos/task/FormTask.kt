package de.wollis_page.gibsonos.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.form.FormFile
import org.json.JSONObject

object FormTask: AbstractTask() {
    fun post(
        context: GibsonOsActivity,
        module: String,
        task: String,
        action: String,
        parameters: Map<String, *>,
    ): JSONObject {
        val dataStore = this.getDataStore(context.getAccount(), module, task, action, "POST")

        parameters.forEach {
            val value = it.value

            if (value is FormFile) {
                dataStore.addParam(it.key, value.file, value.mimeType)
            } else {
                dataStore.addParam(it.key, it.value.toString())
            }
        }

        return this.run(context, dataStore, catchResponseException = false)
    }
}