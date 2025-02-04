package de.wollis_page.gibsonos.module.growDiary.setup.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.light.PlannedRuntimeFragment
import de.wollis_page.gibsonos.module.growDiary.setup.fragment.light.RuntimeFragment

class LightActivity : TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("lightId" to this.intent.getLongExtra("lightId", 0))

        return arrayOf(
            Tab(R.string.grow_diary_planned_runtime_tab, PlannedRuntimeFragment::class, arguments),
            Tab(R.string.grow_diary_runtime_tab, RuntimeFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}