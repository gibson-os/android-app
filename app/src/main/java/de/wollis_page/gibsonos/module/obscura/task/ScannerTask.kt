package de.wollis_page.gibsonos.module.obscura.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.module.obscura.scanner.dto.Status
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

    fun getStatus(context: GibsonOsActivity, deviceName: String, lastCheck: String? = null): Status {
        val dataStore = this.getDataStore(context.getAccount(), "obscura", "scanner", "status")
        dataStore.addParam("deviceName", deviceName)

        if (lastCheck != null) {
            dataStore.addParam("lastCheck", lastCheck)
        }

        return this.load(context, dataStore, showLoading = false, catchResponseException = false)
    }
}