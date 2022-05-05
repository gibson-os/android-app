package de.wollis_page.gibsonos.module.hc.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.hc.index.fragment.LogFragment
import de.wollis_page.gibsonos.module.hc.index.fragment.MasterFragment

class IndexActivity : TabActivity(), AppActivityInterface {
    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.hc_master_tab, MasterFragment::class),
        Tab(R.string.hc_log_tab, LogFragment::class),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.hc_title)
    }

    override fun getId(): Any {
        return 0
    }

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean {
        return true
    }

    override fun getAppIcon() = R.drawable.ic_house_laptop_solid
}