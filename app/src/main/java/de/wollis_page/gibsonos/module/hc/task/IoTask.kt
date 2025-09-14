package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.hc.io.dto.Port
import de.wollis_page.gibsonos.task.AbstractTask

object IoTask: AbstractTask() {
    @Throws(TaskException::class)
    fun ports(context: GibsonOsFragment, moduleId: Long, start: Long, limit: Long): ListResponse<Port> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "hc", "io", "ports")
        dataStore.addParam("moduleId", moduleId)

        return this.loadList(context, dataStore, start, limit)
    }

    @Throws(TaskException::class)
    fun toggle(context: GibsonOsActivity, moduleId: Long, id: Long) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "io", "toggle", "POST")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("id", id)
        this.run(context, dataStore)
    }
}