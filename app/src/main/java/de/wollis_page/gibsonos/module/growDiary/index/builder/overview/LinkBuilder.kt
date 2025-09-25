package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class LinkBuilder(
    private val url: String?,
    private val label: String? = null,
): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        if (this.url === null) {
            return
        }

        val overviewItem = inflater.inflate(
            R.layout.grow_diary_overview_link,
            view.findViewById(android.R.id.content),
            false
        )

        val label = this.label
        val labelView = overviewItem.findViewById<TextView>(R.id.label)

        labelView.visibility = View.GONE

        if (label !== null) {
            labelView.text = label
            labelView.visibility = View.VISIBLE
        }

        overviewItem.findViewById<TextView>(R.id.url).text = this.url
        layout.addView(overviewItem)
    }
}