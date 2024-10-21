package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    private var plantId: Long? = null

    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.grow_diary_plant_overview_tab, OverviewFragment::class, hashMapOf("plantId" to this.plantId)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        this.plantId = this.intent.getLongExtra("plantId", 0)

        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_plant_title)
    }

    override fun getId() = 0
}