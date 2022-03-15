package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.task.AbstractTask

object ModuleTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsActivity, masterId: Long, start: Long, limit: Long): ListResponse<Module> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "slave", "index")
        dataStore.addParam("masterId", masterId)
        dataStore.setPage(start, limit)

        return this.loadListResponse(this.run(context, dataStore))
    }
}