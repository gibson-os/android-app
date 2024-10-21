package de.wollis_page.gibsonos.module.growDiary.manufacture.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.manufacture.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    private var manufactureId: Long? = null

    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.grow_diary_plant_overview_tab, OverviewFragment::class, hashMapOf("manufactureId" to this.manufactureId)),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        this.manufactureId = this.intent.getLongExtra("manufactureId", 0)

        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_manufacture_title)
    }

    override fun getId() = 0
}