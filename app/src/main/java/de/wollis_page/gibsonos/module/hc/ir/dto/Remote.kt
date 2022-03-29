package de.wollis_page.gibsonos.module.hc.ir.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.hc.ir.dto.remote.Key

data class Remote(
    val id: Long,
    val name: String,
    val width: Int,
    val height: Int,
    val background: String?,
    val keys: MutableList<Key>
): ListItemInterface {
    override fun getId(): Any {
        return this.id
    }
}
