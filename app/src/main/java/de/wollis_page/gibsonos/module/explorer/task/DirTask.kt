package de.wollis_page.gibsonos.module.explorer.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.explorer.index.dto.Dir
import de.wollis_page.gibsonos.task.AbstractTask

object DirTask: AbstractTask() {
    @Throws(TaskException::class)
    fun read(context: GibsonOsActivity, account: Account, directory: String): Dir {
        val dataStore = this.getDataStore(account, "explorer", "dir", "read")
        dataStore.addParam("dir", directory)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Dir::class.java)

        return jsonAdapter.fromJson(response.toString()) ?:
            throw TaskException("Dir not in response!", R.string.explorer_dir_error_response)
    }
}