package de.wollis_page.gibsonos.module.growDiary.index.dto.setup.climateControl

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Runtime(
    var id: Long,
    var from: String,
    var to: String?,
): ListItemInterface {
    override fun getId() = this.id
}