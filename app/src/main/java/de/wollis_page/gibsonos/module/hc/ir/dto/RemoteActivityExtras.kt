package de.wollis_page.gibsonos.module.hc.ir.dto

import android.content.Intent
import de.wollis_page.gibsonos.dto.ActivityExtrasInterface
import de.wollis_page.gibsonos.module.hc.index.dto.Module

data class RemoteActivityExtras(
    val module: Module,
    val remoteId: Long
): ActivityExtrasInterface {
    override fun putIntentExtras(intent: Intent) {
        intent.putExtra("module", this.module)
        intent.putExtra("remoteId", this.remoteId)
    }
}
