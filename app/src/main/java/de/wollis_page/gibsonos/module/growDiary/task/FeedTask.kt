package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.task.AbstractTask

object FeedTask: AbstractTask() {
    fun list(context: GibsonOsFragment, plantId: Long, start: Long, limit: Long): ListResponse<Feed> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "feed", "list")
        dataStore.addParam("plantId", plantId)

        return this.loadList(context, dataStore, start, limit)
    }
}