package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.ClimateControl
import de.wollis_page.gibsonos.task.AbstractTask

object ClimateControlTask: AbstractTask() {
    fun index(context: GibsonOsActivity, climateControlId: Long): ClimateControl {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "climateControl", "index")
        dataStore.addParam("id", climateControlId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsFragment,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControl> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "index", "climateControls")

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        climateControlId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "climateControl", "image")
        dataStore.addParam("climateControlId", climateControlId)

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