package de.wollis_page.gibsonos.service

import android.content.Intent
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.dto.ActivityExtrasInterface
import de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.IndexActivityExtras
import de.wollis_page.gibsonos.module.hc.index.activity.MasterActivity
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.index.dto.MasterActivityExtras
import de.wollis_page.gibsonos.module.hc.index.dto.ModuleActivityExtras
import de.wollis_page.gibsonos.module.hc.ir.activity.RemoteActivity
import de.wollis_page.gibsonos.module.hc.ir.dto.RemoteActivityExtras

object AppIntentExtraService {
    private val extras: Map<String, Class<*>> = mapOf(
        IndexActivity::class.java.name to IndexActivityExtras::class.java,
        MasterActivity::class.java.name to MasterActivityExtras::class.java,
        ModuleActivity::class.java.name to ModuleActivityExtras::class.java,
        RemoteActivity::class.java.name to RemoteActivityExtras::class.java,
    )

    fun putExtras(module: String, task: String, action: String, intent: Intent, extras: String) {
        this.putExtras(
            Class.forName(
                "de.wollis_page.gibsonos.module." +
                module + "." +
                task + ".activity." +
                (action.replaceFirstChar { it.uppercase() }) + "Activity",
            ),
            intent,
            extras,
        )
    }

    fun putExtras(className: Class<*>, intent: Intent, extras: String) {
        val activityExtras: Class<*> = this.extras[className.name]
            ?: this.extras[className.superclass.name]
            ?: return
        val extrasDto = this.getAdapter(activityExtras).fromJson(extras) as ActivityExtrasInterface

        extrasDto.putIntentExtras(intent)
    }

    private fun getAdapter(className: Class<*>): JsonAdapter<*>
    {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        return moshi.adapter(className)
    }
}