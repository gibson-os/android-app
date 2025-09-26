package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.plant.fragment.pot.SubstrateFragment

class PotActivity: TabActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf(
            "plantId" to this.intent.getLongExtra("plantId", 0),
            "potId" to this.intent.getLongExtra("potId", 0)
        )

        return arrayOf(
            Tab(R.string.grow_diary_substrate_tab, SubstrateFragment::class, arguments),
        )
    }

    override fun getId() = 0
}
