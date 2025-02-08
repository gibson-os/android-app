package de.wollis_page.gibsonos.module.growDiary.setup.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.climateControl.PlannedRuntimeFragment
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.climateControl.RuntimeFragment

class ClimateControlActivity : TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("climateControlId" to this.intent.getLongExtra("climateControlId", 0))

        return arrayOf(
            Tab(R.string.grow_diary_runtime_tab, RuntimeFragment::class, arguments),
            Tab(R.string.grow_diary_planned_runtime_tab, PlannedRuntimeFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}