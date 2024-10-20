package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Image(
    var id: Long,
    var filename: String,
    var description: String?,
    var created: String,
    var data: String?,
): ListItemInterface {
    override fun getId() = this.id
}
