package de.wollis_page.gibsonos.module.hc.index.dto

import android.content.Intent
import de.wollis_page.gibsonos.dto.ActivityExtrasInterface

data class MasterActivityExtras (
    val master: Master
): ActivityExtrasInterface {
    override fun putIntentExtras(intent: Intent) {
        intent.putExtra("master", this.master)
    }
}