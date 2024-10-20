package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Manufacture
import de.wollis_page.gibsonos.task.AbstractTask

object ManufactureTask: AbstractTask() {
    fun list(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Manufacture> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "manufactures")

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        manufactureId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "manufacture", "image")
        dataStore.addParam("manufactureId", manufactureId)

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