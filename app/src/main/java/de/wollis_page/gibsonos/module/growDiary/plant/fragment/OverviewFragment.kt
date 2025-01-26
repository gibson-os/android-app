package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.module.growDiary.index.dto.Plant
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.feed.Additive
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask

class OverviewFragment: GibsonOsFragment() {
    private lateinit var inflater: LayoutInflater
    private lateinit var overviewContainer: LinearLayout

    override fun getContentView() = R.layout.grow_diary_plant_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val plant = PlantTask.get(this.activity, this.fragmentsArguments["plantId"].toString().toLong())
            this.activity.setTitle(plant.name)

            this.overviewContainer = this.requireView().findViewById(R.id.overviewContiner)
            this.inflater = LayoutInflater.from(this.activity)

            this.view?.findViewById<TextView>(R.id.name)?.text = plant.name
            this.view?.findViewById<TextView>(R.id.seed)?.text = plant.seed.name

            this.activity.runOnUiThread {
                if (plant.state != null) {
                    this.overviewContainer.addView(this.getOverviewItem(
                        R.string.grow_diary_plant_grown_at,
                        plant.state?.grownAt.toString(),
                    ))
                    this.overviewContainer.addView(this.getOverviewItem(
                        plant.state?.title.toString(),
                        plant.state?.to.toString(),
                    ))
                    this.overviewContainer.addView(this.getOverviewItem(
                        R.string.grow_diary_plant_duration,
                        "${plant.state?.toDaysSinceStart} Tage (${plant.state?.toWeekSinceStart} Wochen)",
                    ))
                    this.overviewContainer.addView(this.getOverviewItem(
                        R.string.grow_diary_plant_remaining_time,
                        "${plant.minRemainingGrowingDays} - ${plant.maxRemainingGrowingDays} Tage (${plant.minRemainingGrowingWeeks} - ${plant.maxRemainingGrowingWeeks} Wochen)",
                    ))
                }

                plant.lastClimate?.let { this.addClimateView(it, R.string.grow_diary_plant_climate_last) }
                plant.lastFeed?.let { this.addLastFeedView(it) }
                plant.lastMilestone?.let { this.addLastMilestoneView(it) }
                plant.sumFeed?.let { this.addSumFeedView(it) }
                plant.minTemperature?.let { this.addClimateView(it, R.string.grow_diary_plant_climate_min_temperature) }
                plant.maxTemperature?.let { this.addClimateView(it, R.string.grow_diary_plant_climate_max_temperature) }
                plant.minRelativeHumidity?.let { this.addClimateView(it, R.string.grow_diary_plant_climate_min_relative_humidity) }
                plant.maxRelativeHumidity?.let { this.addClimateView(it, R.string.grow_diary_plant_climate_max_relative_humidity) }
                this.addStateViews(plant)
            }

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = PlantTask.getImage(this.activity, plant.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }

    private fun addStateViews(plant: Plant) {
        plant.states?.forEach { key, state ->
            val stateItemView = this.inflater.inflate(
                R.layout.grow_diary_plant_overview_container,
                this.view?.findViewById(android.R.id.content),
                false
            )

            stateItemView.findViewById<TextView>(R.id.title).text = state.title

            val stateContainer = stateItemView.findViewById<LinearLayout>(R.id.container)
            stateContainer.addView(this.getOverviewItem(state.from!!, state.to!!))
            stateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_duration,
                "${state.days} Tage (${state.week} Wochen)",
            ))
            stateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_duration_since_grown,
                "${state.toDaysSinceStart} Tage (${state.toWeekSinceStart} Wochen)"
            ))

            this.overviewContainer.addView(stateItemView)
        }
    }

    private fun addClimateView(climate: Climate, title: Int) {
        val climateItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            this.view?.findViewById(android.R.id.content),
            false
        )

        val climateContainer = climateItemView.findViewById<LinearLayout>(R.id.container)
        climateContainer.findViewById<TextView>(R.id.title).setText(title)
        climateContainer.addView(this.getOverviewItem(R.string.date, climate.added))

        if (climate.temperature !== null) {
            climateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_climate_temperature,
                "${climate.temperature}°C",
            ))
        }

        if (climate.relativeHumidity !== null) {
            climateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_climate_relative_humidity,
                "${climate.relativeHumidity}%",
            ))
        }

        if (climate.airPressure !== null) {
            climateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_climate_air_pressure,
                "${climate.airPressure}hPa",
            ))
        }

        if (climate.leafTemperature !== null) {
            climateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_climate_leaf_temperature,
                "${climate.leafTemperature}°C",
            ))
        }

        this.overviewContainer.addView(climateItemView)
    }

    private fun addLastFeedView(lastFeed: Feed) {
        val contentView = this.view?.findViewById<ViewGroup>(android.R.id.content)
        val lastFeedItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            contentView,
            false,
        )

        val lastFeedContainer = lastFeedItemView.findViewById<LinearLayout>(R.id.container)
        lastFeedContainer.findViewById<TextView>(R.id.title).setText(R.string.grow_diary_plant_feed_last)
        lastFeedContainer.addView(this.getOverviewItem(R.string.date, lastFeed.added))
        lastFeedContainer.addView(this.getOverviewItem(R.string.grow_diary_plant_feed_milliliter, "${lastFeed.milliliter}ml"))

        if (lastFeed.ph !== null) {
            lastFeedContainer.addView(this.getOverviewItem(R.string.grow_diary_plant_feed_ph, lastFeed.ph.toString()))
        }

        this.addAdditivesViews(lastFeed.additives, lastFeedItemView)

        this.overviewContainer.addView(lastFeedItemView)
    }

    private fun addSumFeedView(feedSum: Feed) {
        val sumFeedItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            this.view?.findViewById(android.R.id.content),
            false
        )

        val sumFeedContainer = sumFeedItemView.findViewById<LinearLayout>(R.id.container)
        sumFeedContainer.findViewById<TextView>(R.id.title).setText(R.string.grow_diary_plant_feed_sum)
        sumFeedContainer.addView(this.getOverviewItem(R.string.grow_diary_plant_feed_milliliter, "${feedSum.milliliter}ml"))

        this.addAdditivesViews(feedSum.additives, sumFeedItemView)

        this.overviewContainer.addView(sumFeedItemView)
    }

    private fun addAdditivesViews(additives: List<Additive>, itemView: View) {
        val contentView = this.view?.findViewById<ViewGroup>(android.R.id.content)
        val lastFeedContainer = itemView.findViewById<LinearLayout>(R.id.container)

        additives.forEach {
            val additiveView = this.inflater.inflate(
                R.layout.grow_diary_plant_feed_additive,
                contentView,
                false,
            )

            this.activity.runTask({
                val image = FertilizerTask.getImage(
                    this.activity,
                    it.fertilizer.fertilizer.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
                )

                this.activity.runOnUiThread {
                    additiveView.findViewById<ImageView>(R.id.image).setImageBitmap(image)
                    additiveView.findViewById<TextView>(R.id.additive).text =
                        it.milliliter.toString() + "ml " + it.fertilizer.fertilizer.name
                    lastFeedContainer.addView(additiveView)
                }
            })
        }
    }

    private fun addLastMilestoneView(milestone: Milestone) {
        val milestoneItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            this.view?.findViewById(android.R.id.content),
            false
        )

        val milestoneContainer = milestoneItemView.findViewById<LinearLayout>(R.id.container)
        milestoneContainer.findViewById<TextView>(R.id.title).setText(R.string.grow_diary_plant_milestone_last)
        milestoneContainer.addView(this.getOverviewItem(R.string.date, milestone.added))
        milestoneContainer.addView(this.getOverviewItem(milestone.title, milestone.value))

        this.overviewContainer.addView(milestoneItemView)
    }

    private fun getOverviewItem(label: Int, value: String) = this.getOverviewItem(this.getString(label), value)

    private fun getOverviewItem(label: String, value: String): View {
        val overviewItem = this.inflater.inflate(
            R.layout.grow_diary_overview_item,
            this.view?.findViewById(android.R.id.content),
            false
        )

        overviewItem.findViewById<TextView>(R.id.label).text = label
        overviewItem.findViewById<TextView>(R.id.value).text = value

        return overviewItem
    }
}