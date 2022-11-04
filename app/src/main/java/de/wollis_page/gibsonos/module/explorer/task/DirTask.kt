package de.wollis_page.gibsonos.module.explorer.task

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.explorer.index.dto.Dir
import de.wollis_page.gibsonos.module.explorer.index.dto.DirList
import de.wollis_page.gibsonos.task.AbstractTask

object DirTask: AbstractTask() {
    @Throws(TaskException::class)
    fun read(context: GibsonOsActivity, directory: String): Dir {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "dir", "read")
        dataStore.addParam("dir", directory)

        val response = this.run(context, dataStore)
        val jsonAdapter = this.getJsonAdapter<Dir>()

        return jsonAdapter.fromJson(response.toString()) ?:
            throw TaskException("Dir not in response!", R.string.explorer_dir_error_response)
    }

    @Throws(TaskException::class)
    fun dirList(context: GibsonOsActivity, directory: String): ListResponse<DirList> {
        val dataStore = this.getDataStore(context.getAccount(), "explorer", "dir", "dirList")
        dataStore.addParam("dir", directory)

        return this.loadList(context, dataStore, 0, 1, R.string.explorer_dir_list_error_response)
    }
}