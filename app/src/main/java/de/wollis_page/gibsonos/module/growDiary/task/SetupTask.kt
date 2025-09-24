package de.wollis_page.gibsonos.module.growDiary.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Form
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
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
        context: GibsonOsFragment,
        start: Long,
        limit: Long,
    ): ListResponse<Setup> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "index", "setups")

        return this.loadList(context, dataStore, start, limit)
    }

    fun getClimateControls(
        context: GibsonOsFragment,
        setupId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControl> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "setup", "climateControls")
        dataStore.addParam("id", setupId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getLights(
        context: GibsonOsFragment,
        setupId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<Light> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "setup", "lights")
        dataStore.addParam("id", setupId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getPlannedClimateCOntrolRuntimes(
        context: GibsonOsFragment,
        climateControlId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControlPlannedRuntime> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "setup", "plannedClimateControlRuntimes")
        dataStore.addParam("id", climateControlId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getPlannedLightRuntimes(
        context: GibsonOsFragment,
        lightId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<LightPlannedRuntime> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "setup", "plannedLightRuntimes")
        dataStore.addParam("id", lightId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getClimateControlRuntimes(
        context: GibsonOsFragment,
        climateControlId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<ClimateControlRuntime> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "setup", "climateControlRuntimes")
        dataStore.addParam("id", climateControlId)

        return this.loadList(context, dataStore, start, limit)
    }

    fun getLightRuntimes(
        context: GibsonOsFragment,
        lightId: Long,
        start: Long,
        limit: Long,
    ): ListResponse<LightRuntime> {
        val dataStore = this.getDataStore(context.activity.getAccount(), "growDiary", "setup", "lightRuntimes")
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

    fun climateControlForm(context: GibsonOsActivity, setupId: Long, climateControlId: Long? = null): Form {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "climateControlForm")
        dataStore.addParam("setupId", setupId)

        if (climateControlId != null) {
            dataStore.addParam("id", climateControlId)
        }

        return this.load(context, dataStore)
    }

    fun lightForm(context: GibsonOsActivity, setupId: Long, lightId: Long? = null): Form {
        val dataStore = this.getDataStore(context.getAccount(), "growDiary", "setup", "lightForm")
        dataStore.addParam("setupId", setupId)

        if (lightId != null) {
            dataStore.addParam("id", lightId)
        }

        return this.load(context, dataStore)
    }
}