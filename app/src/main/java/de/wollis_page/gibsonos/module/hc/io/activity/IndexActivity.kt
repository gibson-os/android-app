package de.wollis_page.gibsonos.module.hc.io.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.dto.Update
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.io.dto.Port
import de.wollis_page.gibsonos.module.hc.io.fragment.IndexFragment

class IndexActivity : ModuleActivity() {
    override fun getAppIcon(): Int = R.drawable.ic_switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.update = Update(
            "hc",
            "io",
            "index",
            this.module.id.toString(),
            Port::class
        )
    }

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab(R.string.hc_module_io_tab, IndexFragment::class, arguments),
            *super.getTabs()
        )
    }
}