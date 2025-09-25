package de.wollis_page.gibsonos.module.growDiary.climateControl.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.climateControl.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("climateControlId" to this.getShortcut()?.parameters?.get("climateControlId"))

        return arrayOf(
            Tab(R.string.grow_diary_overview_tab, OverviewFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}