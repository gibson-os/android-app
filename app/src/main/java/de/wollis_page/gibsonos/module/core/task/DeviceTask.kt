package de.wollis_page.gibsonos.module.core.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Update
import de.wollis_page.gibsonos.task.AbstractTask

object DeviceTask: AbstractTask() {
    fun addPush(context: GibsonOsActivity, update: Update)
    {
        val dataStore = this.getDataStore(context.getAccount(), "core", "device", "addPush", "POST")
        dataStore.addParam("module", update.module)
        dataStore.addParam("task", update.task)
        dataStore.addParam("action", update.action)
        dataStore.addParam("foreignId", update.foreignId)
        this.run(context, dataStore)
    }

    fun removePush(context: GibsonOsActivity, update: Update)
    {
        val dataStore = this.getDataStore(context.getAccount(), "core", "device", "push", "DELETE")
        dataStore.addParam("module", update.module)
        dataStore.addParam("task", update.task)
        dataStore.addParam("action", update.action)
        dataStore.addParam("foreignId", update.foreignId)
        this.run(context, dataStore)
    }
}