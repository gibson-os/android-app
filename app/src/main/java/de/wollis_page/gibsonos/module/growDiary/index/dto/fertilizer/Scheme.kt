package de.wollis_page.gibsonos.module.growDiary.index.dto.fertilizer

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Scheme(
    var id: Long,
    var name: String,
): ListItemInterface {
    override fun getId() = this.id
}
