package de.wollis_page.gibsonos.module.core.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.core.event.dto.Event
import de.wollis_page.gibsonos.task.AbstractTask

object EventTask: AbstractTask() {
    fun index(context: GibsonOsActivity): Event {
        val response = run(context, getDataStore(context.getAccount(), "core", "event", "index"))
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Event::class.java)

        return jsonAdapter.fromJson(response.getJSONObject("data").toString()) ?:
            throw TaskException("Events not in response!")
    }
}