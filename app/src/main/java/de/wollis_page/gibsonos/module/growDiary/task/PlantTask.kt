package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Plant
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.task.AbstractTask

object PlantTask: AbstractTask() {
    fun index(context: GibsonOsActivity, plantId: Long): Plant {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "plant", "index")
        dataStore.addParam("id", plantId)

        return this.load(context, dataStore)
    }

    fun list(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Plant> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "plants")

        return this.loadList(context, dataStore, start, limit)
    }

    fun getImages(context: GibsonOsActivity, plantId: Long, start: Long, limit: Long): ListResponse<Image> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "plant", "images")
        dataStore.addParam("plantId", plantId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getImage(
        context: GibsonOsActivity,
        plantId: Long,
        width: Int? = null,
        height: Int? = null,
        date: String? = null,
    ): Bitmap {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "plant", "image")
        dataStore.addParam("plantId", plantId)

        if (date != null) {
            dataStore.addParam("date", date)
        }

        if (width != null) {
            dataStore.addParam("width", width)
        }

        if (height != null) {
            dataStore.addParam("height", height)
        }

        dataStore.setTimeout(60000)

        return dataStore.loadBitmap()
    }

    fun postImage(
        context: GibsonOsActivity,
        plantId: Long,
        image: Bitmap,
        date: String? = null,
    ): Image {
        val dataStore = this.getDataStore(
            context.getAccount(),
            "growDiary",
            "plant",
            "image",
            "POST",
        )
        dataStore.addParam("image", image)
        dataStore.addParam("plantId", plantId)

        if (date != null) {
            dataStore.addParam("date", date)
        }

        return this.load(context, dataStore)
    }
}