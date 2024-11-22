package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.ClimateFragement
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.FeedFragement
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.ImageFragement
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.MilestoneFragement
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    private var plantId: Long? = null

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("plantId" to this.getShortcut()?.parameters?.get("plantId"))

        return arrayOf(
            Tab(R.string.grow_diary_plant_overview_tab, OverviewFragment::class, arguments),
            Tab(R.string.grow_diary_plant_images_tab, ImageFragement::class, arguments),
            Tab(R.string.grow_diary_plant_climates_tab, ClimateFragement::class, arguments),
            Tab(R.string.grow_diary_plant_feed_tab, FeedFragement::class, arguments),
            Tab(R.string.grow_diary_plant_milestone_tab, MilestoneFragement::class, arguments),
        )
    }

    override fun getId() = this.getShortcut()?.parameters?.get("plantId") ?: 0
}