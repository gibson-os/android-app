package de.wollis_page.gibsonos.service

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity
import de.wollis_page.gibsonos.module.hc.index.activity.MasterActivity
import de.wollis_page.gibsonos.module.hc.ir.activity.RemoteActivity

object AppIconService {
    private val icons: Map<String, Int> = mapOf(
        de.wollis_page.gibsonos.module.core.cronjob.activity.IndexActivity::class.java.toString() to R.drawable.ic_calendar_alt,
        de.wollis_page.gibsonos.module.core.event.activity.IndexActivity::class.java.toString() to R.drawable.ic_stream,
        IndexActivity::class.java.toString() to R.drawable.ic_folder,
        de.wollis_page.gibsonos.module.hc.index.activity.IndexActivity::class.java.toString() to R.drawable.ic_house_laptop_solid,
        MasterActivity::class.java.toString() to R.drawable.ic_house_laptop_solid,
        de.wollis_page.gibsonos.module.hc.io.activity.IndexActivity::class.java.toString() to R.drawable.ic_switch,
        de.wollis_page.gibsonos.module.hc.ir.activity.IndexActivity::class.java.toString() to R.drawable.ic_remote_control,
        RemoteActivity::class.java.toString() to R.drawable.ic_remote_control,
        de.wollis_page.gibsonos.module.hc.neopixel.activity.IndexActivity::class.java.toString() to R.drawable.ic_led,
    )

    fun getIcon(activity: GibsonOsActivity): Int? {
        return this.getIcon(activity::class.java)
    }

    fun getIcon(className: Class<*>): Int? {
        return this.icons[className.name] ?: this.icons[className.superclass.name]
    }

    fun getIcon(module: String, task: String, action: String): Int? {
        return this.getIcon(Class.forName("de.wollis_page.gibsonos.module." +
            module + "." +
            task + ".activity." +
            (action.replaceFirstChar { it.uppercase() }) + "Activity"
        ))
    }
}