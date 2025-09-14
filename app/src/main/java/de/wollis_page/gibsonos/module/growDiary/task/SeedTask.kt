package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Seed
import de.wollis_page.gibsonos.task.AbstractTask

object SeedTask: AbstractTask() {
    fun get(context: GibsonOsActivity, seedId: Long): Seed {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "seed", "")
        dataStore.addParam("id", seedId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsFragment,
        start: Long,
        limit: Long,
        manufactureId: Long? = null,
    ): ListResponse<Seed> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "index", "seeds")

        if (manufactureId != null) {
            dataStore.addParam("manufactureId", manufactureId)
        }

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        seedId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "seed", "image")
        dataStore.addParam("seedId", seedId)

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