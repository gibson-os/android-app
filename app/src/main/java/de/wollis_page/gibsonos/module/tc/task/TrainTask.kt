package de.wollis_page.gibsonos.module.tc.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.tc.index.dto.Train
import de.wollis_page.gibsonos.task.AbstractTask

object TrainTask: AbstractTask() {
    fun getList(context: GibsonOsFragment, start: Long, limit: Long): ListResponse<Train> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "tc", "train", "list")

        return this.loadList(context, dataStore, start, limit)
    }

    fun getImage(
        context: GibsonOsActivity,
        trainId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "tc", "train", "image")
        dataStore.addParam("id", trainId)

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