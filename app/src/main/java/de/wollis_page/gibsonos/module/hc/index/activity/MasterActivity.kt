package de.wollis_page.gibsonos.module.hc.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.module.hc.index.fragment.LogFragment
import de.wollis_page.gibsonos.module.hc.index.fragment.ModuleFragment
import de.wollis_page.gibsonos.service.AppIntentExtraService

class MasterActivity : TabActivity() {
    private lateinit var master: Master

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("masterId" to this.master.id)
        return arrayOf(
            Tab(R.string.hc_module_tab, ModuleFragment::class, arguments),
            Tab(R.string.hc_log_tab, LogFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val shortcut = AppIntentExtraService.getIntentExtra(SHORTCUT_KEY, this.intent) as Shortcut?
        this.master = (AppIntentExtraService.getIntentExtra("master", intent) as Master?)
            ?: Master(
                (shortcut!!.parameters!!["id"] as Double).toLong(),
                shortcut.parameters!!["name"] as String,
                shortcut.parameters!!["address"] as String,
                shortcut.parameters!!["protocol"] as String,
                shortcut.parameters!!["added"] as String,
                shortcut.parameters!!["modified"] as String,
            )

        super.onCreate(savedInstanceState)

        this.setTitle(this.master.name)
    }

    override fun getId() = this.master.getId()
}