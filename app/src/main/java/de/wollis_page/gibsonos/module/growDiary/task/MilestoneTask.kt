package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.task.AbstractTask

object MilestoneTask: AbstractTask() {
    fun getList(context: GibsonOsActivity, plantId: Long, start: Long, limit: Long): ListResponse<Milestone> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "milestone", "list")
        dataStore.addParam("plantId", plantId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getForm(context: GibsonOsActivity, plantId: Long, feedId: Long? = null): Form {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "milestone", "form")
        dataStore.addParam("plantId", plantId)

        if (feedId != null) {
            dataStore.addParam("id", feedId)
        }

        return this.load(context, dataStore)
    }
}