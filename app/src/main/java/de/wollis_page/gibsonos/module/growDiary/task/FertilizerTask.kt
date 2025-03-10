package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Fertilizer
import de.wollis_page.gibsonos.task.AbstractTask

object FertilizerTask: AbstractTask() {
    fun get(context: GibsonOsActivity, fertilizerId: Long): Fertilizer {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "fertilizer", "")
        dataStore.addParam("id", fertilizerId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsActivity,
        start: Long,
        limit: Long,
        manufactureId: Long? = null,
    ): ListResponse<Fertilizer> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "fertilizers")

        if (manufactureId != null) {
            dataStore.addParam("manufactureId", manufactureId)
        }

        return this.loadList(context, dataStore, start, limit)
    }

    fun getImage(
        context: GibsonOsActivity,
        fertilizerId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "fertilizer", "image")
        dataStore.addParam("fertilizerId", fertilizerId)

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