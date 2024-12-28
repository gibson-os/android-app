package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Pot(
    var id: Long,
    var name: String,
    var liter: String,
    var manufactureId: Manufacture?,
): ListItemInterface {
    override fun getId() = this.id
}
