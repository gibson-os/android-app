package de.wollis_page.gibsonos.module.growDiary.seed.fragment

import android.widget.ImageView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.HeadlineBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ImageBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ManufactureBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.RangeBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TextBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TitleBuilder
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.SeedTask

class OverviewFragment: AbstractOverviewFragment() {
    override fun loadOverviewModel() {
        this.activity.runTask({
            val seed = SeedTask.get(this.activity, this.fragmentsArguments["seedId"].toString().toLong())
            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = SeedTask.image(this.activity, seed.id, imageView?.width)
            val viewModel = this.viewModel

            this.activity.runOnUiThread {
                viewModel.addItem(TitleBuilder(seed.name))
                viewModel.addItem(HeadlineBuilder(seed.name))
                viewModel.addItem(ManufactureBuilder(seed.manufacture))
                viewModel.addItem(TextBuilder(seed.type, this.getString(R.string.grow_diary_seed_type)))

                if ((seed.price ?: 0) > 0) {
                    viewModel.addItem(TextBuilder(
                        ((seed.price ?: 0) / 100).toString() + " €",
                        this.getString(R.string.grow_diary_seed_price),
                    ))
                }

                if ((seed.maxGrowingDays ?: 0) > 0) {
                    viewModel.addItem(RangeBuilder(
                        seed.minGrowingDays,
                        seed.maxGrowingDays,
                        "${seed.minGrowingDays} - ${seed.maxGrowingDays} Tage (${seed.minGrowingWeeks} - ${seed.maxGrowingWeeks} Wochen)",
                        "${seed.maxGrowingDays} Tage (${seed.maxGrowingWeeks} Wochen)",
                        this.getString(R.string.grow_diary_seed_growing_days),
                    ))
                }

                if ((seed.maxFloweringDays ?: 0) > 0) {
                    viewModel.addItem(RangeBuilder(
                        seed.minFloweringDays,
                        seed.maxFloweringDays,
                        "${seed.minFloweringDays} - ${seed.maxFloweringDays} Tage (${seed.minFloweringWeeks} - ${seed.maxFloweringWeeks} Wochen)",
                        "${seed.maxFloweringDays} Tage (${seed.maxFloweringWeeks} Wochen)",
                        this.getString(R.string.grow_diary_seed_flowering_days),
                    ))
                }

                if ((seed.plantMaxDays ?: 0) > 0) {
                    viewModel.addItem(RangeBuilder(
                        seed.plantMinDays,
                        seed.plantMaxDays,
                        "${seed.plantMinDays} - ${seed.plantMaxDays} Tage (${seed.plantMinWeeks} - ${seed.plantMaxWeeks} Wochen)",
                        "${seed.plantMaxDays} Tage (${seed.plantMaxWeeks} Wochen)",
                        this.getString(R.string.grow_diary_seed_plant_days),
                    ))
                }

                if ((seed.maxHeight ?: 0) > 0) {
                    viewModel.addItem(RangeBuilder(
                        seed.minHeight,
                        seed.maxHeight,
                        "${seed.minHeight} cm - ${seed.maxHeight} cm",
                        "${seed.maxHeight} cm",
                        this.getString(R.string.grow_diary_seed_height),
                    ))
                }

                viewModel.addItem(ImageBuilder(image))
            }
        })
    }
}