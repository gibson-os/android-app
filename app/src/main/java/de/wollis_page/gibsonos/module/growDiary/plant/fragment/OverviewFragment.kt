package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.HeadlineBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ImageBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.RangeBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.SubheadlineBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TextBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TitleBuilder
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.module.growDiary.index.dto.Plant
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.feed.Additive
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import kotlin.math.round
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Climate as PlantClimate

class OverviewFragment: AbstractOverviewFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.reloadOverviewModel()
        }
    }

    override fun loadOverviewModel() {
        this.activity.runTask({
            val plant = PlantTask.get(this.activity, this.fragmentsArguments["plantId"].toString().toLong())
            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = PlantTask.getImage(this.activity, plant.id, imageView?.width)
            val viewModel = this.viewModel

            this.activity.runOnUiThread {
                viewModel.addItem(TitleBuilder(plant.name))
                viewModel.addItem(HeadlineBuilder(plant.name))
                viewModel.addItem(TextBuilder(plant.seed.name) {
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
                })
                viewModel.addItem(ImageBuilder(image))

                if (plant.state != null) {
                    viewModel.addItem(TextBuilder(plant.state?.grownAt.toString(), this.getString(R.string.grow_diary_plant_grown_at)))
                    viewModel.addItem(TextBuilder(plant.state?.to.toString(), plant.state?.title.toString()))
                    viewModel.addItem(TextBuilder(
                        "Tag ${plant.state?.toDaysSinceStart} (Woche ${plant.state?.toWeekSinceStart})",
                        this.getString(R.string.grow_diary_plant_duration),
                    ))

                    if ((plant.maxRemainingGrowingDays ?: 0) > 0) {
                        viewModel.addItem(RangeBuilder(
                            plant.minRemainingGrowingDays,
                            plant.maxRemainingGrowingDays,
                            "${plant.minRemainingGrowingDays} - ${plant.maxRemainingGrowingDays} Tage (${plant.minRemainingGrowingWeeks} - ${plant.maxRemainingGrowingWeeks} Wochen)",
                            "${plant.maxRemainingGrowingDays} Tage (${plant.maxRemainingGrowingWeeks} Wochen)",
                            this.getString(R.string.grow_diary_plant_remaining_time_manufacture),
                        ))
                    }

                    if ((plant.maxRemainingGrowingDaysOtherPlants ?: 0) > 0) {
                        viewModel.addItem(RangeBuilder(
                            plant.minRemainingGrowingDaysOtherPlants,
                            plant.maxRemainingGrowingDaysOtherPlants,
                            "${plant.minRemainingGrowingDaysOtherPlants} - ${plant.maxRemainingGrowingDaysOtherPlants} Tage (${plant.minRemainingGrowingWeeksOtherPlants} - ${plant.maxRemainingGrowingWeeksOtherPlants} Wochen)",
                            "${plant.maxRemainingGrowingDaysOtherPlants} Tage (${plant.maxRemainingGrowingWeeksOtherPlants} Wochen)",
                            this.getString(R.string.grow_diary_plant_remaining_time_other_plants),
                        ))
                    }

                    val currentSetup = plant.currentSetup

                    if (currentSetup != null) {
                        viewModel.addItem(TextBuilder(
                            currentSetup.setup.name,
                            this.getString(R.string.grow_diary_plant_current_setup),
                        ) {
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
                        })
                        viewModel.addItem(TextBuilder(currentSetup.from, this.getString(R.string.since)))
                    }
                }

                val harvestedWet = plant.harvestedWet ?: 0F
                val harvestedDry = plant.harvestedDry ?: 0F

                if (harvestedWet > 0F) {
                    viewModel.addItem(TextBuilder(
                        "$harvestedWet g",
                        this.getString(R.string.grow_diary_plant_harvested_wet),
                    ))
                }

                if (harvestedDry > 0F) {
                    viewModel.addItem(TextBuilder(
                        "$harvestedDry g",
                        this.getString(R.string.grow_diary_plant_harvested_dry),
                    ))
                    viewModel.addItem(TextBuilder(
                        "1:" + (round((harvestedWet / harvestedDry) * 100) / 100),
                        this.getString(R.string.grow_diary_plant_harvested_ratio),
                    ))
                }

                plant.lastClimates?.let { this.addLastClimatesView(it) }
                plant.lastFeed?.let { this.addLastFeedView(it) }
                plant.lastMilestone?.let { this.addLastMilestoneView(it) }
                plant.sumFeed?.let { this.addSumFeedView(it) }
                plant.climates?.let { this.addClimatesView(it) }
                this.addStateViews(plant)
            }
        })
    }

    private fun addStateViews(plant: Plant) {
        plant.states?.forEach { key, state ->
            this.viewModel.addItem(HeadlineBuilder(state.title))
            this.viewModel.addItem(TextBuilder( state.to!!, state.from!!))
            this.viewModel.addItem(TextBuilder(
                "Tag ${state.days} (Woche ${state.week})",
                this.getString(R.string.grow_diary_plant_duration),
            ))
            this.viewModel.addItem(TextBuilder(
                "Tag ${state.toDaysSinceStart} (Woche ${state.toWeekSinceStart})",
                this.getString(R.string.grow_diary_plant_duration_since_grown),
            ))
        }
    }

    private fun addClimatesView(climates: List<PlantClimate>) {
        this.viewModel.addItem(HeadlineBuilder(this.getString(R.string.grow_diary_plant_climates)))

        climates.forEach { climate ->
            this.viewModel.addItem(SubheadlineBuilder(climate.measuringPoint))

            if (climate.minTemperature !== null) {
                this.viewModel.addItem(RangeBuilder(
                    climate.minTemperature,
                    climate.maxTemperature,
                    "${climate.minTemperature}°C - ${climate.maxTemperature}°C",
                    "${climate.maxTemperature}°C",
                    this.getString(R.string.grow_diary_plant_climate_temperature),
                ))
            }

            if (climate.minRelativeHumidity !== null) {
                this.viewModel.addItem(RangeBuilder(
                    climate.minRelativeHumidity,
                    climate.maxRelativeHumidity,
                    "${climate.minRelativeHumidity}% - ${climate.maxRelativeHumidity}%",
                    "${climate.maxRelativeHumidity}%",
                    this.getString(R.string.grow_diary_plant_climate_relative_humidity),
                ))
            }

            if (climate.minAirPressure !== null) {
                this.viewModel.addItem(RangeBuilder(
                    climate.minAirPressure,
                    climate.maxAirPressure,
                    "${climate.minAirPressure}% - ${climate.maxAirPressure}%",
                    "${climate.maxAirPressure}%",
                    this.getString(R.string.grow_diary_plant_climate_air_pressure),
                ))
            }

            if (climate.minLeafTemperature !== null) {
                this.viewModel.addItem(RangeBuilder(
                    climate.minLeafTemperature,
                    climate.maxLeafTemperature,
                    "${climate.minLeafTemperature}% - ${climate.maxLeafTemperature}%",
                    "${climate.maxLeafTemperature}%",
                    this.getString(R.string.grow_diary_plant_climate_leaf_temperature),
                ))
            }
        }
    }

    private fun addLastClimatesView(climates: List<Climate>) {
        this.viewModel.addItem(HeadlineBuilder(this.getString(R.string.grow_diary_plant_climates_last)))
        climates.forEach { climate ->
            this.viewModel.addItem(SubheadlineBuilder(climate.measuringPoint))
            this.viewModel.addItem(TextBuilder(climate.added, this.getString(R.string.date)))

            if (climate.temperature !== null) {
                this.viewModel.addItem(TextBuilder(
                    "${climate.temperature}°C",
                    this.getString(R.string.grow_diary_plant_climate_temperature),
                ))
            }

            if (climate.relativeHumidity !== null) {
                this.viewModel.addItem(TextBuilder(
                    "${climate.relativeHumidity}%",
                    this.getString(R.string.grow_diary_plant_climate_relative_humidity),
                ))
            }

            if (climate.airPressure !== null) {
                this.viewModel.addItem(TextBuilder(
                    "${climate.airPressure}hPa",
                    this.getString(R.string.grow_diary_plant_climate_air_pressure),
                ))
            }

            if (climate.leafTemperature !== null) {
                this.viewModel.addItem(TextBuilder(
                    "${climate.leafTemperature}°C",
                    this.getString(R.string.grow_diary_plant_climate_leaf_temperature),
                ))
            }
        }
    }

    private fun addLastFeedView(lastFeed: Feed) {
        this.viewModel.addItem(HeadlineBuilder(this.getString(R.string.grow_diary_plant_feed_last)))
        this.viewModel.addItem(TextBuilder(lastFeed.added, this.getString(R.string.date)))
        this.viewModel.addItem(TextBuilder("${lastFeed.milliliter} ml", this.getString(R.string.grow_diary_plant_feed_milliliter)))

        if (lastFeed.ph !== null) {
            this.viewModel.addItem(TextBuilder(lastFeed.ph.toString(), this.getString(R.string.grow_diary_plant_feed_ph)))
        }

        this.addAdditivesViews(lastFeed.additives)
    }

    private fun addSumFeedView(feedSum: Feed) {
        this.viewModel.addItem(HeadlineBuilder(this.getString(R.string.grow_diary_plant_feed_sum)))
        this.viewModel.addItem(TextBuilder("${feedSum.milliliter} ml", this.getString(R.string.grow_diary_plant_feed_milliliter)))

        this.addAdditivesViews(feedSum.additives)
    }

    private fun addAdditivesViews(additives: List<Additive>) {
//        additives.forEach {
//            this.activity.runTask({
//                val image = FertilizerTask.getImage(
//                    this.activity,
//                    it.fertilizer.fertilizer.id,
//                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
//                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
//                )
//
//                this.activity.runOnUiThread {
//                    additiveView.findViewById<ImageView>(R.id.image).setImageBitmap(image)
//                    additiveView.findViewById<TextView>(R.id.additive).text =
//                        it.milliliter.toString() + " ml " + it.fertilizer.fertilizer.name
//                    lastFeedContainer.addView(additiveView)
//                }
//            })
//        }
    }

    private fun addLastMilestoneView(milestone: Milestone) {
        this.viewModel.addItem(HeadlineBuilder(this.getString(R.string.grow_diary_plant_milestone_last)))
        this.viewModel.addItem(TextBuilder(milestone.added, this.getString(R.string.date)))
        this.viewModel.addItem(TextBuilder(milestone.value, milestone.title))
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