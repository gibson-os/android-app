package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Manufacture(
    var id: Long,
    var name: String,
    var url: String,
): ListItemInterface {
    override fun getId() = this.id
}
