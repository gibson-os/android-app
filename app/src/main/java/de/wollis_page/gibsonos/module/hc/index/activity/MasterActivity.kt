package de.wollis_page.gibsonos.module.hc.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.module.hc.index.fragment.LogFragment
import de.wollis_page.gibsonos.module.hc.index.fragment.ModuleFragment

class MasterActivity : TabActivity(), AppActivityInterface {
    private lateinit var master: Master

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("masterId" to this.master.id)
        return arrayOf(
            Tab(R.string.hc_module_tab, ModuleFragment::class, arguments),
            Tab(R.string.hc_log_tab, LogFragment::class, arguments),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.master = this.intent.getParcelableExtra("master")!!

        super.onCreate(savedInstanceState)

        this.setTitle(this.master.name)
    }

    override fun getAppIcon() = R.drawable.ic_stream
}