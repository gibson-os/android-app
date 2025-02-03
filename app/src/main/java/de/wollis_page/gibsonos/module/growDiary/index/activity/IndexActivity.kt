package de.wollis_page.gibsonos.module.growDiary.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.index.fragment.ClimateControlFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.FertilizerFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.LightFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.ManufactureFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.PlantFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.PotFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.RoomFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.SeedFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.SetupFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.SubstrateFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.grow_diary_plant_tab, PlantFragment::class),
        Tab(R.string.grow_diary_setup_tab, SetupFragment::class),
        Tab(R.string.grow_diary_seed_tab, SeedFragment::class),
        Tab(R.string.grow_diary_room_tab, RoomFragment::class),
        Tab(R.string.grow_diary_light_tab, LightFragment::class),
        Tab(R.string.grow_diary_climate_tab, ClimateControlFragment::class),
        Tab(R.string.grow_diary_pot_tab, PotFragment::class),
        Tab(R.string.grow_diary_substrate_tab, SubstrateFragment::class),
        Tab(R.string.grow_diary_fertilizer_tab, FertilizerFragment::class),
        Tab(R.string.grow_diary_manufacture_tab, ManufactureFragment::class),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}