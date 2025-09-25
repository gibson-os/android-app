package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Room(
    var id: Long,
    var name: String,
    var length: Long,
    var width: Long,
    var height: Long,
    var manufacture: Manufacture?,
): ListItemInterface {
    override fun getId() = this.id
}
