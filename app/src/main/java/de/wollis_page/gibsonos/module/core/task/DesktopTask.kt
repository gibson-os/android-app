package de.wollis_page.gibsonos.module.core.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Desktop
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.task.AbstractTask

object DesktopTask: AbstractTask() {
    fun index(context: GibsonOsActivity): Desktop {
        return this.load(
            context,
            this.getDataStore(context.getAccount(), "core", "desktop", "index"),
            R.string.account_error_not_exists
        )
    }

    fun add(context: GibsonOsActivity, shortcut: Shortcut) {
        val dataStore = this.getDataStore(context.getAccount(), "core", "desktop", "add")
        dataStore.addParam("items", this.getJsonAdapter<List<Shortcut>>().toJson(listOf(shortcut)))

        this.run(context, dataStore)
    }

    fun save(context: GibsonOsActivity, shortcuts: ArrayList<Shortcut>): MutableList<Shortcut> {
        val dataStore = this.getDataStore(context.getAccount(), "core", "desktop", "save")
        dataStore.addParam("items", this.getJsonAdapter<List<Shortcut>>().toJson(shortcuts))

        val response = this.loadList<Shortcut>(context, dataStore)

        return response.data
    }
}