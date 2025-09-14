package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.task.AbstractTask

object MasterTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsFragment, start: Long, limit: Long): ListResponse<Master> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "hc", "master", "")

        return this.loadList(context, dataStore, start, limit)
    }
}