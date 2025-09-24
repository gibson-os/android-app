package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Light
import de.wollis_page.gibsonos.task.AbstractTask

object LightTask: AbstractTask() {
    fun index(context: GibsonOsActivity, lightId: Long): Light {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "light", "index")
        dataStore.addParam("id", lightId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsFragment,
        start: Long,
        limit: Long,
    ): ListResponse<Light> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "index", "lights")

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        lightId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "light", "image")
        dataStore.addParam("lightId", lightId)

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