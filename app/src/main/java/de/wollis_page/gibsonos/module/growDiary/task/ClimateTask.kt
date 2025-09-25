package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.task.AbstractTask

object ClimateTask: AbstractTask() {
    fun getList(context: GibsonOsFragment, plantId: Long?, setupId: Long?, start: Long, limit: Long): ListResponse<Climate> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "index", "climates")

        if (plantId != null) {
            dataStore.addParam("plantId", plantId)
        }

        if (setupId != null) {
            dataStore.addParam("setupId", setupId)
        }

        return this.loadList(context, dataStore, start, limit)
    }
}