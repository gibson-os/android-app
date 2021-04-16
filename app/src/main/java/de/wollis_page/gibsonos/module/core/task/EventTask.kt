package de.wollis_page.gibsonos.module.core.task

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.core.event.dto.Event
import de.wollis_page.gibsonos.task.AbstractTask

object EventTask: AbstractTask() {
    fun index(context: GibsonOsActivity): MutableList<Event> {
        val response = this.run(context, this.getDataStore(context.getAccount(), "core", "event", "index"))
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, Event::class.java)
        val jsonAdapter = moshi.adapter<MutableList<Event>>(listType)

        return jsonAdapter.fromJson(response.getJSONArray("data").toString()) ?:
            throw TaskException("Events not in response!")
    }
    
    fun run(context: GibsonOsActivity, eventId: Int) {
        val dataStore = this.getDataStore(context.getAccount(), "core", "event", "run")
        dataStore.addParam("eventId", eventId)
        this.run(context, dataStore)
    }
}