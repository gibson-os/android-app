package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Milestone(
    var id: Long,
    var title: String,
    var value: String,
    var added: String,
): ListItemInterface {
    override fun getId() = this.id
}
