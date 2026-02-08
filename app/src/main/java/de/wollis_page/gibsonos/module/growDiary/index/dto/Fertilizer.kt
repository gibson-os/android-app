package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Fertilizer(
    var id: Long,
    var name: String,
    var pricePerUnit: Int,
    var unit: String,
    var unitSuffix: String,
    var formUnit: String,
    var bySubstrateLiter: Boolean,
    var manufacture: Manufacture?,
): ListItemInterface {
    override fun getId() = this.id
}
