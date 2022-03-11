package de.wollis_page.gibsonos.module.hc.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.task.AbstractTask

object ModuleTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsActivity, masterId: Long): MutableList<Module> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "slave", "index")
        dataStore.addParam("masterId", masterId)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Module::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Module>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Modules not in response!")
    }
}