package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Substrate
import de.wollis_page.gibsonos.task.AbstractTask

object SubstrateTask: AbstractTask() {
    fun index(context: GibsonOsActivity, substrateId: Long): Substrate {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "substrate", "index")
        dataStore.addParam("id", substrateId)

        return this.load(context, dataStore)
    }

    fun list(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Substrate> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "substrates")

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        substrateId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "substrate", "image")
        dataStore.addParam("substrateId", substrateId)

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