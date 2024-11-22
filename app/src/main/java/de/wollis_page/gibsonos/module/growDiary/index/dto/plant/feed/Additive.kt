package de.wollis_page.gibsonos.module.growDiary.index.dto.plant.feed

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Fertilizer

data class Additive(
    var id: Long,
    var milliliter: Float,
    var fertilizer: Fertilizer,
): ListItemInterface {
    override fun getId() = this.id
}