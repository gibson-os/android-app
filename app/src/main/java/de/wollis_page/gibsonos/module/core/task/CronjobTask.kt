package de.wollis_page.gibsonos.module.core.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.core.cronjob.dto.Cronjob
import de.wollis_page.gibsonos.task.AbstractTask

object CronjobTask: AbstractTask() {
    fun index(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Cronjob> {
        val dataStore = this.getDataStore(context.getAccount(), "core", "cronjob", "index")

        return this.loadList(context, dataStore, start, limit)
    }
}