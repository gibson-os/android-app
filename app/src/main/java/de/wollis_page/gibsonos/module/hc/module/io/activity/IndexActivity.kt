package de.wollis_page.gibsonos.module.hc.module.io.activity

import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.module.io.fragment.IndexFragment

class IndexActivity : ModuleActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("moduleId" to this.module.id)

        return arrayOf(
            Tab("IO", IndexFragment::class, arguments),
            *super.getTabs()
        )
    }
}