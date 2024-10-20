package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> = arrayOf(
//        Tab(R.string.grow_diary_plant_images_tab, ImageFragment::class),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.grow_diary_title)
    }

    override fun getId() = 0
}