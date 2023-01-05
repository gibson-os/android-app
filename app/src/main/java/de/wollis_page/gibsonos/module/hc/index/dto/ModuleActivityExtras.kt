package de.wollis_page.gibsonos.module.hc.index.dto

import android.content.Intent
import de.wollis_page.gibsonos.dto.ActivityExtrasInterface

data class ModuleActivityExtras(
    val module: Module
): ActivityExtrasInterface {
    override fun putIntentExtras(intent: Intent) {
        intent.putExtra("module", this.module)
    }
}
