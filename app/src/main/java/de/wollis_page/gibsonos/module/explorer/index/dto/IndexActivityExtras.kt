package de.wollis_page.gibsonos.module.explorer.index.dto

import android.content.Intent
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ActivityExtrasInterface
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut

data class IndexActivityExtras (
    val directory: String,
    val position: Int? = null,
    val token: String? = null,
): ActivityExtrasInterface {
    override fun putIntentExtras(intent: Intent) {
        intent.putExtra(GibsonOsActivity.SHORTCUT_KEY, Shortcut(
            "explorer",
            "index",
            "index",
            "",
            "",
            "",
            0,
            mapOf("dir" to directory)
        ))
        intent.putExtra("position", this.position)
        intent.putExtra("token", this.token)
    }
}