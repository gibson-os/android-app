package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
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

    fun form(context: GibsonOsActivity, plantId: Long, feedId: Long? = null): Form {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "feed", "form")
        dataStore.addParam("plantId", plantId)

        if (feedId != null) {
            dataStore.addParam("id", feedId)
        }

        return this.load(context, dataStore)
    }
}