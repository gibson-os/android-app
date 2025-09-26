package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.Pot
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.pot.Substrate

data class Pot(
    var id: Long,
    var from: String,
    var to: String?,
    var pot: Pot,
    var substrates: List<Substrate>,
): ListItemInterface {
    override fun getId() = this.id
}
