package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Plant(
    var id: Long,
    var name: String,
    var seed: Seed,
    var grownAt: String?,
    var floweringAt: String?,
    var harvestedAt: String?,
    var harvestedWet: Float?,
    var harvestedDry: Float?,
    var grownDay: Int?,
    var floweringDay: Int?,
): ListItemInterface {
    override fun getId() = this.id
}
