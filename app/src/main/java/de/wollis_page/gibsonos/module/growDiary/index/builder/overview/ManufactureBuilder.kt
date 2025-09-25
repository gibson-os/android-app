package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Manufacture
import de.wollis_page.gibsonos.service.ActivityLauncherService

class ManufactureBuilder(private val manufacture: Manufacture?): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        if (this.manufacture === null) {
            return
        }

        val textBuilder = TextBuilder(this.manufacture.name) {
            ActivityLauncherService.startActivity(
                activity,
                "growDiary",
                "manufacture",
                "index",
                mapOf(
                    "manufactureId" to manufacture.id,
                    GibsonOsActivity.SHORTCUT_KEY to Shortcut(
                        "growDiary",
                        "manufacture",
                        "index",
                        manufacture.name,
                        "icon_hemp",
                        mutableMapOf(
                            "manufactureId" to manufacture.id,
                            "name" to manufacture.name,
                        )
                    ),
                )
            )
        }

        textBuilder.build(
            view,
            inflater,
            layout,
            activity,
        )
    }
}