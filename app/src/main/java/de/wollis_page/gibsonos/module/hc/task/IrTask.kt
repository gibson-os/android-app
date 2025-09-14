package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.hc.ir.dto.Key
import de.wollis_page.gibsonos.module.hc.ir.dto.Remote
import de.wollis_page.gibsonos.task.AbstractTask

object IrTask: AbstractTask() {
    @Throws(TaskException::class)
    fun remotes(context: GibsonOsFragment, moduleId: Long, start: Long, limit: Long): ListResponse<Remote> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "hc", "ir", "remotes")
        dataStore.addParam("moduleId", moduleId)

        return this.loadList(context, dataStore, start, limit)
    }

    @Throws(TaskException::class)
    fun remote(context: GibsonOsActivity, id: Long): Remote {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "remote")
        dataStore.addParam("id", id)

        return this.load(context, dataStore)
    }

    @Throws(TaskException::class)
    fun keys(context: GibsonOsFragment, start: Long, limit: Long): ListResponse<Key> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "hc", "ir", "keys")

        return this.loadList(context, dataStore, start, limit)
    }

    @Throws(TaskException::class)
    fun send(
        context: GibsonOsActivity,
        moduleId: Long,
        id: Long
    ) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "", "POST")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("id", id)

        this.run(context, dataStore)
    }

    @Throws(TaskException::class)
    fun sendButton(
        context: GibsonOsActivity,
        moduleId: Long,
        id: Long
    ) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "button", "POST")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("id", id)

        this.run(context, dataStore)
    }
}