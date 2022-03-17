package de.wollis_page.gibsonos.module.hc.module.io.activity

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.module.io.fragment.IndexFragment

class IndexActivity : ModuleActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab(R.string.hc_module_io_tab, IndexFragment::class, arguments),
            *super.getTabs()
        )
    }
}