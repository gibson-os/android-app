package de.wollis_page.gibsonos.service

import de.wollis_page.gibsonos.module.hc.ir.activity.IndexActivity
import de.wollis_page.gibsonos.module.hc.ir.activity.RemoteActivity

object ActivityMatcher {
    private val activityMapping = mutableMapOf(
        "hc" to mutableMapOf(
            "ir" to mutableMapOf(
                "remote" to RemoteActivity::class.java.name
            )
        )
    )

    fun getActivity(
        module: String,
        task: String,
        action: String,
        parameters: MutableMap<String, Any>? = null,
    ): Class<*> {
        return Class.forName(this.getActivityName(module, task, action, parameters))
    }

    private fun getActivityName(
        module: String,
        task: String,
        action: String,
        parameters: MutableMap<String, Any>? = null,
    ): String {
        return this.activityMapping[module]?.get(task)?.get(action)
            ?: this.getActivityNameWithParameters(module, task, action, parameters)
            ?: ("de.wollis_page.gibsonos.module." +
                module + "." +
                task + ".activity." +
                (action.replaceFirstChar { it.uppercase() }) + "Activity")
    }

    private fun getActivityNameWithParameters(
        module: String,
        task: String,
        action: String,
        parameters: MutableMap<String, Any>? = null,
    ): String? {
        if (module == "hc" && task == "module" && action == "view") {
            return when (parameters?.get("type").toString().lowercase()) {
                "ir" -> IndexActivity::class.java.name
                else -> null
            }
        }

        return null
    }
}