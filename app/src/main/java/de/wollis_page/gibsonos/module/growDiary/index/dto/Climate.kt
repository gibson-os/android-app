package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Climate(
    var id: Long,
    var name: String,
    var type: String,
    var watt: Long,
): ListItemInterface {
    override fun getId() = this.id
}
