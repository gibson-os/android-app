package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Room
import de.wollis_page.gibsonos.task.AbstractTask

object RoomTask: AbstractTask() {
    fun index(context: GibsonOsActivity, roomId: Long): Room {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "room", "index")
        dataStore.addParam("id", roomId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsActivity,
        start: Long,
        limit: Long,
        manufactureId: Long? = null,
    ): ListResponse<Room> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "rooms")

        if (manufactureId != null) {
            dataStore.addParam("manufactureId", manufactureId)
        }

        return this.loadList(context, dataStore, start, limit)
    }

    fun image(
        context: GibsonOsActivity,
        roomId: Long,
        width: Int? = null,
        height: Int? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "room", "image")
        dataStore.addParam("roomId", roomId)

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