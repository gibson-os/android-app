package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.index.fragment.ClimateFragment
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.CostFragment
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.FeedFragment
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.FertilizerFragment
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.ImageFragment
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.MilestoneFragment
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("plantId" to this.getShortcut()?.parameters?.get("plantId"))

        return arrayOf(
            Tab(R.string.grow_diary_overview_tab, OverviewFragment::class, arguments),
            Tab(R.string.grow_diary_plant_images_tab, ImageFragment::class, arguments),
            Tab(R.string.grow_diary_climates_tab, ClimateFragment::class, arguments),
            Tab(R.string.grow_diary_plant_feed_tab, FeedFragment::class, arguments),
            Tab(R.string.grow_diary_plant_milestone_tab, MilestoneFragment::class, arguments),
            Tab(R.string.grow_diary_plant_costs_tab, CostFragment::class, arguments),
            Tab(R.string.grow_diary_plant_fertilizer_tab, FertilizerFragment::class, arguments),
        )
    }

    override fun getId() = this.getShortcut()?.parameters?.get("plantId") ?: 0
}