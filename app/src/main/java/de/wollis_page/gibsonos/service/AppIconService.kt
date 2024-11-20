package de.wollis_page.gibsonos.service

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.hc.index.activity.MasterActivity
import de.wollis_page.gibsonos.module.hc.ir.activity.RemoteActivity

object AppIconService {
    private val icons: Map<String, Int> = mapOf(
        de.wollis_page.gibsonos.module.core.cronjob.activity.IndexActivity::class.java.name to R.drawable.ic_calendar_alt,
        de.wollis_page.gibsonos.module.core.event.activity.IndexActivity::class.java.name to R.drawable.ic_stream,
        de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity::class.java.name to R.drawable.ic_folder,
        de.wollis_page.gibsonos.module.hc.index.activity.IndexActivity::class.java.name to R.drawable.ic_house_laptop_solid,
        MasterActivity::class.java.name to R.drawable.ic_house_laptop_solid,
        de.wollis_page.gibsonos.module.hc.io.activity.IndexActivity::class.java.name to R.drawable.ic_switch,
        de.wollis_page.gibsonos.module.hc.ir.activity.IndexActivity::class.java.name to R.drawable.ic_remote_control,
        RemoteActivity::class.java.name to R.drawable.ic_remote_control,
        de.wollis_page.gibsonos.module.hc.neopixel.activity.IndexActivity::class.java.name to R.drawable.ic_led,
        de.wollis_page.gibsonos.module.core.message.activity.IndexActivity::class.java.name to R.drawable.ic_message,
        de.wollis_page.gibsonos.module.obscura.index.activity.IndexActivity::class.java.name to R.drawable.ic_expand,
        de.wollis_page.gibsonos.module.obscura.scanner.activity.FormActivity::class.java.name to R.drawable.ic_expand,
        de.wollis_page.gibsonos.module.growDiary.index.activity.IndexActivity::class.java.name to R.drawable.ic_hemp,
        de.wollis_page.gibsonos.module.growDiary.plant.activity.IndexActivity::class.java.name to R.drawable.ic_hemp,
    )

    fun getIcon(activity: GibsonOsActivity): Int? {
        return this.getIcon(activity::class.java)
    }

    fun getIcon(className: Class<*>): Int? {
        return this.icons[className.name] ?: this.icons[className.superclass.name]
    }

    fun getIcon(
        module: String,
        task: String, action:
        String,
        parameters: MutableMap<String, Any>? = null,
    ): Int? {
        return try {
            this.getIcon(ActivityMatcher.getActivity(module, task, action, parameters))
        } catch (exception: ClassNotFoundException) {
            null
        }
    }
}