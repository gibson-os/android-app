package de.wollis_page.gibsonos.module.growDiary.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.growDiary.index.fragment.ManufactureFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.PlantFragment
import de.wollis_page.gibsonos.module.growDiary.index.fragment.SeedFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.grow_diary_plant_tab, PlantFragment::class),
        Tab(R.string.grow_diary_seed_tab, SeedFragment::class),
        Tab(R.string.grow_diary_manufacture_tab, ManufactureFragment::class),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}