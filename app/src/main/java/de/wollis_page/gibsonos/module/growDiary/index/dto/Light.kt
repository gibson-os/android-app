package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Light(
    var id: Long,
    var name: String,
    var watt: Long,
    var manufactureId: Long,
): ListItemInterface {
    override fun getId() = this.id
}
