package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.State

data class Plant(
    var id: Long,
    var name: String,
    var seed: Seed,
    var state: State?,
    var states: Map<String, State>?,
): ListItemInterface {
    override fun getId() = this.id
}
