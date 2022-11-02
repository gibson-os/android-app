package de.wollis_page.gibsonos.module.explorer.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.ConvertStatus
import de.wollis_page.gibsonos.task.AbstractTask

object Html5Task: AbstractTask() {
    fun convertStatus(context: GibsonOsActivity, token: String): ConvertStatus {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "html5", "convertStatus")
        dataStore.addParam("token", token)

        return this.load(context, dataStore, R.string.explorer_html5_error_convert_status, false)
    }

    fun savePosition(context: GibsonOsActivity, token: String, position: Long) {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "html5", "savePosition")
        dataStore.addParam("token", token)
        dataStore.addParam("position", position)
        // @todo add user id to account
//        dataStore.addParam("userIds", context.getAccount().user)
        this.run(context, dataStore)
    }

    fun convert(
        context: GibsonOsActivity,
        dir: String,
        files: MutableList<String>,
        audioStream: String? = null,
        subtitleStream: String? = null
    ): MutableMap<String, String> {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "html5", "convert")
        dataStore.addParam("dir", dir)
        dataStore.addParam("files", files)
        audioStream?.let { dataStore.addParam("audioStream", it) }
        subtitleStream?.let { dataStore.addParam("subtitleStream", it) }

        return this.load(context, dataStore)
    }
}