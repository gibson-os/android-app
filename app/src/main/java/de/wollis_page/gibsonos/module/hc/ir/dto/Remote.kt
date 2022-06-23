package de.wollis_page.gibsonos.module.hc.ir.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.hc.ir.dto.remote.Button

data class Remote(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val background: String?,
    val buttons: MutableList<Button>
): ListItemInterface {
    override fun getId(): Any {
        return this.id
    }
}
