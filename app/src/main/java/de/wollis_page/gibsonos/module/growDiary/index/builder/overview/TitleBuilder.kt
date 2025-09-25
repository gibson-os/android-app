package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class TitleBuilder(private val title: String): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        activity.setTitle(this.title)
    }
}