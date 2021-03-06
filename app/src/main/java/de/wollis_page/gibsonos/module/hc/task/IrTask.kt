package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.ir.dto.Key
import de.wollis_page.gibsonos.module.hc.ir.dto.Remote
import de.wollis_page.gibsonos.task.AbstractTask

object IrTask: AbstractTask() {
    @Throws(TaskException::class)
    fun remotes(context: GibsonOsActivity, moduleId: Long, start: Long, limit: Long): ListResponse<Remote> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "remotes")
        dataStore.addParam("moduleId", moduleId)

        return this.loadList(context, dataStore, start, limit)
    }

    @Throws(TaskException::class)
    fun remote(context: GibsonOsActivity, remoteId: Long): Remote {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "remote")
        dataStore.addParam("remoteId", remoteId)

        return this.load(context, dataStore)
    }

    @Throws(TaskException::class)
    fun keys(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Key> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "keys")

        return this.loadList(context, dataStore, start, limit)
    }

    @Throws(TaskException::class)
    fun send(
        context: GibsonOsActivity,
        moduleId: Long,
        protocol: Int,
        address: Int,
        command: Int
    ) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "send")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("protocol", protocol)
        dataStore.addParam("address", address)
        dataStore.addParam("command", command)

        this.run(context, dataStore)
    }

    @Throws(TaskException::class)
    fun sendRemoteKey(
        context: GibsonOsActivity,
        moduleId: Long,
        eventId: Long?,
        keys: MutableList<Key>
    ) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "sendRemoteKey")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("eventId", eventId ?: 0)

        val keySubIds = mutableListOf<Long>()

        keys.forEach {
            keySubIds.add(it.id)
        }

        dataStore.addParam("keys", keySubIds)

        this.run(context, dataStore)
    }
}