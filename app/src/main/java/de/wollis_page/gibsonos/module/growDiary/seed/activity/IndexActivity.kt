package de.wollis_page.gibsonos.module.growDiary.seed.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.index.fragment.PlantFragment
import de.wollis_page.gibsonos.module.growDiary.seed.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    private var seedId: Long? = null

    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.grow_diary_overview_tab, OverviewFragment::class, hashMapOf("seedId" to this.seedId)),
        Tab(R.string.grow_diary_plant_tab, PlantFragment::class, hashMapOf("seedId" to this.seedId)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        this.seedId = this.intent.getLongExtra("seedId", 0)

        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_seed_title)
    }

    override fun getId() = 0
}