package de.wollis_page.gibsonos.module.core.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.core.event.dto.Event
import de.wollis_page.gibsonos.task.AbstractTask

object EventTask: AbstractTask() {
    fun index(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Event> {
        val dataStore = this.getDataStore(context.getAccount(), "core", "event", "")

        return this.loadList(context, dataStore, start, limit)
    }
    
    fun run(context: GibsonOsActivity, eventId: Long) {
        val dataStore = this.getDataStore(context.getAccount(), "core", "event", "run", "POST")
        dataStore.addParam("eventId", eventId)
        this.run(context, dataStore)
    }
}