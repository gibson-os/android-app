package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.task.AbstractTask

object ClimateTask: AbstractTask() {
    fun getList(context: GibsonOsActivity, plantId: Long, start: Long, limit: Long): ListResponse<Climate> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "climates")
        dataStore.addParam("plantId", plantId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getForm(context: GibsonOsActivity, plantId: Long, climateId: Long? = null): Form {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "climate", "form")
        dataStore.addParam("plantId", plantId)

        if (climateId != null) {
            dataStore.addParam("id", climateId)
        }

        return this.load(context, dataStore)
    }
}