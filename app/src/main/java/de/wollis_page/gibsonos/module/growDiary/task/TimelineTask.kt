package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.task.AbstractTask

object TimelineTask : AbstractTask() {
    fun get(
        context: GibsonOsActivity,
        plantId: Long,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "timeline", "")
        dataStore.addParam("plantId", plantId)

        dataStore.setTimeout(60000)

        return dataStore.loadBitmap()
    }
}