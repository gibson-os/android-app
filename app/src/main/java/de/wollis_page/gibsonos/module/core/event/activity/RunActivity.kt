package de.wollis_page.gibsonos.module.core.event.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.task.EventTask

class RunActivity: GibsonOsActivity() {
    override fun getContentView(): Int = R.layout.core_event_run_activity

    override fun getId(): Long = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = this
        activity.runTask({
            EventTask.run(
                activity,
                activity.getShortcut()?.parameters?.get("eventId").toString().toDouble().toLong()
            )
            activity.finish()
        })
    }
}