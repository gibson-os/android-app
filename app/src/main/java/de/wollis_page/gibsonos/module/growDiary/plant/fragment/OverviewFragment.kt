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

//            if (plant.grownAt == null) {
//                this.view?.findViewById<LinearLayout>(R.id.grownAtContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.grownDaysContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.minGrownDaysContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.maxGrownDaysContainer)?.visibility = View.INVISIBLE
//            }
//
//            this.view?.findViewById<TextView>(R.id.grownAt)?.text = plant.grownAt
//            this.view?.findViewById<TextView>(R.id.grownDays)?.text = plant.grownDaysFromNow.toString() + " Tage (" + (plant.grownDaysFromNow?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + ". Woche)"
//            this.view?.findViewById<TextView>(R.id.minGrownDays)?.text = plant.seed.minGrowingDays.toString() + " Tage"
//            this.view?.findViewById<TextView>(R.id.maxGrownDays)?.text = plant.seed.maxGrowingDays.toString() + " Tage"
//
//            if (plant.germinatedAt == null) {
//                this.view?.findViewById<LinearLayout>(R.id.germinatedAtContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.germinatedDaysContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.germinatedDaysFromStartContainer)?.visibility = View.INVISIBLE
//            }
//
//            this.view?.findViewById<TextView>(R.id.germinatedAt)?.text = plant.germinatedAt
//            this.view?.findViewById<TextView>(R.id.germinatedDays)?.text = plant.germinatedDays.toString() + " Tage (" + (plant.germinatedDays?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + ". Woche)"
//            this.view?.findViewById<TextView>(R.id.germinatedDaysFromStart)?.text = plant.germinatedDaysFromStart.toString() + " Tage (" + (plant.germinatedDaysFromStart?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + ". Woche)"
//
//            if (plant.seed.minFloweringDays == null) {
//                this.view?.findViewById<LinearLayout>(R.id.minFloweringDaysContainer)?.visibility = View.INVISIBLE
//            }
//
//            if (plant.seed.maxFloweringDays == null) {
//                this.view?.findViewById<LinearLayout>(R.id.maxFloweringDaysContainer)?.visibility = View.INVISIBLE
//            }
//
//            if (plant.floweringAt == null) {
//                this.view?.findViewById<LinearLayout>(R.id.floweringAtContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.floweringDaysContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.floweringDaysFromStartContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.minFloweringDaysContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.maxFloweringDaysContainer)?.visibility = View.INVISIBLE
//            }
//
//            this.view?.findViewById<TextView>(R.id.floweringAt)?.text = plant.floweringAt
//            this.view?.findViewById<TextView>(R.id.floweringDays)?.text = plant.floweringDays.toString() + " Tage (" + (plant.floweringDays?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + ". Woche)"
//            this.view?.findViewById<TextView>(R.id.floweringDaysFromStart)?.text = plant.floweringDaysFromStart.toString() + " Tage (" + (plant.floweringDaysFromStart?.div(7))?.toFloat()?.let { floor(it+1).toInt().toString() } + ". Woche)"
//            this.view?.findViewById<TextView>(R.id.minFloweringDays)?.text = plant.seed.minFloweringDays.toString() + " Tage"
//            this.view?.findViewById<TextView>(R.id.maxFloweringDays)?.text = plant.seed.maxFloweringDays.toString() + " Tage"
//
//            if (plant.harvestedAt == null) {
//                this.view?.findViewById<LinearLayout>(R.id.harvestedAtContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.harvestedWetContainer)?.visibility = View.INVISIBLE
//            }
//
//            this.view?.findViewById<TextView>(R.id.harvestedAt)?.text = plant.harvestedAt
//            this.view?.findViewById<TextView>(R.id.harvestedWet)?.text = plant.harvestedWet.toString() + "g"
//
//            if (plant.dryingAt == null) {
//                this.view?.findViewById<LinearLayout>(R.id.dryingAtContainer)?.visibility = View.INVISIBLE
//                this.view?.findViewById<LinearLayout>(R.id.harvestedDryContainer)?.visibility = View.INVISIBLE
//            }
//
//            this.view?.findViewById<TextView>(R.id.dryingAt)?.text = plant.dryingAt.toString()
//            this.view?.findViewById<TextView>(R.id.harvestedDry)?.text = plant.harvestedDry.toString() + "g"

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = PlantTask.getImage(this.activity, plant.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }
}