package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import kotlin.math.floor

class OverviewFragment: GibsonOsFragment() {
    override fun getContentView() = R.layout.grow_diary_plant_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val plant = PlantTask.index(this.activity, this.fragmentsArguments["plantId"].toString().toLong())

            this.view?.findViewById<TextView>(R.id.name)?.text = plant.name
            this.view?.findViewById<TextView>(R.id.seed)?.text = plant.seed.name

            if (plant.grownAt == null) {
                this.view?.findViewById<LinearLayout>(R.id.grownAtContainer)?.visibility = View.INVISIBLE
                this.view?.findViewById<LinearLayout>(R.id.grownDayContainer)?.visibility = View.INVISIBLE
            }

            this.view?.findViewById<TextView>(R.id.grownAt)?.text = plant.grownAt
            this.view?.findViewById<TextView>(R.id.grownDay)?.text = plant.grownDay.toString() + " Tage (" + (plant.grownDay?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + " Woche)"

            if (plant.floweringAt == null) {
                this.view?.findViewById<LinearLayout>(R.id.floweringAtContainer)?.visibility = View.INVISIBLE
                this.view?.findViewById<LinearLayout>(R.id.floweringDayContainer)?.visibility = View.INVISIBLE
            }

            this.view?.findViewById<TextView>(R.id.floweringAt)?.text = plant.floweringAt
            this.view?.findViewById<TextView>(R.id.floweringDay)?.text = plant.floweringDay.toString() + " Tage (" + (plant.floweringDay?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + " Woche)"

            if (plant.harvestedAt == null) {
                this.view?.findViewById<LinearLayout>(R.id.harvestedAtContainer)?.visibility = View.INVISIBLE
                this.view?.findViewById<LinearLayout>(R.id.harvestedWetContainer)?.visibility = View.INVISIBLE
                this.view?.findViewById<LinearLayout>(R.id.harvestedDryContainer)?.visibility = View.INVISIBLE
            }

            this.view?.findViewById<TextView>(R.id.harvestedAt)?.text = plant.harvestedAt
            this.view?.findViewById<TextView>(R.id.harvestedWet)?.text = plant.harvestedWet.toString()
            this.view?.findViewById<TextView>(R.id.harvestedDry)?.text = plant.harvestedDry.toString()

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = PlantTask.image(this.activity, plant.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }
}