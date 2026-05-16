package de.wollis_page.gibsonos.module.tc.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Train(
    var id: Long,
    var name: String,
): ListItemInterface {
    override fun getId() = this.id
}
