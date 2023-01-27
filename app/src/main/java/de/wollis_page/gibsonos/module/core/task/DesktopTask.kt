package de.wollis_page.gibsonos.module.core.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Desktop
import de.wollis_page.gibsonos.task.AbstractTask

object DesktopTask: AbstractTask() {
    fun index(context: GibsonOsActivity): Desktop {
        return this.load(
            context,
            this.getDataStore(context.getAccount(), "core", "desktop", "index"),
            R.string.account_error_not_exists
        )
    }

    fun add(context: GibsonOsActivity) {
        val dataStore = this.getDataStore(context.getAccount(), "core", "desktop", "add")

        this.run(context, dataStore)
    }
}