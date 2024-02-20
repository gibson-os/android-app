package de.wollis_page.gibsonos.module.explorer.task

import android.graphics.Bitmap
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.task.AbstractTask

object FileTask: AbstractTask() {
    @Throws(TaskException::class)
    fun image(account: Account, directory: String, filename: String, width: Int? = null, height: Int? = null): Bitmap {
        val dataStore = this.getDataStore(account, "explorer", "file", "image")
        dataStore.addParam("dir", directory)
        dataStore.addParam("filename", filename)
        dataStore.setTimeout(60000)

        if (width != null) {
            dataStore.addParam("width", width)
        }

        if (height != null) {
            dataStore.addParam("height", height)
        }

        return dataStore.loadBitmap()
    }

    fun metaInfos(context: GibsonOsActivity, path: String): MutableMap<String, Any> {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "file", "metaInfos")
        dataStore.addParam("path", path)

        return this.load(context, dataStore)
    }
}