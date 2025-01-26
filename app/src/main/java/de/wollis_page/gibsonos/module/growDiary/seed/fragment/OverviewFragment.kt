package de.wollis_page.gibsonos.module.growDiary.seed.fragment

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.task.SeedTask

class OverviewFragment: GibsonOsFragment() {
    override fun getContentView() = R.layout.grow_diary_seed_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val seed = SeedTask.get(this.activity, this.fragmentsArguments["seedId"].toString().toLong())

            this.view?.findViewById<TextView>(R.id.name)?.text = seed.name
            this.view?.findViewById<TextView>(R.id.manufacture)?.text = seed.manufacture?.name
            this.view?.findViewById<TextView>(R.id.floweringDays)?.text = seed.minFloweringDays.toString() + " - " + seed.maxFloweringDays.toString()
            this.view?.findViewById<TextView>(R.id.height)?.text = seed.minHeight.toString() + "cm - " + seed.maxHeight.toString() + "cm"

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = SeedTask.image(this.activity, seed.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }
}