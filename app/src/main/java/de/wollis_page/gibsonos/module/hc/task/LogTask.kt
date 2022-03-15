package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.index.dto.Log
import de.wollis_page.gibsonos.task.AbstractTask

object LogTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsActivity, start: Long, limit: Long, masterId: Long? = null, moduleId: Long? = null): ListResponse<Log> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "index", "log")
        dataStore.setPage(start, limit)

        if (masterId != null) {
            dataStore.addParam("masterId", masterId)
        }

        if (moduleId != null) {
            dataStore.addParam("moduleId", moduleId)
        }

        return this.loadListResponse(this.run(context, dataStore))
    }
}