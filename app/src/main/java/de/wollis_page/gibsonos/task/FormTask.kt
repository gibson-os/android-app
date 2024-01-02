package de.wollis_page.gibsonos.task

import de.wollis_page.gibsonos.activity.GibsonOsActivity

object FormTask: AbstractTask() {
    fun post(
        context: GibsonOsActivity,
        module: String,
        task: String,
        action: String,
        parameters: Map<String, *>,
    ) {
        val dataStore = this.getDataStore(context.getAccount(), module, task, action, "POST")

        parameters.forEach {
            dataStore.addParam(it.key, it.value.toString())
        }

        this.run(context, dataStore)
    }
}