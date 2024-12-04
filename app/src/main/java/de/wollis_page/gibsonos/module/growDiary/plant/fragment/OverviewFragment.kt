package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask

class OverviewFragment: GibsonOsFragment() {
    override fun getContentView() = R.layout.grow_diary_plant_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val plant = PlantTask.index(this.activity, this.fragmentsArguments["plantId"].toString().toLong())

            this.view?.findViewById<TextView>(R.id.name)?.text = plant.name
            this.view?.findViewById<TextView>(R.id.seed)?.text = plant.seed.name

            if (plant.seed.minGrowingDays == null) {
                this.view?.findViewById<LinearLayout>(R.id.minGrownDaysContainer)?.visibility = View.INVISIBLE
            }

            if (plant.seed.maxGrowingDays == null) {
                this.view?.findViewById<LinearLayout>(R.id.maxGrownDaysContainer)?.visibility = View.INVISIBLE
            }

            this.activity.setTitle(plant.name)

            this.view?.findViewById<TextView>(R.id.grownAt)?.text = plant.state?.grownAt
            this.view?.findViewById<TextView>(R.id.lastDate)?.text = plant.state?.to
            this.view?.findViewById<TextView>(R.id.lastDateText)?.text = plant.state?.title
            this.view?.findViewById<TextView>(R.id.lastStateDays)?.text = "${plant.state?.toDaysSinceStart} Tage"
            this.view?.findViewById<TextView>(R.id.lastStateWeek)?.text = "${plant.state?.toWeekSinceStart} Wochen"

            val statesContainer = this.view?.findViewById<LinearLayout>(R.id.statesContainer)
            val inflater = LayoutInflater.from(this.activity)

            this.activity.runOnUiThread {
                plant.states?.forEach { key, state ->
                    val card = inflater.inflate(
                        R.layout.grow_diary_plant_state_item,
                        this.view?.findViewById(android.R.id.content),
                        false
                    )
                    card.findViewById<TextView>(R.id.title).text = state.title
                    card.findViewById<TextView>(R.id.from).text = state.from
                    card.findViewById<TextView>(R.id.to).text = state.to
                    card.findViewById<TextView>(R.id.days).text = "${state.days} Tage"
                    card.findViewById<TextView>(R.id.weeks).text = "${state.week} Wochen"
                    card.findViewById<TextView>(R.id.grownDays).text = "${state.toDaysSinceStart} Tage"
                    card.findViewById<TextView>(R.id.grownWeeks).text = "${state.toWeekSinceStart} Wochen"
                    statesContainer?.addView(card)
                }
            }

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = PlantTask.getImage(this.activity, plant.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }
}