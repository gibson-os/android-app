package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.task.AbstractTask

object FormTask: AbstractTask() {
    fun getForm(context: GibsonOsActivity, task: String, action: String = "form", id: Long? = null): Form {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", task, action)

        if (id != null) {
            dataStore.addParam("id", id)
        }

        return this.load(context, dataStore)
    }
}