package de.wollis_page.gibsonos.module.growDiary.index.dto.plant.pot

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.Substrate

data class Substrate(
    var id: Long,
    var substrate: Substrate,
    var liter: Float,
    var pricePerUnit: Int?,
): ListItemInterface {
    override fun getId() = this.id
}