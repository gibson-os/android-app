package de.wollis_page.gibsonos.module.explorer.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.ConvertStatus
import de.wollis_page.gibsonos.task.AbstractTask

object Html5Task: AbstractTask() {
    fun convertStatus(context: GibsonOsActivity, token: String): ConvertStatus {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "html5", "convertStatus")
        dataStore.addParam("token", token)

        return this.load(context, dataStore, R.string.explorer_html5_error_convert_status)
    }
}