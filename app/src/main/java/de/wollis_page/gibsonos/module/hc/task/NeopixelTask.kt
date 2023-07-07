package de.wollis_page.gibsonos.module.hc.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.neopixel.dto.Pixel
import de.wollis_page.gibsonos.task.AbstractTask

object NeopixelTask: AbstractTask() {
    @Throws(TaskException::class)
    fun pixels(context: GibsonOsActivity, moduleId: Long): ListResponse<Pixel> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "neopixel", "")
        dataStore.addParam("moduleId", moduleId)

        return this.loadList(context, dataStore)
    }
}