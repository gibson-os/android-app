package de.wollis_page.gibsonos.module.growDiary.setup.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.index.fragment.ClimateFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.PlantFragment
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.ClimateControlFragment
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.LightFragment
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("setupId" to this.getShortcut()?.parameters?.get("setupId"))

        return arrayOf(
            Tab(R.string.grow_diary_overview_tab, OverviewFragment::class, arguments),
            Tab(R.string.grow_diary_climates_tab, ClimateFragment::class, arguments),
            Tab(R.string.grow_diary_climate_tab, ClimateControlFragment::class, arguments),
            Tab(R.string.grow_diary_light_tab, LightFragment::class, arguments),
            Tab(
                R.string.grow_diary_plant_tab,
                PlantFragment::class,
                hashMapOf("selectedFilters" to mutableMapOf("setup" to mutableListOf(this.getShortcut()?.parameters?.get("setupId").toString())))
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}