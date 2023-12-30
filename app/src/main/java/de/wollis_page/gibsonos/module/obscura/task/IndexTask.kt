package de.wollis_page.gibsonos.module.obscura.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.obscura.scanner.dto.Scanner
import de.wollis_page.gibsonos.task.AbstractTask

object IndexTask: AbstractTask() {
    @Throws(TaskException::class)
    fun getScanner(context: GibsonOsActivity, start: Long, limit: Long): ListResponse<Scanner> {
        val dataStore = this.getDataStore(context.getAccount(), "obscura", "index", "scanner")

        return this.loadList(context, dataStore, start, limit, R.string.obscura_scanner_error_response)
    }
}