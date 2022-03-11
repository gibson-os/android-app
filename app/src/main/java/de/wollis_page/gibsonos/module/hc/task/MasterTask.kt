package de.wollis_page.gibsonos.module.hc.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.task.AbstractTask

object MasterTask: AbstractTask() {
    @Throws(TaskException::class)
    fun index(context: GibsonOsActivity): MutableList<Master> {
        val response = this.run(context, this.getDataStore(context.getAccount(), "hc", "master", "index"))
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Master::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Master>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Masters not in response!")
    }
}