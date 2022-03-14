package de.wollis_page.gibsonos.module.hc.module.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.module.ir.dto.Key
import de.wollis_page.gibsonos.module.hc.module.ir.dto.Remote
import de.wollis_page.gibsonos.task.AbstractTask

object IrTask: AbstractTask() {
    @Throws(TaskException::class)
    fun remotes(context: GibsonOsActivity, moduleId: Long): MutableList<Remote> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "ir", "remotes")
        dataStore.addParam("moduleId", moduleId)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Remote::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Remote>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Remotes not in response!")
    }

    @Throws(TaskException::class)
    fun keys(context: GibsonOsActivity): MutableList<Key> {
        val response = this.run(context, this.getDataStore(context.getAccount(), "hc", "ir", "keys"))
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Key::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Key>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Keys not in response!")
    }
}