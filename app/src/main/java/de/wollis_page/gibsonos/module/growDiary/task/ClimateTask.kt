package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.task.AbstractTask

object ClimateTask: AbstractTask() {
    fun index(context: GibsonOsActivity, climateId: Long): Climate {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "climate", "index")
        dataStore.addParam("id", climateId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsActivity,
        start: Long,
        limit: Long,
        manufactureId: Long? = null,
    ): ListResponse<Climate> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "climates")

        if (manufactureId != null) {
            dataStore.addParam("manufactureId", manufactureId)
        }

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        climateId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "climate", "image")
        dataStore.addParam("climateId", climateId)

        if (width != null) {
            dataStore.addParam("width", width)
        }

        if (height != null) {
            dataStore.addParam("height", height)
        }

        dataStore.setTimeout(60000)

        return dataStore.loadBitmap()
    }
}