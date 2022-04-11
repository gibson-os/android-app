package de.wollis_page.gibsonos.module.hc.ir.activity

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.ir.fragment.KeyFragment
import de.wollis_page.gibsonos.module.hc.ir.fragment.RemoteFragment

class IndexActivity : ModuleActivity() {
    override fun getAppIcon(): Int = R.drawable.ic_remote_control

    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab(R.string.hc_module_ir_remote_tab, RemoteFragment::class, arguments),
            Tab(R.string.hc_module_ir_key_tab, KeyFragment::class, arguments),
            *super.getTabs()
        )
    }
}