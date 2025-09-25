package de.wollis_page.gibsonos.module.growDiary.pot.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.pot.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("potId" to this.getShortcut()?.parameters?.get("potId"))

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