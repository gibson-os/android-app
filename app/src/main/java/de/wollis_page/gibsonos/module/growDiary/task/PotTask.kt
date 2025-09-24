package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Pot
import de.wollis_page.gibsonos.task.AbstractTask

object PotTask: AbstractTask() {
    fun index(context: GibsonOsActivity, potId: Long): Pot {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "pot", "index")
        dataStore.addParam("id", potId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsFragment,
        start: Long,
        limit: Long,
    ): ListResponse<Pot> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "index", "pots")

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        potId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "pot", "image")
        dataStore.addParam("potId", potId)

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