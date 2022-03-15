package de.wollis_page.gibsonos.module.core.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.core.event.dto.Event
import de.wollis_page.gibsonos.task.AbstractTask

object EventTask: AbstractTask() {
    fun index(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Event> {
        val dataStore = this.getDataStore(context.getAccount(), "core", "event", "index")
        dataStore.setPage(start, limit)

        return this.loadListResponse(this.run(context, dataStore))
    }
    
    fun run(context: GibsonOsActivity, eventId: Int) {
        val dataStore = this.getDataStore(context.getAccount(), "core", "event", "run")
        dataStore.addParam("eventId", eventId)
        this.run(context, dataStore)
    }
}