package de.wollis_page.gibsonos.module.core.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.core.cronjob.dto.Cronjob
import de.wollis_page.gibsonos.task.AbstractTask

object CronjobTask: AbstractTask() {
    fun index(context: GibsonOsActivity): MutableList<Cronjob> {
        val response = this.run(context, this.getDataStore(context.getAccount(), "core", "cronjob", "index"))
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Cronjob::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Cronjob>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Cronjobs not in response!")
    }
}