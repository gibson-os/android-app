package de.wollis_page.gibsonos.module.growDiary.index.dto.setup.light

import de.wollis_page.gibsonos.dto.ListItemInterface

class PlannedRuntime(
    var id: Long,
    var from: String,
    var to: String?,
): ListItemInterface {
    override fun getId() = this.id
}