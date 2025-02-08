package de.wollis_page.gibsonos.module.growDiary.task

import android.graphics.Bitmap
import android.net.Uri
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Plant
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Cost
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Fertilizer
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.task.AbstractTask
import java.io.File

object PlantTask : AbstractTask() {
    fun get(context: GibsonOsActivity, plantId: Long): Plant {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "plant", "")
        dataStore.addParam("id", plantId)

        return this.load(context, dataStore)
    }

    fun list(
        context: GibsonOsActivity,
        start: Long,
        limit: Long,
        seedId: Long? = null,
        fertilizerId: Long? = null,
        substrateId: Long? = null,
        potId: Long? = null,
        setupId: Long? = null,
    ): ListResponse<Plant> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "plants")

        if (seedId != null) {
            dataStore.addParam("seedId", seedId)
        }

        if (fertilizerId != null) {
            dataStore.addParam("fertilizerId", fertilizerId)
        }

        if (substrateId != null) {
            dataStore.addParam("substrateId", substrateId)
        }

        if (potId != null) {
            dataStore.addParam("potId", potId)
        }

        if (setupId != null) {
            dataStore.addParam("setupId", setupId)
        }

        return this.loadList(context, dataStore, start, limit)
    }

    fun getImages(
        context: GibsonOsActivity,
        plantId: Long,
        start: Long,
        limit: Long
    ): ListResponse<Image> {
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
        image: Uri,
        date: String? = null,
    ): Image {
        val dataStore = this.getDataStore(
            context.getAccount(),
            "growDiary",
            "plant",
            "image",
            "POST",
        )

        var tempFile: File

        context.contentResolver.openInputStream(image)!!.use { input ->
            tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)

            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
            dataStore.addParam("image", tempFile, "image/jpg")
        }

        dataStore.addParam("plantId", plantId)

        if (date != null) {
            dataStore.addParam("date", date)
        }

        tempFile.deleteOnExit()

        return this.load(context, dataStore, catchResponseException = false)
    }

    fun getFertilizers(
        context: GibsonOsActivity,
        plantId: Long,
        start: Long,
        limit: Long
    ): ListResponse<Fertilizer> {
        val dataStore =
            this.getDataStore(context.getAccount(), "growDiary", "plant", "fertilizers")
        dataStore.addParam("plantId", plantId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getFertilizerForm(
        context: GibsonOsActivity,
        plantId: Long,
        fertilizerId: Long? = null
    ): Form {
        val dataStore =
            this.getDataStore(context.getAccount(), "growDiary", "plant", "fertilizerForm")
        dataStore.addParam("plantId", plantId)

        if (fertilizerId != null) {
            dataStore.addParam("id", fertilizerId)
        }

        return this.load(context, dataStore)
    }

    fun getCosts(context: GibsonOsActivity, plantId: Long): ListResponse<Cost> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "plant", "costs")
        dataStore.addParam("id", plantId)

        return this.loadList(context, dataStore)
    }
}