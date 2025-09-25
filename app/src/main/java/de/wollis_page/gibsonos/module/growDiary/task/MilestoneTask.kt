package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.task.AbstractTask

object MilestoneTask: AbstractTask() {
    fun getList(context: GibsonOsFragment, plantId: Long, start: Long, limit: Long): ListResponse<Milestone> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "milestone", "list")
        dataStore.addParam("plantId", plantId)

        return this.loadList(context, dataStore, start, limit)
    }
}