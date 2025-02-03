package de.wollis_page.gibsonos.module.growDiary.index.dto.setup

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.ClimateControl

class ClimateControl(
    var id: Long,
    var climateControl: ClimateControl,
    var useCase: String,
): ListItemInterface {
    override fun getId() = this.id
}
