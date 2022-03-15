package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.task.AbstractTask

object MasterTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Master> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "master", "index")
        dataStore.setPage(start, limit)

        return this.loadListResponse(this.run(context, dataStore))
    }
}