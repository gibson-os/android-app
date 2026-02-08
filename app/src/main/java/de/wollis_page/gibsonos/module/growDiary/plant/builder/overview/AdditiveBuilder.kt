package de.wollis_page.gibsonos.module.growDiary.plant.builder.overview

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.OverviewBuilderInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.feed.Additive
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask

class AdditiveBuilder(
    private val additive: Additive,
    private val width: Int,
): OverviewBuilderInterface {
    private var image: Bitmap? = null

    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        val overviewItem = inflater.inflate(
            R.layout.grow_diary_plant_feed_additive,
            view.findViewById(android.R.id.content),
            false
        )

        activity.runTask({
            this.image = this.image ?: FertilizerTask.image(
                activity,
                this.additive.fertilizer.fertilizer.id,
                this.width,
                this.width,
            )

            activity.runOnUiThread {
                overviewItem.findViewById<ImageView>(R.id.image).setImageBitmap(this.image)
            }
        })

        overviewItem.findViewById<TextView>(R.id.additive).text =
            "${this.additive.milliliter} ${this.additive.fertilizer.fertilizer.formUnit} ${this.additive.fertilizer.fertilizer.name}"
        layout.addView(overviewItem)
    }
}