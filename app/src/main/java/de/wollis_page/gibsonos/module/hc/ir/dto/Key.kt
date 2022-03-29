package de.wollis_page.gibsonos.module.hc.ir.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Key(
    var id: Long,
    var protocol: Int,
    var address: Int,
    var command: Int,
    var protocolName: String?,
    var name: String?,
): ListItemInterface {
    override fun getId(): Any {
        return this.id
    }
}
