package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Setup
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.ClimateControl
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.Light
import de.wollis_page.gibsonos.task.AbstractTask

object SetupTask: AbstractTask() {
    fun get(context: GibsonOsActivity, setupId: Long): Setup {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "")
        dataStore.addParam("id", setupId)

        return this.load(context, dataStore)
    }

    fun getList(
        context: GibsonOsActivity,
        start: Long,
        limit: Long,
    ): ListResponse<Setup> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "index", "setups")

        return this.loadList(context, dataStore, start, limit)
    }

    fun getClimateControls(
        context: GibsonOsActivity,
        setupId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControl> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "climateControls")
        dataStore.addParam("id", setupId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getLights(
        context: GibsonOsActivity,
        setupId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<Light> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "lights")
        dataStore.addParam("id", setupId)

        return this.loadList(context, dataStore, start, limit)
    }
}