package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class RangeBuilder(
    val min: Any?,
    val max: Any?,
    val differentValue: String,
    val sameValue: String,
    val label: String? = null,
): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        var value = this.differentValue

        if (this.min == this.max) {
            value = this.sameValue
        }

        TextBuilder(value, this.label).build(
            view,
            inflater,
            layout,
            activity,
        )
    }
}