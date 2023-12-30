package de.wollis_page.gibsonos.module.obscura.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.task.AbstractTask

object ScannerTask: AbstractTask() {
    fun getForm(
        context: GibsonOsActivity,
        deviceName: String,
        vendor: String,
        model: String,
    ): Form {
        val dataStore = this.getDataStore(context.getAccount(), "obscura", "scanner", "form")
        dataStore.addParam("deviceName", deviceName)
        dataStore.addParam("vendor", vendor)
        dataStore.addParam("model", model)

        return this.load(context, dataStore)
    }
}