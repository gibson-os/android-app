package de.wollis_page.gibsonos.module.hc.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.index.fragment.LogFragment
import de.wollis_page.gibsonos.service.AppIntentExtraService

abstract class ModuleActivity : TabActivity() {
    lateinit var module: Module

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab(R.string.hc_log_tab, LogFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val parameters = (AppIntentExtraService.getIntentExtra(SHORTCUT_KEY, this.intent) as Shortcut?)?.parameters
        this.module = (AppIntentExtraService.getIntentExtra("module", intent) as Module?)
            ?: Module(
                parameters!!["id"].toString().toDouble().toLong(),
                parameters["type"].toString(),
                parameters["name"].toString(),
                parameters["address"].toString().toDouble().toInt(),
                parameters["helper"].toString(),
                parameters["modified"].toString(),
                null
            )

        super.onCreate(savedInstanceState)

        this.setTitle(this.module.name)
    }

    override fun getId(): Any {
        return this.module.id
    }

}