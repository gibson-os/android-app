package de.wollis_page.gibsonos.module.hc.module.ir.activity

import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.module.ir.fragment.KeyFragment
import de.wollis_page.gibsonos.module.hc.module.ir.fragment.RemoteFragment

class IndexActivity : ModuleActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab("Fernbedinungen", RemoteFragment::class, arguments),
            Tab("Tasten", KeyFragment::class, arguments),
            *super.getTabs()
        )
    }
}