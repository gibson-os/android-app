package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Seed(
    var id: Long,
    var name: String,
    var type: String,
    var minGrowingDays: Int?,
    var maxGrowingDays: Int?,
    var minFloweringDays: Int?,
    var maxFloweringDays: Int?,
    var minGrowingWeeks: Int?,
    var maxGrowingWeeks: Int?,
    var minFloweringWeeks: Int?,
    var maxFloweringWeeks: Int?,
    var plantMinDays: Int?,
    var plantMaxDays: Int?,
    var plantMinWeeks: Int?,
    var plantMaxWeeks: Int?,
    var minHeight: Int?,
    var maxHeight: Int?,
    var price: Int?,
    var manufacture: Manufacture?,
): ListItemInterface {
    override fun getId() = this.id
}
