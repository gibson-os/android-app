package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.Fertilizer
import de.wollis_page.gibsonos.module.growDiary.index.dto.fertilizer.Scheme

data class Fertilizer(
    var id: Long,
    var fertilizer: Fertilizer,
    var scheme: Scheme,
): ListItemInterface {
    override fun getId() = this.id
}
