package de.wollis_page.gibsonos.module.growDiary.manufacture.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.index.fragment.ClimateControlFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.FertilizerFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.LightFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.PotFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.RoomFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.SeedFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.SubstrateFragment
import de.wollis_page.gibsonos.module.growDiary.manufacture.fragment.OverviewFragment

class IndexActivity : TabActivity() {
    private var manufactureId: Long? = null

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("manufactureId" to this.manufactureId)

        return arrayOf(
            Tab(R.string.grow_diary_overview_tab, OverviewFragment::class, arguments),
            Tab(R.string.grow_diary_seed_tab, SeedFragment::class, arguments),
            Tab(R.string.grow_diary_climate_tab, ClimateControlFragment::class, arguments),
            Tab(R.string.grow_diary_fertilizer_tab, FertilizerFragment::class, arguments),
            Tab(R.string.grow_diary_light_tab, LightFragment::class, arguments),
            Tab(R.string.grow_diary_pot_tab, PotFragment::class, arguments),
            Tab(R.string.grow_diary_room_tab, RoomFragment::class, arguments),
            Tab(R.string.grow_diary_substrate_tab, SubstrateFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.manufactureId = this.intent.getLongExtra("manufactureId", 0)

        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_manufacture_title)
    }

    override fun getId() = 0
}