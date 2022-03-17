package de.wollis_page.gibsonos.module.hc.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.index.fragment.LogFragment

abstract class ModuleActivity : TabActivity(), AppActivityInterface {
    protected lateinit var module: Module

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab(R.string.hc_log_tab, LogFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.module = this.intent.getParcelableExtra("module")!!

        super.onCreate(savedInstanceState)

        this.setTitle(this.module.name)
    }

    override fun getAppIcon() = R.drawable.ic_stream
}