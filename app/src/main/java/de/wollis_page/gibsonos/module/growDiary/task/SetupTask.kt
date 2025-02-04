package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.module.growDiary.index.dto.Setup
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.ClimateControl
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.Light
import de.wollis_page.gibsonos.task.AbstractTask
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.climateControl.PlannedRuntime as ClimateControlPlannedRuntime
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.climateControl.Runtime as ClimateControlRuntime
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.light.PlannedRuntime as LightPlannedRuntime
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.light.Runtime as LightRuntime

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

    fun getPlannedClimateCOntrolRuntimes(
        context: GibsonOsActivity,
        climateControlId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControlPlannedRuntime> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "plannedClimateControlRuntimes")
        dataStore.addParam("id", climateControlId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getPlannedLightRuntimes(
        context: GibsonOsActivity,
        lightId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<LightPlannedRuntime> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "plannedLightRuntimes")
        dataStore.addParam("id", lightId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getClimateControlRuntimes(
        context: GibsonOsActivity,
        climateControlId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControlRuntime> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "climateControlRuntimes")
        dataStore.addParam("id", climateControlId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getLightRuntimes(
        context: GibsonOsActivity,
        lightId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<LightRuntime> {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "lightRuntimes")
        dataStore.addParam("id", lightId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun postSwitchClimateControl(context: GibsonOsActivity, climateControlId: Long): ClimateControlRuntime {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "switchClimateControl", "POST")
        dataStore.addParam("id", climateControlId)

        return this.load(context, dataStore)
    }

    fun postSwitchLight(context: GibsonOsActivity, lightId: Long): LightRuntime {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "switchLight", "POST")
        dataStore.addParam("id", lightId)

        return this.load(context, dataStore)
    }
}