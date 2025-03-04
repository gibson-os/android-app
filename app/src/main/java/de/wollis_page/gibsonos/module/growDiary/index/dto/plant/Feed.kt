package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.feed.Additive

data class Feed(
    var id: Long,
    var added: String,
    var day: Int,
    var milliliter: Long,
    var additives: List<Additive>,
    var ph: Float?,
): ListItemInterface {
    override fun getId() = this.id
}
