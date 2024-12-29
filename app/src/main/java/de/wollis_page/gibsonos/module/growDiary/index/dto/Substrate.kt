package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Substrate(
    var id: Long,
    var name: String,
): ListItemInterface {
    override fun getId() = this.id
}
