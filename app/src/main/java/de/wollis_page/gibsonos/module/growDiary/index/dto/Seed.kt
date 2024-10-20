package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Seed(
    var id: Long,
    var name: String,
    var type: String,
    var minFloweringDays: Int,
    var maxFloweringDays: Int,
    var minHeight: Int,
    var maxHeight: Int,
): ListItemInterface {
    override fun getId() = this.id
}
