package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.module.growDiary.index.dto.Plant
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.feed.Additive
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import kotlin.math.round
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Climate as PlantClimate

class OverviewFragment: AbstractOverviewFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>
    lateinit var overviewContainerLayout: LinearLayout

    override fun getContentView() = R.layout.grow_diary_plant_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.load()
        }

        this.load()
    }

    private fun load() {
        this.activity.runTask({
            val plant = PlantTask.get(this.activity, this.fragmentsArguments["plantId"].toString().toLong())
            this.activity.setTitle(plant.name)

            this.overviewContainerLayout = this.getOverviewContainer()

            this.activity.runOnUiThread {
                this.view?.findViewById<TextView>(R.id.name)?.text = plant.name

                val seedText = this.view?.findViewById<TextView>(R.id.seed)
                val seedString = SpannableString(plant.seed.name)

                seedString.setSpan(UnderlineSpan(), 0, seedString.length, 0)
                seedText?.text = seedString
                seedText?.setOnClickListener {
                    ActivityLauncherService.startActivity(
                        this.activity,
                        "growDiary",
                        "seed",
                        "index",
                        mapOf(
                            "seedId" to plant.seed.id,
                            GibsonOsActivity.SHORTCUT_KEY to Shortcut(
                                "growDiary",
                                "seed",
                                "index",
                                plant.seed.name,
                                "icon_seed",
                                mutableMapOf(
                                    "seedId" to plant.seed.id,
                                    "name" to plant.seed.name,
                                )
                            ),
                        )
                    )
                }

                if (plant.state != null) {
                    this.addOverviewItem(R.string.grow_diary_plant_grown_at, plant.state?.grownAt.toString())
                    this.addOverviewItem(plant.state?.title.toString(), plant.state?.to.toString())
                    this.addOverviewItem(
                        R.string.grow_diary_plant_duration,
                        "Tag ${plant.state?.toDaysSinceStart} (Woche ${plant.state?.toWeekSinceStart})",
                    )

                    if ((plant.maxRemainingGrowingDays ?: 0) > 0) {
                        this.addOverviewRangeItem(
                            R.string.grow_diary_plant_remaining_time_manufacture,
                            plant.minRemainingGrowingDays,
                            plant.maxRemainingGrowingDays,
                            "${plant.minRemainingGrowingDays} - ${plant.maxRemainingGrowingDays} Tage (${plant.minRemainingGrowingWeeks} - ${plant.maxRemainingGrowingWeeks} Wochen)",
                            "${plant.maxRemainingGrowingDays} Tage (${plant.maxRemainingGrowingWeeks} Wochen)",
                        )
                    }

                    if ((plant.maxRemainingGrowingDaysOtherPlants ?: 0) > 0) {
                        this.addOverviewRangeItem(
                            R.string.grow_diary_plant_remaining_time_manufacture,
                            plant.minRemainingGrowingDaysOtherPlants,
                            plant.maxRemainingGrowingDaysOtherPlants,
                            "${plant.minRemainingGrowingDaysOtherPlants} - ${plant.maxRemainingGrowingDaysOtherPlants} Tage (${plant.minRemainingGrowingWeeksOtherPlants} - ${plant.maxRemainingGrowingWeeksOtherPlants} Wochen)",
                            "${plant.maxRemainingGrowingDaysOtherPlants} Tage (${plant.maxRemainingGrowingWeeksOtherPlants} Wochen)",
                        )
                    }
                }

                val currentSetup = plant.currentSetup

                if (currentSetup != null) {
                    val currentSetupView = this.getOverviewItem(
                        R.string.grow_diary_plant_current_setup,
                        currentSetup.setup.name,
                    )
                    val currentSetupText = currentSetupView.findViewById<TextView>(R.id.value)
                    val currentSetupString = SpannableString(currentSetup.setup.name)

                    currentSetupString.setSpan(UnderlineSpan(), 0, currentSetupString.length, 0)
                    currentSetupText.text = currentSetupString

                    currentSetupText.setOnClickListener {
                        ActivityLauncherService.startActivity(
                            this.activity,
                            "growDiary",
                            "setup",
                            "index",
                            mapOf(
                                "setupId" to currentSetup.setup.id,
                                GibsonOsActivity.SHORTCUT_KEY to Shortcut(
                                    "growDiary",
                                    "setup",
                                    "index",
                                    currentSetup.setup.name,
                                    "icon_grow_setup",
                                    mutableMapOf(
                                        "setupId" to currentSetup.setup.id,
                                        "name" to currentSetup.setup.name,
                                    )
                                ),
                            )
                        )
                    }
                    this.overviewContainerLayout.addView(currentSetupView)
                    this.addOverviewItem(R.string.since, currentSetup.from)
                }

                val harvestedWet = plant.harvestedWet ?: 0F
                val harvestedDry = plant.harvestedDry ?: 0F

                if (harvestedWet > 0F) {
                    this.addOverviewItem(R.string.grow_diary_plant_harvested_wet, "$harvestedWet g")
                }

                if (harvestedDry > 0F) {
                    this.addOverviewItem(R.string.grow_diary_plant_harvested_dry, "$harvestedDry g")
                    this.addOverviewItem(R.string.grow_diary_plant_harvested_ratio, "1:" + (round((harvestedWet / harvestedDry) * 100) / 100))
                }

                plant.lastClimates?.let { this.addLastClimatesView(it) }
                plant.lastFeed?.let { this.addLastFeedView(it) }
                plant.lastMilestone?.let { this.addLastMilestoneView(it) }
                plant.sumFeed?.let { this.addSumFeedView(it) }
                plant.climates?.let { this.addClimatesView(it) }
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
            val stateContainer = stateItemView.findViewById<LinearLayout>(R.id.container)

            stateContainer.addView(this.getOverviewHeadline(state.title))
            stateContainer.addView(this.getOverviewItem(state.from!!, state.to!!))
            stateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_duration,
                "Tag ${state.days} (Woche ${state.week})",
            ))
            stateContainer.addView(this.getOverviewItem(
                R.string.grow_diary_plant_duration_since_grown,
                "Tag ${state.toDaysSinceStart} (Woche ${state.toWeekSinceStart})"
            ))

            this.overviewContainerLayout.addView(stateItemView)
        }
    }

    private fun addClimatesView(climates: List<PlantClimate>) {
        val climateItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            this.view?.findViewById(android.R.id.content),
            false
        )
        val climateContainer = climateItemView.findViewById<LinearLayout>(R.id.container)

        climateContainer.addView(this.getOverviewHeadline(R.string.grow_diary_plant_climates))
        climates.forEach { climate ->
            climateContainer.addView(this.getOverviewSubeadline(climate.measuringPoint))

            if (climate.minTemperature !== null) {
                climateContainer.addView(this.getOverviewRangeItem(
                    R.string.grow_diary_plant_climate_temperature,
                    climate.minTemperature,
                    climate.maxTemperature,
                    "${climate.minTemperature}°C - ${climate.maxTemperature}°C",
                    "${climate.maxTemperature}°C",
                ))
            }

            if (climate.minRelativeHumidity !== null) {
                climateContainer.addView(this.getOverviewRangeItem(
                    R.string.grow_diary_plant_climate_relative_humidity,
                    climate.minRelativeHumidity,
                    climate.maxRelativeHumidity,
                    "${climate.minRelativeHumidity}% - ${climate.maxRelativeHumidity}%",
                    "${climate.maxRelativeHumidity}%",
                ))
            }

            if (climate.minAirPressure !== null) {
                climateContainer.addView(this.getOverviewRangeItem(
                    R.string.grow_diary_plant_climate_air_pressure,
                    climate.minAirPressure,
                    climate.maxAirPressure,
                    "${climate.minAirPressure}% - ${climate.maxAirPressure}%",
                    "${climate.maxAirPressure}%",
                ))
            }

            if (climate.minLeafTemperature !== null) {
                climateContainer.addView(this.getOverviewRangeItem(
                    R.string.grow_diary_plant_climate_leaf_temperature,
                    climate.minLeafTemperature,
                    climate.maxLeafTemperature,
                    "${climate.minLeafTemperature}% - ${climate.maxLeafTemperature}%",
                    "${climate.maxLeafTemperature}%",
                ))
            }
        }

        this.overviewContainerLayout.addView(climateItemView)
    }

    private fun addLastClimatesView(climates: List<Climate>) {
        val climateItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            this.view?.findViewById(android.R.id.content),
            false
        )
        val climateContainer = climateItemView.findViewById<LinearLayout>(R.id.container)

        climateContainer.addView(this.getOverviewHeadline(R.string.grow_diary_plant_climates_last))
        climates.forEach { climate ->
            climateContainer.addView(this.getOverviewSubeadline(climate.measuringPoint))
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
        }

        this.overviewContainerLayout.addView(climateItemView)
    }

    private fun addLastFeedView(lastFeed: Feed) {
        val contentView = this.view?.findViewById<ViewGroup>(android.R.id.content)
        val lastFeedItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            contentView,
            false,
        )

        val lastFeedContainer = lastFeedItemView.findViewById<LinearLayout>(R.id.container)
        lastFeedContainer.addView(this.getOverviewHeadline(R.string.grow_diary_plant_feed_last))
        lastFeedContainer.addView(this.getOverviewItem(R.string.date, lastFeed.added))
        lastFeedContainer.addView(this.getOverviewItem(R.string.grow_diary_plant_feed_milliliter, "${lastFeed.milliliter} ml"))

        if (lastFeed.ph !== null) {
            lastFeedContainer.addView(this.getOverviewItem(R.string.grow_diary_plant_feed_ph, lastFeed.ph.toString()))
        }

        this.addAdditivesViews(lastFeed.additives, lastFeedItemView)

        this.overviewContainerLayout.addView(lastFeedItemView)
    }

    private fun addSumFeedView(feedSum: Feed) {
        val sumFeedItemView = this.inflater.inflate(
            R.layout.grow_diary_plant_overview_container,
            this.view?.findViewById(android.R.id.content),
            false
        )

        val sumFeedContainer = sumFeedItemView.findViewById<LinearLayout>(R.id.container)
        sumFeedContainer.addView(this.getOverviewHeadline(R.string.grow_diary_plant_feed_sum))
        sumFeedContainer.addView(this.getOverviewItem(R.string.grow_diary_plant_feed_milliliter, "${feedSum.milliliter} ml"))

        this.addAdditivesViews(feedSum.additives, sumFeedItemView)

        this.overviewContainerLayout.addView(sumFeedItemView)
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
                        it.milliliter.toString() + " ml " + it.fertilizer.fertilizer.name
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
        milestoneContainer.addView(this.getOverviewHeadline(R.string.grow_diary_plant_milestone_last))
        milestoneContainer.addView(this.getOverviewItem(R.string.date, milestone.added))
        milestoneContainer.addView(this.getOverviewItem(milestone.title, milestone.value))

        this.overviewContainerLayout.addView(milestoneItemView)
    }

    override fun actionButton() = R.layout.base_button_edit

    override fun actionOnClickListener() {
        this.activity.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "index",
                "form",
                mapOf(
                    "task" to "plant",
                    "id" to this.fragmentsArguments["plantId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.editButton)
}