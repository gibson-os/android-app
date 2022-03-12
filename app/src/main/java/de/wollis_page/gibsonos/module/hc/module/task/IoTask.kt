package de.wollis_page.gibsonos.module.hc.module.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.module.io.dto.Port
import de.wollis_page.gibsonos.task.AbstractTask

object IoTask: AbstractTask() {
    @Throws(TaskException::class)
    fun ports(context: GibsonOsActivity, moduleId: Long): MutableList<Port> {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "io", "ports")
        dataStore.addParam("moduleId", moduleId)
        val response = this.run(context, dataStore)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Port::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Port>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Modules not in response!")
    }

    @Throws(TaskException::class)
    fun toggle(context: GibsonOsActivity, moduleId: Long, number: Int) {
        val dataStore = this.getDataStore(context.getAccount(), "hc", "io", "toggle")
        dataStore.addParam("moduleId", moduleId)
        dataStore.addParam("number", number)
        this.run(context, dataStore)
    }
}