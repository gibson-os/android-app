package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class HeadlineBuilder(private val text: String): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        val overviewItem = inflater.inflate(
            R.layout.grow_diary_overview_headline,
            view.findViewById(android.R.id.content),
            false
        )

        overviewItem.findViewById<TextView>(R.id.title).text = this.text
        layout.addView(overviewItem)
    }
}