package de.wollis_page.gibsonos.module.growDiary.seed.fragment

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.SeedTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class OverviewFragment: AbstractOverviewFragment() {
    override fun getContentView() = R.layout.grow_diary_seed_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val seed = SeedTask.get(this.activity, this.fragmentsArguments["seedId"].toString().toLong())

            this.activity.runOnUiThread {
                this.activity.setTitle(seed.name)

                this.view?.findViewById<TextView>(R.id.name)?.text = seed.name

                val manufactureText = this.view?.findViewById<TextView>(R.id.manufacture)
                val manufactureString = SpannableString(seed.manufacture!!.name)
                manufactureText?.setOnClickListener {
                    ActivityLauncherService.startActivity(
                        this.activity,
                        "growDiary",
                        "manufacture",
                        "index",
                        mapOf(
                            "manufactureId" to seed.manufacture!!.id,
                            GibsonOsActivity.SHORTCUT_KEY to Shortcut(
                                "growDiary",
                                "manufacture",
                                "index",
                                seed.manufacture!!.name,
                                "icon_hemp",
                                mutableMapOf(
                                    "manufactureId" to seed.manufacture!!.id,
                                    "name" to seed.manufacture!!.name,
                                )
                            ),
                        )
                    )
                }
                manufactureString.setSpan(UnderlineSpan(), 0, manufactureString.length, 0)
                manufactureText?.text = manufactureString

                this.addOverviewItem(R.string.grow_diary_seed_type, seed.type)

                if ((seed.price ?: 0) > 0) {
                    this.addOverviewItem(
                        R.string.grow_diary_seed_price,
                        ((seed.price ?: 0) / 100).toString() + " €"
                    )
                }

                if ((seed.maxGrowingDays ?: 0) > 0) {
                    this.addOverviewRangeItem(
                        R.string.grow_diary_seed_growing_days,
                        seed.minGrowingDays,
                        seed.maxGrowingDays,
                        "${seed.minGrowingDays} - ${seed.maxGrowingDays} Tage (${seed.minGrowingWeeks} - ${seed.maxGrowingWeeks} Wochen)",
                        "${seed.maxGrowingDays} Tage (${seed.maxGrowingWeeks} Wochen)",
                    )
                }

                if ((seed.maxFloweringDays ?: 0) > 0) {
                    this.addOverviewRangeItem(
                        R.string.grow_diary_seed_flowering_days,
                        seed.minFloweringDays,
                        seed.maxFloweringDays,
                        "${seed.minFloweringDays} - ${seed.maxFloweringDays} Tage (${seed.minFloweringWeeks} - ${seed.maxFloweringWeeks} Wochen)",
                        "${seed.maxFloweringDays} Tage (${seed.maxFloweringWeeks} Wochen)",
                    )
                }

                if ((seed.plantMaxDays ?: 0) > 0) {
                    this.addOverviewRangeItem(
                        R.string.grow_diary_seed_plant_days,
                        seed.plantMinDays,
                        seed.plantMaxDays,
                        "${seed.plantMinDays} - ${seed.plantMaxDays} Tage (${seed.plantMinWeeks} - ${seed.plantMaxWeeks} Wochen)",
                        "${seed.plantMaxDays} Tage (${seed.plantMaxWeeks} Wochen)",
                    )
                }

                if ((seed.maxHeight ?: 0) > 0) {
                    this.addOverviewRangeItem(
                        R.string.grow_diary_seed_height,
                        seed.minHeight,
                        seed.maxHeight,
                        "${seed.minHeight} cm - ${seed.maxHeight} cm",
                        "${seed.maxHeight} cm",
                    )
                }
            }

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = SeedTask.image(this.activity, seed.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }
}