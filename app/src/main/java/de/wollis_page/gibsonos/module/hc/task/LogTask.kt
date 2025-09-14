package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.hc.index.dto.Log
import de.wollis_page.gibsonos.task.AbstractTask

object LogTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsFragment, start: Long, limit: Long, masterId: Long? = null, moduleId: Long? = null): ListResponse<Log> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "hc", "index", "log")

        if (masterId != null) {
            dataStore.addParam("masterId", masterId)
        }

        if (moduleId != null) {
            dataStore.addParam("moduleId", moduleId)
        }

        return this.loadList(context, dataStore, start, limit)
    }
}