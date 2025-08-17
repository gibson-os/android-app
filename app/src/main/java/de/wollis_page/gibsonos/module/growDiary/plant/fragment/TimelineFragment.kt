package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.task.TimelineTask
import de.wollis_page.gibsonos.view.TouchImageView

class TimelineFragment: GibsonOsFragment() {
    override fun getContentView() = R.layout.grow_diary_plant_image_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.load()
    }

    private fun load() {
        val timeline = TimelineTask.get(this.activity, this.fragmentsArguments["plantId"].toString().toLong())

        this.activity.runOnUiThread {
            this.view?.findViewById<TouchImageView>(R.id.image)?.setImageBitmap(timeline)
        }
    }
}