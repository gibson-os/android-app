package de.wollis_page.gibsonos.module.hc.module.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.module.io.dto.Port
import de.wollis_page.gibsonos.task.AbstractTask

object IoTask: AbstractTask() {
    @Throws(TaskException::class)
    fun ports(context: GibsonOsActivity, moduleId: Long, start: Long, limit: Long): ListResponse<Port> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "io", "ports")
        dataStore.addParam("moduleId", moduleId)
        dataStore.setPage(start, limit)

        return this.loadListResponse(this.run(context, dataStore))
    }

    @Throws(TaskException::class)
    fun toggle(context: GibsonOsActivity, moduleId: Long, number: Int) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "io", "toggle")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("number", number)
        this.run(context, dataStore)
    }
}